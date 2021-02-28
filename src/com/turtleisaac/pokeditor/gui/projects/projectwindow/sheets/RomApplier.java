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
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalEditor;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalReturnGen4;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerEditorGen4;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerReturnGen4;
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
 * This class is for applying rom data to sheets
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
                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "personal");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal"));
                        api.updateSheet("Personal",personalReturn.getPersonalData());
                    }

                    if(contains(selected, "TM Learnsets"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "personal");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "personal"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl"));
                        api.updateSheet("Level-Up Learnsets",editor.learnsetToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(project, dataPath, baseRom);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo"));
                        api.updateSheet("Evolutions",editor.evolutionsToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "evo",false,false));
                    }

                    if(contains(selected,"Tutor Move"))
                    {
                        JOptionPane.showMessageDialog(this,"Tutor Move editors not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        api.updateSheet("Baby Forms",editor.babyFormsToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(project,dataPath);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl.narc",dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl"));
                        api.updateSheet("Moves",editor.movesToSheet(File.separator + "poketool" + File.separator + "waza" + File.separator + "waza_tbl"));
                    }

                    if(contains(selected,"Items"))
                    {
                        ItemEditorGen4 editor= new ItemEditorGen4(dataPath,project);
                        narctowl.unpack(dataPath + File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "item_data.narc",dataPath + File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "item_data");
                        toDelete.add(new File(dataPath + File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "item_data"));
                        api.updateSheet("Items",editor.itemsToSheet(File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "item_data"));
                    }

                    if(contains(selected,"Encounters"))
                    {
                        SinnohEncounterEditor editor= new SinnohEncounterEditor(project, dataPath);
                        SinnohEncounterReturn encounterReturn;

                        if(baseRom == Game.Diamond) //Diamond
                        {
                            narctowl.unpack(dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "d_enc_data.narc",dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "d_enc_data");
                            encounterReturn= editor.encountersToSheet(File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "d_enc_data");
                            toDelete.add(new File(dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "d_enc_data"));
                        }
                        else //Pearl
                        {
                            narctowl.unpack(dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "p_enc_data.narc",dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "p_enc_data");
                            encounterReturn= editor.encountersToSheet(File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "p_enc_data");
                            toDelete.add(new File(dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "p_enc_data"));
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

                    if(contains(selected, "Trainer"))
                    {
                        TrainerEditorGen4 editor= new TrainerEditorGen4(project,dataPath,baseRom);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata.narc",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata");
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke.narc",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata"));
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke"));
                        TrainerReturnGen4 trainerReturn= editor.trainersToSheets(File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata",File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke");
                        if(contains(selected,"Data"))
                            api.updateSheet("Trainer Data",trainerReturn.getTrainerData());
                        if(contains(selected,"Pokemon"))
                            api.updateSheet("Trainer Pokemon",trainerReturn.getTrainerPokemon());
                    }
                    break;

                case Platinum:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal"));
                        api.updateSheet("Personal",personalReturn.getPersonalData());
                    }

                    if(contains(selected, "TM Learnsets"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "pl_personal"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl"));
                        api.updateSheet("Level-Up Learnsets",editor.learnsetToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "wotbl"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(project, dataPath, baseRom);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo.narc",dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "personal" + File.separator + "evo"));
                        api.updateSheet("Evolutions",editor.evolutionsToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "evo",false, false));
                    }

                    if(contains(selected,"Tutor Move"))
                    {
                        JOptionPane.showMessageDialog(this,"Tutor Move editors not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        api.updateSheet("Baby Forms",editor.babyFormsToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(project,dataPath);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl.narc",dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl"));
                        api.updateSheet("Moves",editor.movesToSheet(File.separator + "poketool" + File.separator + "waza" + File.separator + "pl_waza_tbl"));
                    }

                    if(contains(selected,"Items"))
                    {
                        ItemEditorGen4 editor= new ItemEditorGen4(dataPath,project);
                        narctowl.unpack(dataPath + File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "pl_item_data.narc",dataPath + File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "pl_item_data");
                        toDelete.add(new File(dataPath + File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "pl_item_data"));
                        api.updateSheet("Items",editor.itemsToSheet(File.separator + "itemtool" + File.separator + "itemdata" + File.separator + "pl_item_data"));
                    }

                    if(contains(selected,"Encounters"))
                    {
                        SinnohEncounterEditor editor= new SinnohEncounterEditor(project,dataPath);
                        narctowl.unpack(dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "pl_enc_data.narc",dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "pl_enc_data");
                        SinnohEncounterReturn encounterReturn= editor.encountersToSheet(File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "pl_enc_data");
                        toDelete.add(new File(dataPath + File.separator + "fielddata" + File.separator + "encountdata" + File.separator + "pl_enc_data"));

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

                    if(contains(selected, "Trainer"))
                    {
                        TrainerEditorGen4 editor= new TrainerEditorGen4(project,dataPath,baseRom);
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata.narc",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata");
                        narctowl.unpack(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke.narc",dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke");
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata"));
                        toDelete.add(new File(dataPath + File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke"));
                        TrainerReturnGen4 trainerReturn= editor.trainersToSheets(File.separator + "poketool" + File.separator + "trainer" + File.separator + "trdata",File.separator + "poketool" + File.separator + "trainer" + File.separator + "trpoke");
                        if(contains(selected,"Data"))
                            api.updateSheet("Trainer Data",trainerReturn.getTrainerData());
                        if(contains(selected,"Pokemon"))
                            api.updateSheet("Trainer Pokemon",trainerReturn.getTrainerPokemon());
                    }
                    break;

                case HeartGold:
                case SoulSilver:
                    if(contains(selected, "Personal"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet(File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "TM Learnsets"))
                    {
                        PersonalEditor editor= new PersonalEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_");
                        PersonalReturnGen4 personalReturn= editor.personalToSheet(File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "2_"));
                        api.updateSheet("TM Learnsets",personalReturn.getTMData());
                    }

                    if(contains(selected, "Level-Up Learnsets"))
                    {
                        LearnsetEditor editor= new LearnsetEditor(dataPath, project);
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3_");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3_"));
                        api.updateSheet("Level-Up Learnsets",editor.learnsetToSheet(File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "3_"));
                    }

                    if(contains(selected,"Evolutions"))
                    {
                        EvolutionEditor editor= new EvolutionEditor(project, dataPath, baseRom);
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_"));
                        api.updateSheet("Evolutions",editor.evolutionsToSheet(File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_",false, false));
                    }

                    if(contains(selected,"Baby Forms"))
                    {
                        BabyFormEditor editor= new BabyFormEditor(dataPath);
                        api.updateSheet("Baby Forms",editor.babyFormsToSheet(File.separator + "poketool" + File.separator + "personal" + File.separator + "pms.narc"));
                    }

                    if(contains(selected,"Moves"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(project,dataPath);
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "1",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "1_");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "1_"));
                        api.updateSheet("Moves",editor.movesToSheet(File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "1_"));
                    }

                    if(contains(selected,"Items"))
                    {
                        MoveEditorGen4 editor= new MoveEditorGen4(project,dataPath);
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "7",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "7_");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "7_"));
                        api.updateSheet("Items",editor.movesToSheet(File.separator + "a" + File.separator + "0" + File.separator + "1" + File.separator + "7_"));
                    }

                    if(contains(selected,"Encounters"))
                    {
                        JohtoEncounterEditor editor= new JohtoEncounterEditor(project, dataPath);
                        JohtoEncounterReturn encounterReturn;

                        if(baseRom == Game.HeartGold) //HG
                        {
                            narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7_");
                            encounterReturn= editor.encountersToSheet(File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7_");
                            toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7_"));

                        }
                        else //SS
                        {
                            narctowl.unpack(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6",dataPath + File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6_");
                            encounterReturn= editor.encountersToSheet(File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6_");
                            toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6_"));
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

                    if(contains(selected, "Trainer"))
                    {
                        TrainerEditorGen4 editor= new TrainerEditorGen4(project,dataPath,baseRom);
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "5",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "5_");
                        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "6",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "6_");
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "5_"));
                        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "6_"));
                        TrainerReturnGen4 trainerReturn= editor.trainersToSheets(File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "5_",File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "6_");
                        if(contains(selected,"Data"))
                            api.updateSheet("Trainer Data",trainerReturn.getTrainerData());
                        if(contains(selected,"Pokemon"))
                            api.updateSheet("Trainer Pokemon",trainerReturn.getTrainerPokemon());
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
