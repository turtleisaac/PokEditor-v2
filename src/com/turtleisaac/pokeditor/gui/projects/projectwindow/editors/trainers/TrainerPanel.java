/*
 * Created by JFormDesigner on Thu Jan 21 16:34:37 EST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.editors.trainers;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerDataGen4;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerPokemonData;
import net.miginfocom.swing.*;

/**
 * @author Truck
 */
public class TrainerPanel extends JPanel
{
    private TrainerDataGen4 trainer;
    private ArrayList<TrainerPokemonData> pokemonlist;
    private int lastSelected;

    public TrainerPanel()
    {
        initComponents();
        lastSelected= 0;
        trainerPokemonTabbedPane.addTab("Pokémon " + (trainerPokemonTabbedPane.getTabCount()+1), new TrainerPokemonPanel(null,toggleMovesCheckbox.isSelected(),toggleHeldItemsCheckbox.isSelected()));

        ComboBoxSearchable itemComboBoxSearchable1= new ComboBoxSearchable(itemComboBox1);
        ComboBoxSearchable itemComboBoxSearchable2= new ComboBoxSearchable(itemComboBox2);
        ComboBoxSearchable itemComboBoxSearchable3= new ComboBoxSearchable(itemComboBox3);
        ComboBoxSearchable itemComboBoxSearchable4= new ComboBoxSearchable(itemComboBox4);

        ComboBoxSearchable trainerClassComboBoxSearchable= new ComboBoxSearchable(trainerClassSelectorComboBox);
        ComboBoxSearchable trainerSelectionComboBoxSearchable= new ComboBoxSearchable(trainerSelectionComboBox);
    }

    public void setTrainer(TrainerDataGen4 trainer)
    {
        this.trainer = trainer;
    }

    public void setPokemonlist(ArrayList<TrainerPokemonData> pokemonlist)
    {
        this.pokemonlist= pokemonlist;
        for(TrainerPokemonData pokemon : pokemonlist)
        {
            trainerPokemonTabbedPane.addTab("Pokémon " + (trainerPokemonTabbedPane.getTabCount()+1), new TrainerPokemonPanel(null,toggleMovesCheckbox.isSelected(),toggleHeldItemsCheckbox.isSelected()));
        }
    }

    private void trainerSelectionComboBoxActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void newTrainerButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void trainerClassSelectorComboBoxActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void trainerClassImageButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void numberPokemonSliderStateChanged(ChangeEvent e)
    {
        if(numberPokemonSlider.getValue() >= lastSelected)
        {
            while(trainerPokemonTabbedPane.getTabCount() != numberPokemonSlider.getValue())
            {
                trainerPokemonTabbedPane.addTab("Pokémon " + (trainerPokemonTabbedPane.getTabCount()+1), new TrainerPokemonPanel(null,toggleMovesCheckbox.isSelected(),toggleHeldItemsCheckbox.isSelected()));
            }
        }
        else
        {
            while(trainerPokemonTabbedPane.getTabCount() != numberPokemonSlider.getValue())
            {
                trainerPokemonTabbedPane.removeTabAt(trainerPokemonTabbedPane.getTabCount()-1);
            }
        }

        lastSelected= numberPokemonSlider.getValue();
    }

    private void toggleMovesCheckboxItemStateChanged(ItemEvent e)
    {
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
        System.out.println("Tab count: " + trainerPokemonTabbedPane.getTabCount());
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        trainerDataPanel = new JPanel();
        trainerSelectionComboBox = new JComboBox();
        newTrainerButton = new JButton();
        hSpacer1 = new JPanel(null);
        toggleMovesCheckbox = new JCheckBox();
        toggleHeldItemsCheckbox = new JCheckBox();
        checkBox1 = new JCheckBox();
        trainerDataSubPanel = new JPanel();
        trainerNameLabel = new JLabel();
        itemPanel = new JPanel();
        itemComboBox1 = new JComboBox();
        itemComboBox2 = new JComboBox();
        itemComboBox3 = new JComboBox();
        itemComboBox4 = new JComboBox();
        trainerNameTextField = new JTextField();
        numberPokemonLabel = new JLabel();
        numberPokemonSlider = new JSlider();
        trainerClassPanel = new JPanel();
        trainerClassImageButton = new JButton();
        trainerClassSelectorComboBox = new JComboBox();
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
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[grow,center]"));

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

            //---- trainerSelectionComboBox ----
            trainerSelectionComboBox.addActionListener(e -> trainerSelectionComboBoxActionPerformed(e));
            trainerDataPanel.add(trainerSelectionComboBox, "cell 0 0");

            //---- newTrainerButton ----
            newTrainerButton.setText("New Trainer");
            newTrainerButton.addActionListener(e -> newTrainerButtonActionPerformed(e));
            trainerDataPanel.add(newTrainerButton, "cell 1 0");
            trainerDataPanel.add(hSpacer1, "cell 1 0 2 1");

            //---- toggleMovesCheckbox ----
            toggleMovesCheckbox.setText("Moves");
            toggleMovesCheckbox.addItemListener(e -> toggleMovesCheckboxItemStateChanged(e));
            trainerDataPanel.add(toggleMovesCheckbox, "cell 2 0");

            //---- toggleHeldItemsCheckbox ----
            toggleHeldItemsCheckbox.setText("Held Items");
            toggleHeldItemsCheckbox.addItemListener(e -> toggleHeldItemsCheckboxItemStateChanged(e));
            trainerDataPanel.add(toggleHeldItemsCheckbox, "cell 3 0");

            //---- checkBox1 ----
            checkBox1.setText("Double Battle");
            trainerDataPanel.add(checkBox1, "cell 4 0");

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

                //---- trainerNameTextField ----
                trainerNameTextField.setText("                                                                                    ");
                trainerDataSubPanel.add(trainerNameTextField, "cell 0 1,alignx left,growx 0");

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
        add(trainerDataPanel, "cell 0 0");

        //======== trainerPokemonTabbedPane ========
        {
            trainerPokemonTabbedPane.setBorder(new TitledBorder("Pok\u00e9mon"));
        }
        add(trainerPokemonTabbedPane, "cell 0 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel trainerDataPanel;
    private JComboBox trainerSelectionComboBox;
    private JButton newTrainerButton;
    private JPanel hSpacer1;
    private JCheckBox toggleMovesCheckbox;
    private JCheckBox toggleHeldItemsCheckbox;
    private JCheckBox checkBox1;
    private JPanel trainerDataSubPanel;
    private JLabel trainerNameLabel;
    private JPanel itemPanel;
    private JComboBox itemComboBox1;
    private JComboBox itemComboBox2;
    private JComboBox itemComboBox3;
    private JComboBox itemComboBox4;
    private JTextField trainerNameTextField;
    private JLabel numberPokemonLabel;
    private JSlider numberPokemonSlider;
    private JPanel trainerClassPanel;
    private JButton trainerClassImageButton;
    private JComboBox trainerClassSelectorComboBox;
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
}
