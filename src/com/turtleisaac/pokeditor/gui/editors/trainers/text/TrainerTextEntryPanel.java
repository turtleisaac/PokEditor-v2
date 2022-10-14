/*
 * Created by JFormDesigner on Thu Oct 13 13:37:39 CDT 2022
 */

package com.turtleisaac.pokeditor.gui.editors.trainers.text;

import java.awt.event.*;
import javax.swing.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerText;
import net.miginfocom.swing.*;

/**
 * @author Daniel Horn
 */
public class TrainerTextEntryPanel extends JPanel {

    public TrainerTextEntryPanel(TrainerTextFrame trainerTextFrame, TrainerText text) {
        initComponents();

        activationComboBox.setSelectedIndex(TrainerTextFrame.activationConditionToId.indexOf(text.getCondition()));
        trainerTextArea.setText(text.getText());

        ComboBoxSearchable searchable= new ComboBoxSearchable(activationComboBox);
    }

    public String getText()
    {
        return trainerTextArea.getText();
    }

    public int getActivationCondition()
    {
        return TrainerTextFrame.activationConditionToId.get(activationComboBox.getSelectedIndex());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        activationLabel = new JLabel();
        activationComboBox = new JComboBox<>();
        textLabel = new JLabel();
        trainerTextArea = new JTextArea();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[grow,fill]",
            // rows
            "[]" +
            "[]" +
            "[grow,fill]"));

        //---- activationLabel ----
        activationLabel.setText("Activation Condition:");
        add(activationLabel, "cell 0 0");

        //---- activationComboBox ----
        activationComboBox.setModel(new DefaultComboBoxModel<>(TrainerTextFrame.activationConditions.toArray(new String[0])));
        add(activationComboBox, "cell 1 0,growx");

        //---- textLabel ----
        textLabel.setText("Text:");
        add(textLabel, "cell 0 1");

        //---- trainerTextArea ----
        trainerTextArea.setLineWrap(true);
        trainerTextArea.setWrapStyleWord(true);
        add(trainerTextArea, "cell 0 2 2 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel activationLabel;
    private JComboBox activationComboBox;
    private JLabel textLabel;
    private JTextArea trainerTextArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
