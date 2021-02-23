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

import com.turtleisaac.pokeditor.editors.babies.BabyFormEditor;
import com.turtleisaac.pokeditor.editors.encounters.johto.JohtoEncounterEditor;
import com.turtleisaac.pokeditor.editors.evolutions.gen4.EvolutionEditor;
import com.turtleisaac.pokeditor.editors.learnsets.LearnsetEditor;
import com.turtleisaac.pokeditor.editors.moves.gen4.MoveEditorGen4;
import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalEditor;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerEditorGen4;
import com.turtleisaac.pokeditor.gui.*;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.ProjectWindow;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import net.miginfocom.swing.*;
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

    public SheetApplier(Project project, String projectPath, GoogleSheetsAPI api, JFrame parent)
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
            Narctowl narctowl= new Narctowl(true);
            String dataPath= projectPath + File.separator + project.getName() + File.separator + "data";


            switch (baseRom)
            {
                case Diamond:
                case Pearl:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        editor.csvToPersonal(api.getSpecifiedSheetArr("Personal"),api.getSpecifiedSheetArr("TM Learnsets"),File.separator + "poketool" + File.separator + "personal" + File.separator + "personal");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal","",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal"));
                    }

                    if(contains(selected, "Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, baseRom);
                        editor.sheetToLearnsets(api.getSpecifiedSheetArr("Level-Up Learnsets"),File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl","",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(dataPath, baseRom);
                        editor.sheetToEvolutions(api.getSpecifiedSheetArr("Evolutions"),File.separator + "poketool" + File.separator + "personal" + File.separator + "evo");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo","",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo"));
                    }

//                    if(contains(selected,"Tutor Move"))
//                    {
//                        JOptionPane.showMessageDialog(this,"Tutor Move editors not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
//                    }
//
                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        editor.sheetToBabyForms(api.getSpecifiedSheetArr("Baby Forms"),File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc");
                    }

                    if(contains(selected, "Trainers"))
                    {
                        TrainerEditorGen4 editor= new TrainerEditorGen4(dataPath,baseRom);
                        editor.sheetsToTrainers(api.getSpecifiedSheetArr("Trainer Data"),api.getSpecifiedSheetArr("Trainer Pokemon"),File.separator + "poketool" + File.separator + "trainer" + File.separator + "");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata","",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata.narc");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke","",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata"));
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(dataPath);
                        editor.sheetToMoves(api.getSpecifiedSheetArr("Moves"),File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl","",dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl"));
                    }
//
//                    if(contains(selected,"Encounters"))
//                    {
//                        SinnohEncounterEditor editor= new SinnohEncounterEditor(dataPath);
//                        SinnohEncounterReturn encounterReturn;
//
//                        if(baseRom == Game.Diamond) //Diamond
//                        {
//                            narctowl.unpack(dataPath + "/fielddata/encountdata/d_enc_data.narc",dataPath + "/fielddata/encountdata/d_enc_data");
//                            encounterReturn= editor.encountersToSheet("/fielddata/encountdata/d_enc_data");
//                            toDelete.add(new File(dataPath + "/fielddata/encountdata/d_enc_data"));
//                        }
//                        else //Pearl
//                        {
//                            narctowl.unpack(dataPath + "/fielddata/encountdata/p_enc_data.narc",dataPath + "/fielddata/encountdata/p_enc_data");
//                            encounterReturn= editor.encountersToSheet("/fielddata/encountdata/p_enc_data");
//                            toDelete.add(new File(dataPath + "/fielddata/encountdata/p_enc_data"));
//                        }
//
//                        if(contains(selected,"Field"))
//                            api.updateSheet("Field Encounters",encounterReturn.getField());
//                        if(contains(selected,"Water"))
//                            api.updateSheet("Water Encounters",encounterReturn.getWater());
//                        if(contains(selected,"Swarm"))
//                            api.updateSheet("Swarm/ Day/ Night Encounters",encounterReturn.getSwarm());
//                        if(contains(selected,"Radar"))
//                            api.updateSheet("Poke Radar Encounters",encounterReturn.getRadar());
//                        if(contains(selected,"Mode"))
//                            api.updateSheet("Dual-Slot Mode Encounters",encounterReturn.getDualSlot());
//                    }
                    break;

                case Platinum:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        editor.csvToPersonal(api.getSpecifiedSheetArr("Personal"),api.getSpecifiedSheetArr("TM Learnsets"),File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal","",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal"));
                    }

                    if(contains(selected, "Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, baseRom);
                        editor.sheetToLearnsets(api.getSpecifiedSheetArr("Level-Up Learnsets"),File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl","",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl.narc");
//                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(dataPath, baseRom);
                        editor.sheetToEvolutions(api.getSpecifiedSheetArr("Evolutions"),File.separator + "poketool" + File.separator + "personal" + File.separator + "evo");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo","",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo"));
                    }
//
//                    if(contains(selected,"Tutor Move"))
//                    {
//                        JOptionPane.showMessageDialog(this,"Tutor Move editors not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
//                    }
//
                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        editor.sheetToBabyForms(api.getSpecifiedSheetArr("Baby Forms"),File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc");
                    }

                    if(contains(selected, "Trainers"))
                    {
                        TrainerEditorGen4 editor= new TrainerEditorGen4(dataPath,baseRom);
                        editor.sheetsToTrainers(api.getSpecifiedSheetArr("Trainer Data"),api.getSpecifiedSheetArr("Trainer Pokemon"),File.separator + "poketool" + File.separator + "trainer" + File.separator + "");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata","",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata.narc");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke","",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata"));
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(dataPath);
                        editor.sheetToMoves(api.getSpecifiedSheetArr("Moves"),File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl");
                        narctowl.pack(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl","",dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl.narc");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl"));
                    }
//
//                    Items
//
//                    if(contains(selected,"Encounters"))
//                    {
//                        SinnohEncounterEditor editor= new SinnohEncounterEditor(dataPath);
//                        narctowl.unpack(dataPath + "/fielddata/encountdata/pl_enc_data.narc",dataPath + "/fielddata/encountdata/pl_enc_data");
//                        SinnohEncounterReturn encounterReturn= editor.encountersToSheet("/fielddata/encountdata/pl_enc_data");
//                        toDelete.add(new File(dataPath + "/fielddata/encountdata/pl_enc_data"));
//
//                        if(contains(selected,"Field"))
//                            api.updateSheet("Field Encounters",encounterReturn.getField());
//                        if(contains(selected,"Water"))
//                            api.updateSheet("Water Encounters",encounterReturn.getWater());
//                        if(contains(selected,"Swarm"))
//                            api.updateSheet("Swarm/ Day/ Night Encounters",encounterReturn.getSwarm());
//                        if(contains(selected,"Radar"))
//                            api.updateSheet("Poke Radar Encounters",encounterReturn.getRadar());
//                        if(contains(selected,"Mode"))
//                            api.updateSheet("Dual-Slot Mode Encounters",encounterReturn.getDualSlot());
//                    }
                    break;

                case HeartGold:
                case SoulSilver:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        editor.csvToPersonal(api.getSpecifiedSheetArr("Personal"),api.getSpecifiedSheetArr("TM Learnsets"),File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_");
                        narctowl.pack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_","",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_"));
                    }

                    if(contains(selected, "Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, baseRom);
                        editor.sheetToLearnsets(api.getSpecifiedSheetArr("Level-Up Learnsets"),File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3_");
                        narctowl.pack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3_","",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3_"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(dataPath, baseRom);
                        editor.sheetToEvolutions(api.getSpecifiedSheetArr("Evolutions"),File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_");
                        narctowl.pack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_","",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_"));
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        editor.sheetToBabyForms(api.getSpecifiedSheetArr("Baby Forms"),File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc");
                    }

//                    if(contains(selected,"Moves"))
//                    {
//                        MoveEditorGen4 editor= new MoveEditorGen4(dataPath);
//                        narctowl.unpack(dataPath + "/a/0/1/1",dataPath + "/a/0/1/1");
//                        toDelete.add(new File(dataPath + "/a/0/1/1"));
//                        api.updateSheet("Moves",editor.movesToSheet("/a/0/1/1"));
//                    }
//
                    if(contains(selected,"Encounters"))
                    {
                        JohtoEncounterEditor editor= new JohtoEncounterEditor(dataPath);
                        Object[][] field= api.getSpecifiedSheetArr("Field Encounters");
                        Object[][] water= api.getSpecifiedSheetArr("Water Encounters");
                        Object[][] smash= api.getSpecifiedSheetArr("Rock Smash Encounters");
                        Object[][] outbreak= api.getSpecifiedSheetArr("Mass-Outbreak Encounters");
                        Object[][] sound= api.getSpecifiedSheetArr("Sound Encounters");


                        if(baseRom == Game.HeartGold) //HG
                        {
                            editor.sheetsToEncounters(field,water,smash,outbreak,sound,File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7_");
                            narctowl.pack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7_","",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7");
                            toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7_"));

                        }
                        else //SS
                        {
                            editor.sheetsToEncounters(field,water,smash,outbreak,sound,File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6_");
                            narctowl.pack(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6_","",dataPath + File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6");
                            toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6_"));
                        }
                    }
                    break;

                case Black:
                case White:

                    break;

                case Black2:
                case White2:

                    break;
            }


        }
        catch (IOException exception)
        {
            exception.printStackTrace();
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
            parent.toFront();
            parent.setEnabled(true);
            dispose();
        }
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

    public static void clearDirs(File folder)
    {
        for(File f : Objects.requireNonNull(folder.listFiles()))
        {
            if(f.isDirectory())
                clearDirs(f);
            else
                f.delete();
        }
        folder.delete();
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
