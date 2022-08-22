/*
 * Created by JFormDesigner on Thu Jan 21 16:34:37 EST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.trainers;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.turtleisaac.pokeditor.editors.trainers.gen4.*;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.BitStream;
import com.turtleisaac.pokeditor.gui.EditorComboBox;
import com.turtleisaac.pokeditor.gui.ComboBoxItem;
import com.turtleisaac.pokeditor.gui.editors.trainers.text.TrainerTextFrame;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.ProjectWindow;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.TableLocator;
import com.turtleisaac.pokeditor.utilities.TablePointers;
import com.turtleisaac.pokeditor.utilities.images.ImageBase;
import net.miginfocom.swing.*;
import turtleisaac.GoogleSheetsAPI;

/**
 * @author turtleisaac
 */
public class TrainerPanel extends JPanel
{
    private TrainerDataGen4 trainer;
    private ArrayList<TrainerPokemonData> pokemonList;
    private int numberPokemonLastSelected;
    private int trainerFileLastSelected;
    private boolean saved= true;

    private GoogleSheetsAPI api;
    private Object[][] trainerDataTable;
    private Object[][] trainerPokemonTable;
    private TrainerEditorGen4 trainerEditor;

    private Project project;
    private String projectPath;
    private String[] nameData;
    private String[] itemData;
    private String[] moveData;
    private static String[] trainerClassData;
    private static int[] formNumberData;

    private static TableLocator tableLocator;
    private byte[] trainerClassGenderTable;

    private final Animator animator;
    public ArrayList<File> toDelete= new ArrayList<>();

    private ArrayList<TrainerText> trainerTexts;
    private TrainerTextEditor textEditor;

    private final ProjectWindow parent;

    public TrainerPanel(ProjectWindow parent)
    {
        initComponents();
        this.parent = parent;

        numberPokemonLastSelected= 0;
        trainerFileLastSelected= 0;

        trainerSelectionComboBox.setMaximumRowCount(10);

        tableLocator= null;

        animator= new Animator(trainerClassImageButton);
    }

//    public TrainerPanel(GoogleSheetsAPI api, Project project)
//    {
//        initComponents();
//
//        numberPokemonLastSelected= 0;
//        trainerFileLastSelected= 0;
//
//        tableLocator= null;
//
//        try
//        {
//            setProject(project);
//            setApi(api);
//        }
//        catch (IOException exception)
//        {
//            exception.printStackTrace();
//        }
//
//        animator= new Animator(trainerClassImageButton);
//    }

    public void setTrainer(TrainerDataGen4 trainer)
    {
        this.trainer = trainer;
    }

    public void setPokemonList()
    {
        trainerPokemonTabbedPane.removeAll();
        for(TrainerPokemonData pokemon : pokemonList)
        {
            trainerPokemonTabbedPane.addTab("Pokémon " + (trainerPokemonTabbedPane.getTabCount()+1), new TrainerPokemonPanel(this,pokemon,toggleMovesCheckbox.isSelected(),toggleHeldItemsCheckbox.isSelected()));
        }
    }

    private void trainerSelectionComboBoxActionPerformed(ActionEvent e)
    {
        if(trainerEditor == null)
        {
            try
            {
                trainerEditor= new TrainerEditorGen4(project,projectPath,project.getBaseRom());
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }

        if(trainerSelectionComboBox.hasFocus())
        {
//            if(!saved)
//            {
//                if(JOptionPane.showConfirmDialog(this,"Do you want to save your changes to this trainer?","PokEditor",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) == 0)
//                {
//                    saveTrainerButtonActionPerformed(e);
//                }
//            }

            int trainerFile= trainerSelectionComboBox.getSelectedIndex();
            try
            {
                trainer= trainerEditor.parseTrainerData(trainerDataTable[trainerFile+1]);
                loadTrainerData();
            }
            catch(NullPointerException exception)
            {
                exception.printStackTrace();
            }
            trainerFileLastSelected= trainerSelectionComboBox.getSelectedIndex();
        }
        else
        {
            saved= true;
        }
    }

    private void newTrainerButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        JOptionPane.showMessageDialog(this,"Not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void trainerClassSelectorComboBoxActionPerformed(ActionEvent e)
    {
        saved= false;
        int baseOffset= trainerClassSelectorComboBox.getSelectedIndex()*5;
        String dataPath= project.getProjectPath().getAbsolutePath() + File.separator + project.getName() + File.separator + "data";
        String unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;
        try
        {
            String narcPath= Project.isDPPT(project) ? File.separator + "poketool" + File.separator + "trgra" + File.separator + "trfgra.narc" : File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "8";
            String folderPath= unpackedFolderPath + "trfgra";
            new File(folderPath).deleteOnExit();
            toDelete.add(new File(folderPath));


            if(!new File(folderPath).exists())
            {
                Narctowl narctowl= new Narctowl(true);
                narctowl.unpack(dataPath + narcPath,folderPath);
            }

            ImageBase imageBase= new ImageBase(project,folderPath + File.separator + baseOffset + ".bin", folderPath + File.separator + (baseOffset + 1) + ".bin", folderPath + File.separator + (baseOffset + 2) + ".bin");

            animator.reset(imageBase.getImages(trainerClassImageButton.getBackground()));

        } catch (IOException exception)
        {
            exception.printStackTrace();
        }

//        System.out.println(trainerClassSelectorComboBox.getSelectedItem());
        try
        {
            getSelectedClassGender();
        }
        catch (NullPointerException ignored)
        {
        }

    }

    private void trainerClassImageButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        JOptionPane.showMessageDialog(this,"Trainer Class Detail Viewer not available","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void numberPokemonSliderStateChanged(ChangeEvent e)
    {
        saved= false;
        if(numberPokemonSlider.getValue() >= numberPokemonLastSelected)
        {
            while(trainerPokemonTabbedPane.getTabCount() != numberPokemonSlider.getValue())
            {
                trainerPokemonTabbedPane.addTab("Pokémon " + (trainerPokemonTabbedPane.getTabCount()+1), new TrainerPokemonPanel(this,null,toggleMovesCheckbox.isSelected(),toggleHeldItemsCheckbox.isSelected()));
                if(pokemonList != null)
                {
                    pokemonList.add(new TrainerPokemonData()
                    {
                        @Override
                        public short getIvs()
                        {
                            return 0;
                        }

                        @Override
                        public short getAbility()
                        {
                            return 0;
                        }

                        @Override
                        public int getLevel()
                        {
                            return 0;
                        }

                        @Override
                        public int getPokemon()
                        {
                            return 0;
                        }

                        @Override
                        public int getAltForm()
                        {
                            return 0;
                        }

                        @Override
                        public int getItem()
                        {
                            return 0;
                        }

                        @Override
                        public int getMove1()
                        {
                            return 0;
                        }

                        @Override
                        public int getMove2()
                        {
                            return 0;
                        }

                        @Override
                        public int getMove3()
                        {
                            return 0;
                        }

                        @Override
                        public int getMove4()
                        {
                            return 0;
                        }

                        @Override
                        public short getBallCapsule()
                        {
                            return 0;
                        }
                    });
                }
            }
        }
        else
        {
            while(trainerPokemonTabbedPane.getTabCount() != numberPokemonSlider.getValue() && trainerPokemonTabbedPane.getTabCount() > 0)
            {
                trainerPokemonTabbedPane.removeTabAt(trainerPokemonTabbedPane.getTabCount()-1);
                pokemonList.remove(trainerPokemonTabbedPane.getTabCount()-1);
            }
        }

        numberPokemonLastSelected = numberPokemonSlider.getValue();
    }

    private void toggleMovesCheckboxItemStateChanged(ItemEvent e)
    {
        saved= false;
        for(int i= 0; i < trainerPokemonTabbedPane.getTabCount(); i++)
        {
            TrainerPokemonPanel panel= (TrainerPokemonPanel) trainerPokemonTabbedPane.getComponentAt(i);

            try
            {
                panel.setMovesEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
            catch (NullPointerException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    private void toggleHeldItemsCheckboxItemStateChanged(ItemEvent e)
    {
        saved= false;
//        System.out.println("Tab count: " + trainerPokemonTabbedPane.getTabCount());
        for(int i= 0; i < trainerPokemonTabbedPane.getTabCount(); i++)
        {
            TrainerPokemonPanel panel= (TrainerPokemonPanel) trainerPokemonTabbedPane.getComponentAt(i);

            try
            {
                panel.setItemEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
            catch (NullPointerException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    private void saveTrainerButtonActionPerformed(ActionEvent e)
    {
        short unknown1= trainer.getUnknown1();
        short unknown2= trainer.getUnknown2();
        short unknown3= trainer.getUnknown3();
        short battleType= trainer.getBattleType();

        trainer= new TrainerDataGen4()
        {
            @Override
            public short getFlag()
            {
                BitStream bitStream= new BitStream();
                bitStream.append(toggleMovesCheckbox.isSelected());
                bitStream.append(toggleHeldItemsCheckbox.isSelected());
                return (short) Integer.parseInt(bitStream.toString(),2);
            }

            @Override
            public short getTrainerClass()
            {
                return (short) trainerClassSelectorComboBox.getSelectedIndex();
            }

            @Override
            public short getBattleType()
            {
                return battleType;
            }

            @Override
            public short getNumPokemon()
            {
                return (short) numberPokemonSlider.getValue();
            }

            @Override
            public int getItem1()
            {
                return itemComboBox1.getSelectedIndex();
            }

            @Override
            public int getItem2()
            {
                return itemComboBox2.getSelectedIndex();
            }

            @Override
            public int getItem3()
            {
                return itemComboBox3.getSelectedIndex();
            }

            @Override
            public int getItem4()
            {
                return itemComboBox4.getSelectedIndex();
            }

            @Override
            public long getAI()
            {
                BitStream bitStream= new BitStream();
                bitStream.append(effectivenessPriorityCheckbox.isSelected());
                bitStream.append(attackEvaluationCheckbox.isSelected());
                bitStream.append(expertModeCheckbox.isSelected());
                bitStream.append(statusPriorityCheckbox.isSelected());
                bitStream.append(riskyAttackCheckbox.isSelected());
                bitStream.append(damagePriorityCheckbox.isSelected());
                bitStream.append(partnerTrainerCheckbox.isSelected());
                bitStream.append(doubleBattleCheckbox.isSelected());
                bitStream.append(healingPriorityCheckbox.isSelected());
                bitStream.append(utilizeWeatherCheckbox.isSelected());
                bitStream.append(harassmentCheckbox.isSelected());
                bitStream.append(roamingCheckbox.isSelected());
                bitStream.append(safariZoneCheckbox.isSelected());
                bitStream.append(catchingDemoCheckbox.isSelected());

                return Long.parseLong(bitStream.toString(),2);
            }

            @Override
            public short getBattleType2() //Single or Double battle
            {
                return (short) (doubleBattleCheckbox.isSelected() ? 1 : 0);
            }

            @Override
            public short getUnknown1()
            {
                return unknown1;
            }

            @Override
            public short getUnknown2()
            {
                return unknown2;
            }

            @Override
            public short getUnknown3()
            {
                return unknown3;
            }
        };

        String[] trainerDataArr= new String[29];
        trainerDataArr[0]= "" + trainerFileLastSelected;
        trainerDataArr[1]= trainerNameTextField.getText();
        System.arraycopy(trainerEditor.createTrainerDataRow(trainer),0,trainerDataArr,2,27);

        String[] team= new String[68];
        team[0]= trainerDataArr[0];
        team[1]= trainerDataArr[1];

        for(int i= 0; i < trainerPokemonTabbedPane.getTabCount(); i++)
        {
            TrainerPokemonPanel panel= (TrainerPokemonPanel) trainerPokemonTabbedPane.getComponentAt(i);
            pokemonList.set(i,panel.getPokemonData());
        }

        System.arraycopy(trainerEditor.createTrainerTeamRow(pokemonList),0,team,2,66);

        trainerDataTable[trainerSelectionComboBox.getSelectedIndex()+1] = trainerDataArr;
        trainerPokemonTable[trainerSelectionComboBox.getSelectedIndex()+1] = team;

        try
        {
//            int offset = api.isLocal() ? 1 : 2;
            api.updateSheet("Trainer Data", trainerDataTable);
            api.updateSheet("Trainer Pokemon", trainerPokemonTable);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        saved= true;


        ComboBoxItem selectedItem= (ComboBoxItem) trainerSelectionComboBox.getSelectedItem();
        selectedItem.setName(trainerNameTextField.getText() + " (" + trainerSelectionComboBox.getSelectedIndex() + ")");

        try
        {
            trainerDataTable= api.getSpecifiedSheetArr("Trainer Data");
            trainerPokemonTable= api.getSpecifiedSheetArr("Trainer Pokemon");
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }

//        if(parent.shee)
    }

    private void exportSmogonButtonActionPerformed(ActionEvent e)
    {
        SmogonFrame smogonFrame= new SmogonFrame(this);
        for(int i= 0; i < trainerPokemonTabbedPane.getTabCount(); i++)
        {
            TrainerPokemonPanel pokemonPanel= (TrainerPokemonPanel) trainerPokemonTabbedPane.getComponentAt(i);
            smogonFrame.append(pokemonPanel.toString() + "\n\n");
        }
        smogonFrame.setLocationRelativeTo(this);
        smogonFrame.setPreferredSize(new Dimension(400,500));
        smogonFrame.setMinimumSize(smogonFrame.getMinimumSize());
        smogonFrame.pack();
        smogonFrame.setVisible(true);
    }

    public void importSmogonButtonActionPerformed(String[] arr)
    {
        // TODO finish import code
        ArrayList<String[]> smogonList= new ArrayList<>();
        int start= 0;
        for(int i= 1; i < arr.length; i++)
        {
            if(contains(nameData,arr[i]))
            {
                smogonList.add(Arrays.copyOfRange(arr,start,i));
                start= i;
            }
        }

//        for(String[] pokemon : smogonList)
//        {
//            System.out.println(Arrays.toString(pokemon));
//        }
    }

    private void refreshButtonActionPerformed(ActionEvent e)
    {
        int current= trainerSelectionComboBox.getSelectedIndex();

        try
        {
            setApi(api);
        } catch (IOException | NullPointerException exception)
        {
            exception.printStackTrace();
        }

        trainerSelectionComboBox.setSelectedIndex(current);
        trainerSelectionComboBoxActionPerformed(null);
    }

    private void trainerTextButtonActionPerformed(ActionEvent e)
    {
        // TODO uncomment code and restore functionality
        JOptionPane.showMessageDialog(this, "Not implemented yet", "Trainer Text Editor", JOptionPane.ERROR_MESSAGE);
//        try
//        {
//            if(Project.isHGSS(project))
//            {
//                TrainerTextFrame trainerTextFrame= new TrainerTextFrame(project, trainerSelectionComboBox.getSelectedIndex(), trainerSelectionComboBox.getSelectedItem().toString());
//                trainerTextFrame.setLocationRelativeTo(this);
//            }
//            else
//            {
//                JOptionPane.showMessageDialog(this,"Not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        catch(IOException exception)
//        {
//            exception.printStackTrace();
//        }


    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        trainerSelectionComboBox = new EditorComboBox();
        newTrainerButton = new JButton();
        saveTrainerButton = new JButton();
        refreshButton = new JButton();
        trainerDataPanel = new JPanel();
        exportSmogonButton = new JButton();
        trainerTextButton = new JButton();
        toggleMovesCheckbox = new JCheckBox();
        toggleHeldItemsCheckbox = new JCheckBox();
        toggleDoubleBattleCheckbox = new JCheckBox();
        trainerDataSubPanel = new JPanel();
        trainerNameLabel = new JLabel();
        itemPanel = new JPanel();
        itemComboBox1 = new EditorComboBox();
        itemComboBox2 = new EditorComboBox();
        itemComboBox3 = new EditorComboBox();
        itemComboBox4 = new EditorComboBox();
        trainerNameTextField = new JTextField();
        numberPokemonLabel = new JLabel();
        numberPokemonSlider = new JSlider();
        trainerClassPanel = new JPanel();
        trainerClassImageButton = new JButton();
        trainerClassSelectorComboBox = new EditorComboBox();
        trainerAiPanel = new JPanel();
        effectivenessPriorityCheckbox = new JCheckBox();
        attackEvaluationCheckbox = new JCheckBox();
        expertModeCheckbox = new JCheckBox();
        statusPriorityCheckbox = new JCheckBox();
        riskyAttackCheckbox = new JCheckBox();
        damagePriorityCheckbox = new JCheckBox();
        partnerTrainerCheckbox = new JCheckBox();
        doubleBattleCheckbox = new JCheckBox();
        healingPriorityCheckbox = new JCheckBox();
        utilizeWeatherCheckbox = new JCheckBox();
        harassmentCheckbox = new JCheckBox();
        roamingCheckbox = new JCheckBox();
        safariZoneCheckbox = new JCheckBox();
        catchingDemoCheckbox = new JCheckBox();
        trainerPokemonTabbedPane = new JTabbedPane();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[grow,center]"));

        //---- trainerSelectionComboBox ----
        trainerSelectionComboBox.addActionListener(e -> trainerSelectionComboBoxActionPerformed(e));
        add(trainerSelectionComboBox, "cell 0 0");

        //---- newTrainerButton ----
        newTrainerButton.setText("New Trainer");
        newTrainerButton.addActionListener(e -> newTrainerButtonActionPerformed(e));
        add(newTrainerButton, "cell 1 0,growx");

        //---- saveTrainerButton ----
        saveTrainerButton.setText("Save Trainer");
        saveTrainerButton.addActionListener(e -> saveTrainerButtonActionPerformed(e));
        add(saveTrainerButton, "cell 1 0,growx");

        //---- refreshButton ----
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(e -> refreshButtonActionPerformed(e));
        add(refreshButton, "cell 1 0");

        //======== trainerDataPanel ========
        {
            trainerDataPanel.setBorder(new TitledBorder("Trainer Data"));
            trainerDataPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[grow,fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[grow]" +
                "[]0"));

            //---- exportSmogonButton ----
            exportSmogonButton.setText("Export Pok\u00e9mon");
            exportSmogonButton.setToolTipText("Export or import team in Smogon format");
            exportSmogonButton.addActionListener(e -> exportSmogonButtonActionPerformed(e));
            trainerDataPanel.add(exportSmogonButton, "cell 0 0");

            //---- trainerTextButton ----
            trainerTextButton.setText("Trainer Text");
            trainerTextButton.addActionListener(e -> trainerTextButtonActionPerformed(e));
            trainerDataPanel.add(trainerTextButton, "cell 1 0");

            //---- toggleMovesCheckbox ----
            toggleMovesCheckbox.setText("Moves");
            toggleMovesCheckbox.addItemListener(e -> toggleMovesCheckboxItemStateChanged(e));
            trainerDataPanel.add(toggleMovesCheckbox, "cell 2 0");

            //---- toggleHeldItemsCheckbox ----
            toggleHeldItemsCheckbox.setText("Held Items");
            toggleHeldItemsCheckbox.addItemListener(e -> toggleHeldItemsCheckboxItemStateChanged(e));
            trainerDataPanel.add(toggleHeldItemsCheckbox, "cell 3 0");

            //---- toggleDoubleBattleCheckbox ----
            toggleDoubleBattleCheckbox.setText("Double Battle");
            trainerDataPanel.add(toggleDoubleBattleCheckbox, "cell 4 0");

            //======== trainerDataSubPanel ========
            {
                trainerDataSubPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[grow,fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- trainerNameLabel ----
                trainerNameLabel.setText("Name");
                trainerDataSubPanel.add(trainerNameLabel, "cell 0 0");

                //======== itemPanel ========
                {
                    itemPanel.setBorder(new TitledBorder("Items"));
                    itemPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[grow,fill]" +
                        "[grow,fill]",
                        // rows
                        "[grow]" +
                        "[grow]"));
                    itemPanel.add(itemComboBox1, "cell 0 0,grow");
                    itemPanel.add(itemComboBox2, "cell 1 0,grow");
                    itemPanel.add(itemComboBox3, "cell 0 1,grow");
                    itemPanel.add(itemComboBox4, "cell 1 1,grow");
                }
                trainerDataSubPanel.add(itemPanel, "cell 1 0 1 5,grow");
                trainerDataSubPanel.add(trainerNameTextField, "cell 0 1,growx");

                //---- numberPokemonLabel ----
                numberPokemonLabel.setText("Number of Pok\u00e9mon");
                trainerDataSubPanel.add(numberPokemonLabel, "cell 0 2,growx");

                //---- numberPokemonSlider ----
                numberPokemonSlider.setMaximum(6);
                numberPokemonSlider.setPaintLabels(true);
                numberPokemonSlider.setPaintTicks(true);
                numberPokemonSlider.setSnapToTicks(true);
                numberPokemonSlider.setMinimum(1);
                numberPokemonSlider.setToolTipText("Determines the number of pokemon on the trainer's team");
                numberPokemonSlider.setMajorTickSpacing(1);
                numberPokemonSlider.setValue(1);
                numberPokemonSlider.addChangeListener(e -> numberPokemonSliderStateChanged(e));
                trainerDataSubPanel.add(numberPokemonSlider, "cell 0 3");

                //======== trainerClassPanel ========
                {
                    trainerClassPanel.setBorder(new TitledBorder("Trainer Class"));
                    trainerClassPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[grow,fill]",
                        // rows
                        "[grow]" +
                        "[]"));

                    //---- trainerClassImageButton ----
                    trainerClassImageButton.setIcon(new ImageIcon(getClass().getResource("/icons/trainer_battle.png")));
                    trainerClassImageButton.addActionListener(e -> trainerClassImageButtonActionPerformed(e));
                    trainerClassPanel.add(trainerClassImageButton, "cell 0 0,grow");

                    //---- trainerClassSelectorComboBox ----
                    trainerClassSelectorComboBox.addActionListener(e -> trainerClassSelectorComboBoxActionPerformed(e));
                    trainerClassPanel.add(trainerClassSelectorComboBox, "cell 0 1");
                }
                trainerDataSubPanel.add(trainerClassPanel, "cell 0 5,grow");

                //======== trainerAiPanel ========
                {
                    trainerAiPanel.setBorder(new TitledBorder("Trainer AI"));
                    trainerAiPanel.setToolTipText("Dictates how the trainer will behave in battle");
                    trainerAiPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- effectivenessPriorityCheckbox ----
                    effectivenessPriorityCheckbox.setText("Prioritize effectiveness");
                    trainerAiPanel.add(effectivenessPriorityCheckbox, "cell 0 0");

                    //---- attackEvaluationCheckbox ----
                    attackEvaluationCheckbox.setText("Evaluate attacks");
                    trainerAiPanel.add(attackEvaluationCheckbox, "cell 1 0");

                    //---- expertModeCheckbox ----
                    expertModeCheckbox.setText("Expert");
                    trainerAiPanel.add(expertModeCheckbox, "cell 2 0");

                    //---- statusPriorityCheckbox ----
                    statusPriorityCheckbox.setText("Prioritize status");
                    trainerAiPanel.add(statusPriorityCheckbox, "cell 3 0");

                    //---- riskyAttackCheckbox ----
                    riskyAttackCheckbox.setText("Risky attacks");
                    trainerAiPanel.add(riskyAttackCheckbox, "cell 0 1");

                    //---- damagePriorityCheckbox ----
                    damagePriorityCheckbox.setText("Prioritize damage");
                    trainerAiPanel.add(damagePriorityCheckbox, "cell 1 1");

                    //---- partnerTrainerCheckbox ----
                    partnerTrainerCheckbox.setText("Partner");
                    trainerAiPanel.add(partnerTrainerCheckbox, "cell 2 1");

                    //---- doubleBattleCheckbox ----
                    doubleBattleCheckbox.setText("Double battle");
                    trainerAiPanel.add(doubleBattleCheckbox, "cell 3 1");

                    //---- healingPriorityCheckbox ----
                    healingPriorityCheckbox.setText("Prioritize healing");
                    trainerAiPanel.add(healingPriorityCheckbox, "cell 0 2");

                    //---- utilizeWeatherCheckbox ----
                    utilizeWeatherCheckbox.setText("Utilize weather");
                    trainerAiPanel.add(utilizeWeatherCheckbox, "cell 1 2");

                    //---- harassmentCheckbox ----
                    harassmentCheckbox.setText("Harassment");
                    trainerAiPanel.add(harassmentCheckbox, "cell 2 2");

                    //---- roamingCheckbox ----
                    roamingCheckbox.setText("Roaming Pok\u00e9mon");
                    trainerAiPanel.add(roamingCheckbox, "cell 3 2");

                    //---- safariZoneCheckbox ----
                    safariZoneCheckbox.setText("Safari Zone");
                    trainerAiPanel.add(safariZoneCheckbox, "cell 0 3");

                    //---- catchingDemoCheckbox ----
                    catchingDemoCheckbox.setText("Catching demo");
                    trainerAiPanel.add(catchingDemoCheckbox, "cell 1 3");
                }
                trainerDataSubPanel.add(trainerAiPanel, "cell 1 5,aligny center,grow 100 0");
            }
            trainerDataPanel.add(trainerDataSubPanel, "cell 0 1 5 1");
        }
        add(trainerDataPanel, "cell 0 1 2 1");

        //======== trainerPokemonTabbedPane ========
        {
            trainerPokemonTabbedPane.setBorder(new TitledBorder("Pok\u00e9mon"));
        }
        add(trainerPokemonTabbedPane, "cell 0 2 2 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private EditorComboBox trainerSelectionComboBox;
    private JButton newTrainerButton;
    private JButton saveTrainerButton;
    private JButton refreshButton;
    private JPanel trainerDataPanel;
    private JButton exportSmogonButton;
    private JButton trainerTextButton;
    private JCheckBox toggleMovesCheckbox;
    private JCheckBox toggleHeldItemsCheckbox;
    private JCheckBox toggleDoubleBattleCheckbox;
    private JPanel trainerDataSubPanel;
    private JLabel trainerNameLabel;
    private JPanel itemPanel;
    private EditorComboBox itemComboBox1;
    private EditorComboBox itemComboBox2;
    private EditorComboBox itemComboBox3;
    private EditorComboBox itemComboBox4;
    private JTextField trainerNameTextField;
    private JLabel numberPokemonLabel;
    private JSlider numberPokemonSlider;
    private JPanel trainerClassPanel;
    private JButton trainerClassImageButton;
    private EditorComboBox trainerClassSelectorComboBox;
    private JPanel trainerAiPanel;
    private JCheckBox effectivenessPriorityCheckbox;
    private JCheckBox attackEvaluationCheckbox;
    private JCheckBox expertModeCheckbox;
    private JCheckBox statusPriorityCheckbox;
    private JCheckBox riskyAttackCheckbox;
    private JCheckBox damagePriorityCheckbox;
    private JCheckBox partnerTrainerCheckbox;
    private JCheckBox doubleBattleCheckbox;
    private JCheckBox healingPriorityCheckbox;
    private JCheckBox utilizeWeatherCheckbox;
    private JCheckBox harassmentCheckbox;
    private JCheckBox roamingCheckbox;
    private JCheckBox safariZoneCheckbox;
    private JCheckBox catchingDemoCheckbox;
    private JTabbedPane trainerPokemonTabbedPane;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public int getSelectedClassIndex()
    {
        return trainerClassSelectorComboBox.getSelectedIndex();
    }

    public int getTrainerFileIndex()
    {
        return trainerSelectionComboBox.getSelectedIndex();
    }

    public boolean getSelectedClassGender()
    {
        int val= trainerClassGenderTable[trainerClassSelectorComboBox.getSelectedIndex()];
//        System.out.println("Trainer Class Gender Value: " + val);
        return val == 0 || val == 2;
    }

    public String[] getSpeciesList()
    {
        return nameData;
    }

    public String[] getItemList()
    {
        return itemData;
    }

    public String[] getMoveData()
    {
        return moveData;
    }

    public int[] getFormNumberData() {return formNumberData;}

    public void setProject(Project project) throws IOException
    {
//        System.out.println(projectPath);
        this.project= project;
        this.projectPath= project.getProjectPath().getAbsolutePath();
        tableLocator= new TableLocator(project);

        String resourcePath= projectPath + File.separator + "Program Files" + File.separator;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                moveData= TextEditor.getBank(project,588);
                itemData= TextEditor.getBank(project,344);
                trainerClassData= TextEditor.getBank(project,560);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                moveData= TextEditor.getBank(project,647);
                itemData= TextEditor.getBank(project,392);
                trainerClassData= TextEditor.getBank(project,619);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                moveData= TextEditor.getBank(project,750);
                itemData= TextEditor.getBank(project,222);
                trainerClassData= TextEditor.getBank(project,730);
                break;
        }

        for(String item : itemData)
        {
            itemComboBox1.addItem(new ComboBoxItem(item));
            itemComboBox2.addItem(new ComboBoxItem(item));
            itemComboBox3.addItem(new ComboBoxItem(item));
            itemComboBox4.addItem(new ComboBoxItem(item));
        }

        for(String trainerClass : trainerClassData)
        {
            trainerClassSelectorComboBox.addItem(new ComboBoxItem(trainerClass));
        }

        TrainerPokemonPanel newPanel= new TrainerPokemonPanel(this,null,toggleMovesCheckbox.isSelected(),toggleHeldItemsCheckbox.isSelected());
        newPanel.enableParentData();


        TablePointers.TablePointer classGenderPointer;
        boolean success= true;
        try
        {
            classGenderPointer= TablePointers.getPointers().get(project.getBaseRomGameCode()).get("classGender");
        }
        catch (Exception e)
        {
            success= false;
            classGenderPointer= null;

            e.printStackTrace();
        }

        if(success)
        {
            trainerClassGenderTable= tableLocator.obtainTableArr(classGenderPointer,trainerClassData.length,1);
        }


        trainerPokemonTabbedPane.addTab("Pokémon " + (trainerPokemonTabbedPane.getTabCount()+1), newPanel);
    }

    public String getProjectPath()
    {
        return projectPath;
    }

    public boolean isAbilityFunctional()
    {
        return Project.isHGSS(project);
    }

    public void setApi(GoogleSheetsAPI api) throws IOException
    {
        if(api != null)
        {
            this.api= api;

            trainerDataTable= api.getSpecifiedSheetArr("Trainer Data");
            trainerPokemonTable= api.getSpecifiedSheetArr("Trainer Pokemon");
            trainerSelectionComboBox.removeAllItems();

            int idx= 0;
            boolean first= true;
            trainerSelectionComboBox.removeAllItems();
            for(Object[] row : trainerDataTable)
            {
                if(!first)
                    trainerSelectionComboBox.addItem(new ComboBoxItem(row[1] + " (" + idx++ + ")"));
                else
                    first= false;
            }

            try
            {
                trainerEditor= new TrainerEditorGen4(project, projectPath, project.getBaseRom());
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }

            trainerClassSelectorComboBox.setSelectedIndex(1);

            //TODO restore this functionality upon merge with trainer_text
//            boolean success = true;
//            try
//            {
//                textEditor = new TrainerTextEditor(project);
//            }
//            catch(IOException e)
//            {
//                success = false;
//                e.printStackTrace();
//            }
//
//            if (success)
//            {
//                trainerTexts = textEditor.getTrainerTexts();
//            }
//            else
//            {
//                trainerTexts = null;
//                trainerTextButton.setEnabled(false);
//            }
        }
    }

    public void setSaved(boolean saved)
    {
        this.saved= saved;
    }

    private void loadTrainerData()
    {
        trainerNameTextField.setText((String) trainerDataTable[trainerSelectionComboBox.getSelectedIndex()+1][1]);
        numberPokemonSlider.setValue(trainer.getNumPokemon());
        itemComboBox1.setSelectedIndex(trainer.getItem1());
        itemComboBox2.setSelectedIndex(trainer.getItem2());
        itemComboBox3.setSelectedIndex(trainer.getItem3());
        itemComboBox4.setSelectedIndex(trainer.getItem4());
        toggleMovesCheckbox.setSelected((trainer.getFlag() & 1) == 1);
        toggleHeldItemsCheckbox.setSelected(((trainer.getFlag() >> 1) & 1) == 1);
        toggleDoubleBattleCheckbox.setSelected(trainer.getBattleType2() == 2);
        trainerClassSelectorComboBox.setSelectedIndex(trainer.getTrainerClass());

        int idx= 0;
        effectivenessPriorityCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        attackEvaluationCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        expertModeCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        statusPriorityCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        riskyAttackCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        healingPriorityCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        partnerTrainerCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        doubleBattleCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        healingPriorityCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        utilizeWeatherCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        harassmentCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        roamingCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        safariZoneCheckbox.setSelected(((trainer.getAI() >> idx++) & 1) == 1);
        catchingDemoCheckbox.setSelected(((trainer.getAI() >> idx) & 1) == 1);



        pokemonList= trainerEditor.parseTrainerTeam(Arrays.copyOfRange(trainerPokemonTable[trainerSelectionComboBox.getSelectedIndex()+1],2,trainerPokemonTable[0].length),trainer.getNumPokemon());
        setPokemonList();
    }

    private boolean contains(String[] arr, String str)
    {
        for(String s : arr)
        {
            if(str.startsWith(s))
                return true;
        }
        return false;
    }




    static class Animator implements ActionListener
    {
        final Timer timer;
        final JButton button;
        BufferedImage[] trainerFrames;
        int index;

        public Animator(JButton button)
        {
            this.button= button;
            timer= new Timer(190,this);
        }

        public void reset(BufferedImage[] trainerFrames)
        {
            if(this.trainerFrames == null)
            {
                timer.start();
            }

            BufferedImage[] arr= new BufferedImage[(int) (trainerFrames.length*2)];

//            if(trainerFrames.length <= 2)
//            {
                Arrays.fill(arr,trainerFrames[trainerFrames.length-1]);
                System.arraycopy(trainerFrames,0,arr,0,trainerFrames.length);
//            }
//            else
//            {
//                Arrays.fill(arr,trainerFrames[trainerFrames.length-1]);
//                System.arraycopy(Arrays.copyOf(trainerFrames,trainerFrames.length-1),0,arr,0,trainerFrames.length-1);
//            }
            this.trainerFrames= arr;


            index= 0;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            BufferedImage frame= trainerFrames[index++ % trainerFrames.length];

            button.setIcon(new ImageIcon(frame));
        }
    }
}
