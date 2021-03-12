/*
 * Created by JFormDesigner on Mon Mar 01 11:16:57 EST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.encounters.johto;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * @author turtleisaac
 */
public class JohtoEncounterPanel extends JPanel {
    public JohtoEncounterPanel() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        encounterFileLabel = new JLabel();
        speciesSearchLabel = new JLabel();
        locationSearchLabel = new JLabel();
        encounterFileComboBox = new JComboBox();
        saveButton = new JButton();
        hSpacer1 = new JPanel(null);
        speciesSearchTextField = new JTextField();
        speciesSearchButton = new JButton();
        hSpacer2 = new JPanel(null);
        locationSearchTextField = new JTextField();
        locationSearchButton = new JButton();
        separator1 = new JSeparator();
        label4 = new JLabel();
        label3 = new JLabel();
        tabbedPane1 = new JTabbedPane();
        fieldEncounterPanel = new JPanel();
        fieldPanel = new JPanel();
        encounterRateLabel = new JLabel();
        encounterRateSpinner = new JSpinner();
        label6 = new JLabel();
        field20ComboBox1 = new JComboBox();
        label7 = new JLabel();
        field20ComboBox2 = new JComboBox();
        label8 = new JLabel();
        field10ComboBox1 = new JComboBox();
        label9 = new JLabel();
        field10ComboBox2 = new JComboBox();
        label10 = new JLabel();
        field10ComboBox3 = new JComboBox();
        label11 = new JLabel();
        field10ComboBox4 = new JComboBox();
        label12 = new JLabel();
        field5ComboBox1 = new JComboBox();
        label13 = new JLabel();
        field5ComboBox2 = new JComboBox();
        label14 = new JLabel();
        field4ComboBox1 = new JComboBox();
        label15 = new JLabel();
        field4ComboBox2 = new JComboBox();
        label16 = new JLabel();
        field1ComboBox1 = new JComboBox();
        label17 = new JLabel();
        field1ComboBox2 = new JComboBox();
        swarmDayNightPanel = new JPanel();
        label18 = new JLabel();
        label19 = new JLabel();
        swarm20ComboBox1 = new JComboBox();
        label20 = new JLabel();
        label21 = new JLabel();
        swarm20ComboBox2 = new JComboBox();
        separator2 = new JSeparator();
        label22 = new JLabel();
        day10ComboBox1 = new JComboBox();
        label23 = new JLabel();
        day10ComboBox2 = new JComboBox();
        separator3 = new JSeparator();
        label24 = new JLabel();
        label25 = new JLabel();
        night10ComboBox1 = new JComboBox();
        label26 = new JLabel();
        night10ComboBox2 = new JComboBox();
        pokeRadarPanel = new JPanel();
        label27 = new JLabel();
        radar10ComboBox1 = new JComboBox();
        label28 = new JLabel();
        radar10ComboBox2 = new JComboBox();
        label29 = new JLabel();
        radar10ComboBox3 = new JComboBox();
        label30 = new JLabel();
        radar10ComboBox4 = new JComboBox();
        encounterCalculatorPanel = new JPanel();
        label33 = new JLabel();
        textField2 = new JTextField();
        hSpacer3 = new JPanel(null);
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        checkBox3 = new JCheckBox();
        hSpacer4 = new JPanel(null);
        button3 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        waterEncounterPanel = new JPanel();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[124,fill]" +
            "[80,fill]" +
            "[45,fill]" +
            "[150,fill]" +
            "[fill]" +
            "[45,fill]" +
            "[154,fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[grow]"));

        //---- encounterFileLabel ----
        encounterFileLabel.setText("Encounter FIle");
        add(encounterFileLabel, "cell 1 0,alignx center,growx 0");

        //---- speciesSearchLabel ----
        speciesSearchLabel.setText("Search by Species");
        add(speciesSearchLabel, "cell 4 0,alignx center,growx 0");

        //---- locationSearchLabel ----
        locationSearchLabel.setText("Search by Location");
        add(locationSearchLabel, "cell 7 0,alignx center,growx 0");
        add(encounterFileComboBox, "cell 1 1");

        //---- saveButton ----
        saveButton.setText("Save");
        add(saveButton, "cell 2 1");
        add(hSpacer1, "cell 3 1");
        add(speciesSearchTextField, "cell 4 1");

        //---- speciesSearchButton ----
        speciesSearchButton.setText("Search");
        add(speciesSearchButton, "cell 5 1");
        add(hSpacer2, "cell 6 1");
        add(locationSearchTextField, "cell 7 1");

        //---- locationSearchButton ----
        locationSearchButton.setText("Search");
        add(locationSearchButton, "cell 8 1");
        add(separator1, "cell 1 2 8 1");

        //---- label4 ----
        label4.setText("This encounter file is used in:");
        add(label4, "cell 1 3 8 1,alignx center,growx 0");
        add(label3, "cell 1 4 8 1,alignx center,growx 0");

        //======== tabbedPane1 ========
        {
            tabbedPane1.setBorder(new TitledBorder(""));

            //======== fieldEncounterPanel ========
            {
                fieldEncounterPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[274,fill]" +
                    "[grow,fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[grow]"));

                //======== fieldPanel ========
                {
                    fieldPanel.setBorder(new TitledBorder("Field"));
                    fieldPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[140,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- encounterRateLabel ----
                    encounterRateLabel.setText("Encounter Rate:");
                    fieldPanel.add(encounterRateLabel, "cell 0 0");
                    fieldPanel.add(encounterRateSpinner, "cell 1 0");

                    //---- label6 ----
                    label6.setText("20%");
                    fieldPanel.add(label6, "cell 0 1,alignx right,growx 0");
                    fieldPanel.add(field20ComboBox1, "cell 1 1");

                    //---- label7 ----
                    label7.setText("20%");
                    fieldPanel.add(label7, "cell 0 2,alignx right,growx 0");
                    fieldPanel.add(field20ComboBox2, "cell 1 2");

                    //---- label8 ----
                    label8.setText("10%");
                    fieldPanel.add(label8, "cell 0 3,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox1, "cell 1 3");

                    //---- label9 ----
                    label9.setText("10%");
                    fieldPanel.add(label9, "cell 0 4,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox2, "cell 1 4");

                    //---- label10 ----
                    label10.setText("10%");
                    fieldPanel.add(label10, "cell 0 5,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox3, "cell 1 5");

                    //---- label11 ----
                    label11.setText("10%");
                    fieldPanel.add(label11, "cell 0 6,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox4, "cell 1 6");

                    //---- label12 ----
                    label12.setText("5%");
                    fieldPanel.add(label12, "cell 0 7,alignx right,growx 0");
                    fieldPanel.add(field5ComboBox1, "cell 1 7");

                    //---- label13 ----
                    label13.setText("5%");
                    fieldPanel.add(label13, "cell 0 8,alignx right,growx 0");
                    fieldPanel.add(field5ComboBox2, "cell 1 8");

                    //---- label14 ----
                    label14.setText("4%");
                    fieldPanel.add(label14, "cell 0 9,alignx right,growx 0");
                    fieldPanel.add(field4ComboBox1, "cell 1 9");

                    //---- label15 ----
                    label15.setText("4%");
                    fieldPanel.add(label15, "cell 0 10,alignx right,growx 0");
                    fieldPanel.add(field4ComboBox2, "cell 1 10");

                    //---- label16 ----
                    label16.setText("1%");
                    fieldPanel.add(label16, "cell 0 11,alignx right,growx 0");
                    fieldPanel.add(field1ComboBox1, "cell 1 11");

                    //---- label17 ----
                    label17.setText("1%");
                    fieldPanel.add(label17, "cell 0 12,alignx right,growx 0");
                    fieldPanel.add(field1ComboBox2, "cell 1 12");
                }
                fieldEncounterPanel.add(fieldPanel, "cell 0 0 1 2");

                //======== swarmDayNightPanel ========
                {
                    swarmDayNightPanel.setBorder(new TitledBorder("Swarm/ Day/ Night"));
                    swarmDayNightPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[140,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label18 ----
                    label18.setText("Swarm");
                    swarmDayNightPanel.add(label18, "cell 0 0");

                    //---- label19 ----
                    label19.setText("20%");
                    swarmDayNightPanel.add(label19, "cell 0 1,alignx right,growx 0");
                    swarmDayNightPanel.add(swarm20ComboBox1, "cell 1 1");

                    //---- label20 ----
                    label20.setText("20%");
                    swarmDayNightPanel.add(label20, "cell 0 2,alignx right,growx 0");

                    //---- label21 ----
                    label21.setText("Day");
                    swarmDayNightPanel.add(label21, "cell 0 4");
                    swarmDayNightPanel.add(swarm20ComboBox2, "cell 1 2");
                    swarmDayNightPanel.add(separator2, "cell 0 3 2 1");

                    //---- label22 ----
                    label22.setText("10%");
                    swarmDayNightPanel.add(label22, "cell 0 5,alignx right,growx 0");
                    swarmDayNightPanel.add(day10ComboBox1, "cell 1 5");

                    //---- label23 ----
                    label23.setText("10%");
                    swarmDayNightPanel.add(label23, "cell 0 6,alignx right,growx 0");
                    swarmDayNightPanel.add(day10ComboBox2, "cell 1 6");
                    swarmDayNightPanel.add(separator3, "cell 0 7 2 1");

                    //---- label24 ----
                    label24.setText("Night");
                    swarmDayNightPanel.add(label24, "cell 0 8");

                    //---- label25 ----
                    label25.setText("10%");
                    swarmDayNightPanel.add(label25, "cell 0 9,alignx right,growx 0");
                    swarmDayNightPanel.add(night10ComboBox1, "cell 1 9");

                    //---- label26 ----
                    label26.setText("10%");
                    swarmDayNightPanel.add(label26, "cell 0 10,alignx right,growx 0");
                    swarmDayNightPanel.add(night10ComboBox2, "cell 1 10");
                }
                fieldEncounterPanel.add(swarmDayNightPanel, "cell 1 0 2 1,alignx left,growx 0");

                //======== pokeRadarPanel ========
                {
                    pokeRadarPanel.setBorder(new TitledBorder("Pok\u00e9 Radar"));
                    pokeRadarPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[grow,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label27 ----
                    label27.setText("10%");
                    pokeRadarPanel.add(label27, "cell 0 0");
                    pokeRadarPanel.add(radar10ComboBox1, "cell 1 0");

                    //---- label28 ----
                    label28.setText("10%");
                    pokeRadarPanel.add(label28, "cell 0 1");
                    pokeRadarPanel.add(radar10ComboBox2, "cell 1 1");

                    //---- label29 ----
                    label29.setText("10%");
                    pokeRadarPanel.add(label29, "cell 0 2");
                    pokeRadarPanel.add(radar10ComboBox3, "cell 1 2");

                    //---- label30 ----
                    label30.setText("10%");
                    pokeRadarPanel.add(label30, "cell 0 3");
                    pokeRadarPanel.add(radar10ComboBox4, "cell 1 3");
                }
                fieldEncounterPanel.add(pokeRadarPanel, "cell 1 0 2 1,aligny top,grow 100 0");

                //======== encounterCalculatorPanel ========
                {
                    encounterCalculatorPanel.setBorder(new TitledBorder("Encounter Slot Calculator"));
                    encounterCalculatorPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[86,fill]" +
                        "[110,grow,fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[grow,fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]0" +
                        "[]"));

                    //---- label33 ----
                    label33.setText("Time of day (0-23)");
                    encounterCalculatorPanel.add(label33, "cell 0 0");
                    encounterCalculatorPanel.add(textField2, "cell 1 0");
                    encounterCalculatorPanel.add(hSpacer3, "cell 2 0");

                    //---- checkBox1 ----
                    checkBox1.setText("Swarm");
                    encounterCalculatorPanel.add(checkBox1, "cell 3 0");

                    //---- checkBox2 ----
                    checkBox2.setText("Pok\u00e9 Radar");
                    encounterCalculatorPanel.add(checkBox2, "cell 4 0");

                    //---- checkBox3 ----
                    checkBox3.setText("Dual-Slot Mode");
                    encounterCalculatorPanel.add(checkBox3, "cell 5 0");
                    encounterCalculatorPanel.add(hSpacer4, "cell 6 0");

                    //---- button3 ----
                    button3.setText("Calculate");
                    encounterCalculatorPanel.add(button3, "cell 7 0");

                    //======== scrollPane1 ========
                    {

                        //---- table1 ----
                        table1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                        scrollPane1.setViewportView(table1);
                    }
                    encounterCalculatorPanel.add(scrollPane1, "cell 0 1 8 1,grow");
                }
                fieldEncounterPanel.add(encounterCalculatorPanel, "cell 0 2 3 1,growy");
            }
            tabbedPane1.addTab("Field Encounters", fieldEncounterPanel);

            //======== waterEncounterPanel ========
            {
                waterEncounterPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));
            }
            tabbedPane1.addTab("Water Encounters", waterEncounterPanel);
        }
        add(tabbedPane1, "cell 1 5 8 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel encounterFileLabel;
    private JLabel speciesSearchLabel;
    private JLabel locationSearchLabel;
    private JComboBox encounterFileComboBox;
    private JButton saveButton;
    private JPanel hSpacer1;
    private JTextField speciesSearchTextField;
    private JButton speciesSearchButton;
    private JPanel hSpacer2;
    private JTextField locationSearchTextField;
    private JButton locationSearchButton;
    private JSeparator separator1;
    private JLabel label4;
    private JLabel label3;
    private JTabbedPane tabbedPane1;
    private JPanel fieldEncounterPanel;
    private JPanel fieldPanel;
    private JLabel encounterRateLabel;
    private JSpinner encounterRateSpinner;
    private JLabel label6;
    private JComboBox field20ComboBox1;
    private JLabel label7;
    private JComboBox field20ComboBox2;
    private JLabel label8;
    private JComboBox field10ComboBox1;
    private JLabel label9;
    private JComboBox field10ComboBox2;
    private JLabel label10;
    private JComboBox field10ComboBox3;
    private JLabel label11;
    private JComboBox field10ComboBox4;
    private JLabel label12;
    private JComboBox field5ComboBox1;
    private JLabel label13;
    private JComboBox field5ComboBox2;
    private JLabel label14;
    private JComboBox field4ComboBox1;
    private JLabel label15;
    private JComboBox field4ComboBox2;
    private JLabel label16;
    private JComboBox field1ComboBox1;
    private JLabel label17;
    private JComboBox field1ComboBox2;
    private JPanel swarmDayNightPanel;
    private JLabel label18;
    private JLabel label19;
    private JComboBox swarm20ComboBox1;
    private JLabel label20;
    private JLabel label21;
    private JComboBox swarm20ComboBox2;
    private JSeparator separator2;
    private JLabel label22;
    private JComboBox day10ComboBox1;
    private JLabel label23;
    private JComboBox day10ComboBox2;
    private JSeparator separator3;
    private JLabel label24;
    private JLabel label25;
    private JComboBox night10ComboBox1;
    private JLabel label26;
    private JComboBox night10ComboBox2;
    private JPanel pokeRadarPanel;
    private JLabel label27;
    private JComboBox radar10ComboBox1;
    private JLabel label28;
    private JComboBox radar10ComboBox2;
    private JLabel label29;
    private JComboBox radar10ComboBox3;
    private JLabel label30;
    private JComboBox radar10ComboBox4;
    private JPanel encounterCalculatorPanel;
    private JLabel label33;
    private JTextField textField2;
    private JPanel hSpacer3;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JPanel hSpacer4;
    private JButton button3;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel waterEncounterPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
