/*
 * Created by JFormDesigner on Mon Jan 04 12:19:53 EST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.turtleisaac.pokeditor.editors.babies.BabyFormEditor;
import com.turtleisaac.pokeditor.editors.encounters.johto.JohtoEncounterEditor;
import com.turtleisaac.pokeditor.editors.encounters.sinnoh.SinnohEncounterEditor;
import com.turtleisaac.pokeditor.editors.evolutions.gen4.EvolutionEditor;
import com.turtleisaac.pokeditor.editors.items.ItemEditorGen4;
import com.turtleisaac.pokeditor.editors.items.ItemTableEntry;
import com.turtleisaac.pokeditor.editors.learnsets.LearnsetEditor;
import com.turtleisaac.pokeditor.editors.moves.gen4.MoveEditorGen4;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerReturnGen4;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalEditor;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerEditorGen4;
import com.turtleisaac.pokeditor.gui.JCheckboxTree;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.ProjectWindow;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import net.miginfocom.swing.*;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import turtleisaac.GoogleSheetsAPI;

/**
 * @author turtleisaac
 * This class is for applying sheet data to a ROM
 */
public class SheetApplier extends JFrame
{
    private Project project;
    private Game baseRom;
    private String[] editors;
    private String projectPath;

    private GoogleSheetsAPI api;
    private JFrame parent;
    ArrayList<ItemTableEntry> itemTableData;

    public SheetApplier(Project project, String projectPath, GoogleSheetsAPI api, JFrame parent, ArrayList<ItemTableEntry> itemTableData)
    {
        initComponents();
        DefaultMutableTreeNode root= new DefaultMutableTreeNode("All Editors");
        baseRom= project.getBaseRom();
        editors= baseRom.editorList;
        this.project= project;
        baseRom= project.getBaseRom();
        editors= baseRom.editorList;
        this.projectPath = projectPath;
        this.api= api;
        this.parent= parent;
        this.itemTableData = itemTableData;

        for(String str : editors)
        {
            DefaultMutableTreeNode child= new DefaultMutableTreeNode(str);
            root.add(child);
        }



        editorTree= new JCheckboxTree(root);
        editorTree.setVisible(true);
        setPreferredSize(new Dimension(300,350));
        scrollPane1.setViewportView(editorTree);

        pack();
        setVisible(true);
        toFront();
    }

    private void optionsButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
    }

    private ArrayList<File> toDelete;

    private void applyButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        toDelete= new ArrayList<>();
        String selectedString= Arrays.toString(editorTree.getCheckedPaths());
        selectedString= selectedString.substring(0, selectedString.length()-1);

        String[] selected= selectedString.split(", ");

        try
        {
            try
            {
                api.updateSheet("Formatting (DO NOT TOUCH)",FormatGenerator.updateFormatSheet(project));
            }
            catch(IOException exception)
            {
                System.err.println("Failure to perform automatic formatting sheet update and verification. Do you own this sheet?");
            }

            Narctowl narctowl= new Narctowl(true);
            String dataPath= project.getDataPath();
            String unpackedFolderPath = projectPath + File.separator + "temp" + File.separator;


            switch (baseRom)
            {
                case Platinum:
                    if(contains(selected, "Personal"))
                    {
                        String outputDir= unpackedFolderPath + "pl_personal";
                        String outputNarc= File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal.narc";

                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        editor.csvToPersonal(api.getSpecifiedSheetArr("Personal"),api.getSpecifiedSheetArr("TM Learnsets"),outputDir,dataPath + outputNarc);

                        pack(outputDir, dataPath + outputNarc);
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        String outputDir= unpackedFolderPath + "wotbl";
                        String outputNarc= File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl.narc";

                        LearnsetEditor editor= new LearnsetEditor(dataPath, project);
                        editor.sheetToLearnsets(api.getSpecifiedSheetArr("Level-Up Learnsets"),outputDir);

                        pack(outputDir, dataPath + outputNarc);
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        String outputDir= unpackedFolderPath + "evo";
                        String outputNarc= File.separator + "poketool" + File.separator + "personal" + File.separator + "evo.narc";

                        EvolutionEditor editor= new EvolutionEditor(project, dataPath, baseRom);
                        editor.sheetToEvolutions(api.getSpecifiedSheetArr("Evolutions"),outputDir);

                        pack(outputDir, dataPath + outputNarc);
                    }
//
//                    if(contains(selected,"Tutor Move"))
//                    {
//                        JOptionPane.showMessageDialog(this,"Tutor Move editors not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
//                    }
//
                    if(contains(selected,"Baby Forms"))
                    {
                        String outputNarc= File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc";

                        BabyFormEditor editor= new BabyFormEditor(dataPath,project);
                        editor.sheetToBabyForms(api.getSpecifiedSheetArr("Baby Forms"),outputNarc);
                    }

                    if(contains(selected, "Trainers"))
                    {
                        String trainerDir= File.separator + "poketool" + File.separator + "trainer" + File.separator + "";

                        String trdataDir= unpackedFolderPath + "trdata";
                        String trdataNarc= trainerDir + "trdata.narc";
                        String trpokeDir= unpackedFolderPath + "trpoke";
                        String trpokeNarc= trainerDir + "trpoke.narc";

                        TrainerEditorGen4 editor= new TrainerEditorGen4(project,dataPath,baseRom);
                        editor.sheetsToTrainers(api.getSpecifiedSheetArr("Trainer Data"),api.getSpecifiedSheetArr("Trainer Pokemon"),unpackedFolderPath);

                        pack(trdataDir, dataPath + trdataNarc);
                        pack(trpokeDir, dataPath + trpokeNarc);
                    }

                    if(contains(selected,"Moves"))
                    {
                        String outputDir= unpackedFolderPath + "pl_waza_tbl";
                        String outputNarc= File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl.narc";

                        MoveEditorGen4 editor= new MoveEditorGen4(project,dataPath);
                        editor.sheetToMoves(api.getSpecifiedSheetArr("Moves"),outputDir, outputNarc);

                        pack(outputDir, dataPath + outputNarc);
                    }

                    if(contains(selected,"Items"))
                    {
                        String outputDir= unpackedFolderPath + "pl_item_data";
                        String outputNarc= File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "pl_item_data.narc";

                        ItemEditorGen4 editor= new ItemEditorGen4(dataPath,project,itemTableData);
                        editor.sheetToItems(api.getSpecifiedSheetArr("Items"),outputDir,outputNarc);

                        pack(outputDir, dataPath + outputNarc);
                    }

                    if(contains(selected,"Encounters"))
                    {
                        SinnohEncounterEditor editor= new SinnohEncounterEditor(project,dataPath);

                        String outputDir= unpackedFolderPath + "pl_enc_data";
                        String outputNarc= File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "pl_enc_data.narc";

                        editor.sheetsToEncounters(api.getSpecifiedSheetArr("Field Encounters"),api.getSpecifiedSheetArr("Water Encounters"),api.getSpecifiedSheetArr("Swarm/ Day/ Night Encounters"),api.getSpecifiedSheetArr("Poke Radar Encounters"),api.getSpecifiedSheetArr("Dual-Slot Mode Encounters"),api.getSpecifiedSheetArr("Alt Form Encounters"),outputDir);
                        pack(outputDir, dataPath + outputNarc);
                    }
                    break;

                case HeartGold:
                case SoulSilver:
                    if(contains(selected, "Personal"))
                    {
                        String outputDir= unpackedFolderPath + "personal";
                        String outputNarc= File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2";

                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        editor.csvToPersonal(api.getSpecifiedSheetArr("Personal"),api.getSpecifiedSheetArr("TM Learnsets"),outputDir,outputNarc);

                        narctowl.pack(outputDir,"",dataPath + outputNarc);
                        toDelete.add(new File(outputDir));
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        String outputDir= unpackedFolderPath + "wotbl";
                        String outputNarc= File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3";

                        LearnsetEditor editor= new LearnsetEditor(dataPath, project);
                        editor.sheetToLearnsets(api.getSpecifiedSheetArr("Level-Up Learnsets"),outputDir);

                        narctowl.pack(outputDir,"",dataPath + outputNarc);
                        toDelete.add(new File(outputDir));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        String outputDir= unpackedFolderPath + "evo";
                        String outputNarc= File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4";

                        EvolutionEditor editor= new EvolutionEditor(project, dataPath, baseRom);
                        editor.sheetToEvolutions(api.getSpecifiedSheetArr("Evolutions"),outputDir);
                        narctowl.pack(outputDir,"",dataPath + outputNarc);
                        toDelete.add(new File(outputDir));
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath,project);
                        editor.sheetToBabyForms(api.getSpecifiedSheetArr("Baby Forms"),File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc");
                    }

                    if(contains(selected,"Moves"))
                    {
                        String outputDir= unpackedFolderPath + "item_data";
                        String outputNarc= File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "1";

                        MoveEditorGen4 editor= new MoveEditorGen4(project,dataPath);
                        editor.sheetToMoves(api.getSpecifiedSheetArr("Moves"),outputDir,outputNarc);

                        pack(outputDir, dataPath + outputNarc);
                    }

                    if(contains(selected,"Encounters"))
                    {
                        JohtoEncounterEditor editor= new JohtoEncounterEditor(project, dataPath);
                        Object[][] field= api.getSpecifiedSheetArr("Field Encounters");
                        Object[][] water= api.getSpecifiedSheetArr("Water Encounters");
                        Object[][] smash= api.getSpecifiedSheetArr("Rock Smash Encounters");
                        Object[][] outbreak= api.getSpecifiedSheetArr("Mass-Outbreak Encounters");
                        Object[][] sound= api.getSpecifiedSheetArr("Sound Encounters");

                        String outputDir= unpackedFolderPath + "enc_data";
                        String outputNarc;


                        if(baseRom == Game.HeartGold) //HG
                        {
                            outputNarc= File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7";

                            editor.sheetsToEncounters(field,water,smash,outbreak,sound,outputDir);
                            narctowl.pack(outputDir,"",outputNarc);
                        }
                        else //SS
                        {
                            outputNarc= File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6";

                            editor.sheetsToEncounters(field,water,smash,outbreak,sound,outputDir);
                            narctowl.pack(outputDir,"",dataPath + outputNarc);
                        }

                        toDelete.add(new File(outputDir));
                    }

                    if(contains(selected, "Trainer"))
                    {
                        String trainerDir= File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "";

                        String trdataDir= unpackedFolderPath + "trdata";
                        String trdataNarc= trainerDir + "5";
                        String trpokeDir= unpackedFolderPath + "trpoke";
                        String trpokeNarc= trainerDir + "6";

                        TrainerEditorGen4 editor= new TrainerEditorGen4(project,dataPath,baseRom);
                        editor.sheetsToTrainers(api.getSpecifiedSheetArr("Trainer Data"),api.getSpecifiedSheetArr("Trainer Pokemon"),unpackedFolderPath);

                        pack(trdataDir, dataPath + trdataNarc);
                        pack(trpokeDir, dataPath + trpokeNarc);
                    }

                    if(contains(selected,"Items"))
                    {
                        String outputDir= unpackedFolderPath + "item_data";
                        String outputNarc= File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "7";

                        ItemEditorGen4 editor= new ItemEditorGen4(dataPath,project,itemTableData);
                        editor.sheetToItems(api.getSpecifiedSheetArr("Items"),outputDir,outputNarc);

                        pack(outputDir, dataPath + outputNarc);
                    }
                    break;
            }


        }
        catch (IOException exception)
        {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(this,"An error has occurred. Please check the cmd window for further details.","Error",JOptionPane.ERROR_MESSAGE);
            dispose();
            parent.toFront();
            parent.setEnabled(true);
        }

        for(File file : toDelete)
        {
            System.out.println("Deleting folder: " + file.toString());
            clearDirs(file);
        }

        if(editorTree.getCheckedPaths().length == 0)
            JOptionPane.showMessageDialog(this,"You haven't selected anything to apply!","Error",JOptionPane.ERROR_MESSAGE);
        else if(!contains(selected,"Tutor Move"))
        {
            JOptionPane.showMessageDialog(this,"Sheets applied to ROM!","Success",JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Sheets applied to ROM");
            dispose();
            parent.toFront();
            parent.setEnabled(true);
        }
    }

    private void pack(String outputDir, String outputNarc) throws IOException
    {
        Narctowl narctowl= new Narctowl(true);
        narctowl.pack(outputDir,"",outputNarc);
        toDelete.add(new File(outputDir));
    }

    private void infoButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        EditApplierInfo applierInfo= new EditApplierInfo(baseRom);
        applierInfo.setLocationRelativeTo(this);
    }

    private void thisWindowClosing(WindowEvent e)
    {
        parent.setEnabled(true);
        parent.toFront();
        dispose();
        ((ProjectWindow) parent).sheetChooserComboBoxActionPerformed(new ActionEvent(this,0,null));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        editorTree = new JCheckboxTree();
        optionsButton = new JButton();
        infoButton = new JButton();
        applyButton = new JButton();

        //======== this ========
        setTitle("Apply Sheets to ROM");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[grow,fill]",
            // rows
            "[grow,fill]" +
            "[]"));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(editorTree);
        }
        contentPane.add(scrollPane1, "cell 0 0 3 1");

        //---- optionsButton ----
        optionsButton.setText("Options");
        optionsButton.addActionListener(e -> optionsButtonActionPerformed(e));
        contentPane.add(optionsButton, "cell 0 1");

        //---- infoButton ----
        infoButton.setText("Info");
        infoButton.addActionListener(e -> infoButtonActionPerformed(e));
        contentPane.add(infoButton, "cell 1 1");

        //---- applyButton ----
        applyButton.setText("Apply");
        applyButton.addActionListener(e -> applyButtonActionPerformed(e));
        contentPane.add(applyButton, "cell 2 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JCheckboxTree editorTree;
    private JButton optionsButton;
    private JButton infoButton;
    private JButton applyButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private boolean contains(String[] arr, String str)
    {
        for(String s : arr)
        {
            if(s.toLowerCase().contains(str.toLowerCase()))
                return true;
        }
        return false;
    }

    private static void clearDirs(File folder)
    {
        try
        {
            FileUtils.deleteDirectory(folder);
        }
        catch(IOException e)
        {
            System.err.println("\tFailed to delete folder: " + folder.getAbsolutePath());
            e.printStackTrace();
        }

        if(folder.exists())
        {
            System.err.println("\tFolder wasn't deleted?");
        }
    }

    public static void makeVolatile(File folder)
    {
        folder.deleteOnExit();
        for(File f : Objects.requireNonNull(folder.listFiles()))
        {
            if(f.isDirectory())
                makeVolatile(f);
            else
                f.deleteOnExit();
        }
    }
}
