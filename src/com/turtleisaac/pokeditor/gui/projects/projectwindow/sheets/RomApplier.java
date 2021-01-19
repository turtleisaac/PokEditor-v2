/*
 * Created by JFormDesigner on Mon Jan 04 12:19:53 EST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets;

import java.awt.event.*;
import com.turtleisaac.pokeditor.editors.babies.BabyFormEditor;
import com.turtleisaac.pokeditor.editors.encounters.johto.JohtoEncounterEditor;
import com.turtleisaac.pokeditor.editors.encounters.johto.JohtoEncounterReturn;
import com.turtleisaac.pokeditor.editors.encounters.sinnoh.SinnohEncounterEditor;
import com.turtleisaac.pokeditor.editors.encounters.sinnoh.SinnohEncounterReturn;
import com.turtleisaac.pokeditor.editors.evolutions.gen4.EvolutionEditor;
import com.turtleisaac.pokeditor.editors.items.ItemEditorGen4;
import com.turtleisaac.pokeditor.editors.learnsets.LearnsetEditor;
import com.turtleisaac.pokeditor.editors.moves.gen4.MoveEditorGen4;
import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalEditor;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalReturnGen4;
import com.turtleisaac.pokeditor.gui.JCheckboxTree;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import net.miginfocom.swing.MigLayout;
import turtleisaac.GoogleSheetsAPI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author turtleisaac
 */
public class RomApplier extends JFrame
{
    private Project project;
    private Game baseRom;
    private String[] editors;
    private String projectPath;

    private GoogleSheetsAPI api;
    private JFrame parent;

    public RomApplier(Project project, String projectPath, GoogleSheetsAPI api, JFrame parent)
    {
        initComponents();
        DefaultMutableTreeNode root= new DefaultMutableTreeNode("Sheets");
        this.project= project;
        baseRom= project.getBaseRom();
        editors= baseRom.editorList;
        this.projectPath = projectPath;
        this.api= api;
        this.parent= parent;

        for(String str : baseRom.sheetList)
        {
            DefaultMutableTreeNode child= new DefaultMutableTreeNode(str);
            root.add(child);
        }


        editorTree= new JCheckboxTree(root);
        editorTree.setVisible(true);
        setPreferredSize(new Dimension(300,350));
        scrollPane1.setViewportView(editorTree);

        setVisible(true);
        toFront();
        pack();
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
            String dataPath= projectPath + File.separator + project.getName() + "/data";


            switch (baseRom)
            {
                case Diamond:
                case Pearl:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/personal.narc",dataPath + "/poketool/personal/personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet("/poketool/personal/personal");
                        toDelete.add(new File(dataPath + "/poketool/personal/personal"));
                        api.updateSheet("Personal",personalReturn.getPersonalData());
                    }

                    if(contains(selected, "TM Learnsets"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/personal.narc",dataPath + "/poketool/personal/personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet("/poketool/personal/personal");
                        toDelete.add(new File(dataPath + "/poketool/personal/personal"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/wotbl.narc",dataPath + "/poketool/personal/wotbl");
                        toDelete.add(new File(dataPath + "/poketool/personal/wotbl"));
                        api.updateSheet("Level-Up Learnsets",editor.learnsetToSheet("/poketool/personal/wotbl"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/evo.narc",dataPath + "/poketool/personal/evo");
                        toDelete.add(new File(dataPath + "/poketool/personal/evo"));
                        api.updateSheet("Evolutions",editor.evolutionsToSheet("/poketool/personal/evo",false));
                    }

                    if(contains(selected,"Tutor Move"))
                    {
                        JOptionPane.showMessageDialog(this,"Tutor Move editors not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        api.updateSheet("Baby Forms",editor.babyFormsToSheet("/poketool/personal/pms.narc"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(dataPath);
                        narctowl.unpack(dataPath + "/poketool/waza/waza_tbl.narc",dataPath + "/poketool/waza/waza_tbl");
                        toDelete.add(new File(dataPath + "/poketool/waza/waza_tbl"));
                        api.updateSheet("Moves",editor.movesToSheet("/poketool/waza/waza_tbl"));
                    }

                    if(contains(selected,"Items"))
                    {
                        ItemEditorGen4 editor= new ItemEditorGen4(dataPath,baseRom);
                        narctowl.unpack(dataPath + "/itemtool/itemdata/item_data.narc",dataPath + "/itemtool/itemdata/item_data");
                        toDelete.add(new File(dataPath + "/itemtool/itemdata/item_data"));
                        api.updateSheet("Items",editor.itemsToSheet("/itemtool/itemdata/item_data"));
                    }

                    if(contains(selected,"Encounters"))
                    {
                        SinnohEncounterEditor editor= new SinnohEncounterEditor(dataPath);
                        SinnohEncounterReturn encounterReturn;

                        if(baseRom == Game.Diamond) //Diamond
                        {
                            narctowl.unpack(dataPath + "/fielddata/encountdata/d_enc_data.narc",dataPath + "/fielddata/encountdata/d_enc_data");
                            encounterReturn= editor.encountersToSheet("/fielddata/encountdata/d_enc_data");
                            toDelete.add(new File(dataPath + "/fielddata/encountdata/d_enc_data"));
                        }
                        else //Pearl
                        {
                            narctowl.unpack(dataPath + "/fielddata/encountdata/p_enc_data.narc",dataPath + "/fielddata/encountdata/p_enc_data");
                            encounterReturn= editor.encountersToSheet("/fielddata/encountdata/p_enc_data");
                            toDelete.add(new File(dataPath + "/fielddata/encountdata/p_enc_data"));
                        }

                        if(contains(selected,"Field"))
                            api.updateSheet("Field Encounters",encounterReturn.getField());
                        if(contains(selected,"Water"))
                            api.updateSheet("Water Encounters",encounterReturn.getWater());
                        if(contains(selected,"Swarm"))
                            api.updateSheet("Swarm/ Day/ Night Encounters",encounterReturn.getSwarm());
                        if(contains(selected,"Radar"))
                            api.updateSheet("Poke Radar Encounters",encounterReturn.getRadar());
                        if(contains(selected,"Mode"))
                            api.updateSheet("Dual-Slot Mode Encounters",encounterReturn.getDualSlot());
                    }
                    break;

                case Platinum:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/pl_personal.narc",dataPath + "/poketool/personal/pl_personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet("/poketool/personal/pl_personal");
                        toDelete.add(new File(dataPath + "/poketool/personal/pl_personal"));
                        api.updateSheet("Personal",personalReturn.getPersonalData());
                    }

                    if(contains(selected, "TM Learnsets"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/pl_personal.narc",dataPath + "/poketool/personal/pl_personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet("/poketool/personal/pl_personal");
                        toDelete.add(new File(dataPath + "/poketool/personal/pl_personal"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/wotbl.narc",dataPath + "/poketool/personal/wotbl");
                        toDelete.add(new File(dataPath + "/poketool/personal/wotbl"));
                        api.updateSheet("Level-Up Learnsets",editor.learnsetToSheet("/poketool/personal/wotbl"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/poketool/personal/evo.narc",dataPath + "/poketool/personal/evo");
                        toDelete.add(new File(dataPath + "/poketool/personal/evo"));
                        api.updateSheet("Evolutions",editor.evolutionsToSheet("/poketool/personal/evo",false));
                    }

                    if(contains(selected,"Tutor Move"))
                    {
                        JOptionPane.showMessageDialog(this,"Tutor Move editors not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        api.updateSheet("Baby Forms",editor.babyFormsToSheet("/poketool/personal/pms.narc"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(dataPath);
                        narctowl.unpack(dataPath + "/poketool/waza/pl_waza_tbl.narc",dataPath + "/poketool/waza/pl_waza_tbl");
                        toDelete.add(new File(dataPath + "/poketool/waza/pl_waza_tbl"));
                        api.updateSheet("Moves",editor.movesToSheet("/poketool/waza/pl_waza_tbl"));
                    }

                    if(contains(selected,"Items"))
                    {
                        ItemEditorGen4 editor= new ItemEditorGen4(dataPath,baseRom);
                        narctowl.unpack(dataPath + "/itemtool/itemdata/pl_item_data.narc",dataPath + "/itemtool/itemdata/pl_item_data");
                        toDelete.add(new File(dataPath + "/itemtool/itemdata/pl_item_data"));
                        api.updateSheet("Items",editor.itemsToSheet("/itemtool/itemdata/pl_item_data"));
                    }

                    if(contains(selected,"Encounters"))
                    {
                        SinnohEncounterEditor editor= new SinnohEncounterEditor(dataPath);
                        narctowl.unpack(dataPath + "/fielddata/encountdata/pl_enc_data.narc",dataPath + "/fielddata/encountdata/pl_enc_data");
                        SinnohEncounterReturn encounterReturn= editor.encountersToSheet("/fielddata/encountdata/pl_enc_data");
                        toDelete.add(new File(dataPath + "/fielddata/encountdata/pl_enc_data"));

                        if(contains(selected,"Field"))
                            api.updateSheet("Field Encounters",encounterReturn.getField());
                        if(contains(selected,"Water"))
                            api.updateSheet("Water Encounters",encounterReturn.getWater());
                        if(contains(selected,"Swarm"))
                            api.updateSheet("Swarm/ Day/ Night Encounters",encounterReturn.getSwarm());
                        if(contains(selected,"Radar"))
                            api.updateSheet("Poke Radar Encounters",encounterReturn.getRadar());
                        if(contains(selected,"Mode"))
                            api.updateSheet("Dual-Slot Mode Encounters",encounterReturn.getDualSlot());
                    }
                    break;

                case HeartGold:
                case SoulSilver:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/a/0/0/2",dataPath + "/a/0/0/2_");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet("/a/0/0/2_");
                        toDelete.add(new File(dataPath + "/a/0/0/2_"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "TM Learnsets"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/a/0/0/2",dataPath + "/a/0/0/2_");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet("/a/0/0/2_");
                        toDelete.add(new File(dataPath + "/a/0/0/2_"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/a/0/3/3",dataPath + "/a/0/3/3_");
                        toDelete.add(new File(dataPath + "/a/0/3/3_"));
                        api.updateSheet("Level-Up Learnsets",editor.learnsetToSheet("/a/0/3/3_"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(dataPath, baseRom);
                        narctowl.unpack(dataPath + "/a/0/3/4",dataPath + "/a/0/3/4_");
                        toDelete.add(new File(dataPath + "/a/0/3/4_"));
                        api.updateSheet("Evolutions",editor.evolutionsToSheet("/a/0/3/4_",false));
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        api.updateSheet("Baby Forms",editor.babyFormsToSheet("/poketool/personal/pms.narc"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(dataPath);
                        narctowl.unpack(dataPath + "/a/0/1/1",dataPath + "/a/0/1/1_");
                        toDelete.add(new File(dataPath + "/a/0/1/1_"));
                        api.updateSheet("Moves",editor.movesToSheet("/a/0/1/1_"));
                    }

                    if(contains(selected,"Items"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(dataPath);
                        narctowl.unpack(dataPath + "/a/0/1/7",dataPath + "/a/0/1/7_");
                        toDelete.add(new File(dataPath + "/a/0/1/7_"));
                        api.updateSheet("Items",editor.movesToSheet("/a/0/1/7_"));
                    }

                    if(contains(selected,"Encounters"))
                    {
                        JohtoEncounterEditor editor= new JohtoEncounterEditor(dataPath);
                        JohtoEncounterReturn encounterReturn;

                        if(baseRom == Game.HeartGold) //HG
                        {
                            narctowl.unpack(dataPath + "/a/0/3/7",dataPath + "/a/0/3/7_");
                            encounterReturn= editor.encountersToSheet("/a/0/3/7_");
                            toDelete.add(new File(dataPath + "/a/0/3/7_"));

                        }
                        else //SS
                        {
                            narctowl.unpack(dataPath + "/a/1/3/6",dataPath + "/a/1/3/6_");
                            encounterReturn= editor.encountersToSheet("/a/1/3/6_");
                            toDelete.add(new File(dataPath + "/a/1/3/6_"));
                        }

                        if(contains(selected,"Field"))
                            api.updateSheet("Field Encounters",encounterReturn.getField());
                        if(contains(selected,"Water"))
                            api.updateSheet("Water Encounters",encounterReturn.getWater());
                        if(contains(selected,"Rock Smash"))
                            api.updateSheet("Rock Smash Encounters",encounterReturn.getSmash());
                        if(contains(selected,"Mass-Outbreak"))
                            api.updateSheet("Mass-Outbreak Encounters",encounterReturn.getOutbreak());
                        if(contains(selected,"Sound"))
                            api.updateSheet("Sound Encounters",encounterReturn.getSound());
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
        JOptionPane.showMessageDialog(this,"This feature lets you take data from your ROM and upload it to your Google Sheets document. If you have previously made edits to your ROM using another tool, this is how you can make those edits appear in the Google Sheet. Uploading any data will overwrite the existing data in the sheet, so be careful.","Info",JOptionPane.INFORMATION_MESSAGE);
    }

    private void thisWindowClosing(WindowEvent e)
    {
        parent.toFront();
        parent.setEnabled(true);
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        editorTree = new JCheckboxTree();
        optionsButton = new JButton();
        infoButton = new JButton();
        applyButton = new JButton();

        //======== this ========
        setTitle("Apply ROM to Sheet");
        setMinimumSize(new Dimension(50, 50));
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
