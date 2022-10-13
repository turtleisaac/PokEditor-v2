/*
 * Created by JFormDesigner on Thu Oct 13 13:37:39 CDT 2022
 */

package com.turtleisaac.pokeditor.gui.editors.trainers.text;

import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Daniel Horn
 */
public class TrainerTextEntryPanel extends JPanel {
    public TrainerTextEntryPanel() {
        initComponents();
    }

    private void activationConditionActionPerformed(ActionEvent e) {
        // TODO add your code here
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
        activationComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "Pre-Battle Overworld",
            "In-Battle Trainer Defeat",
            "Post-Battle Trainer Defeat Overworld",
            "In-Battle Last Pokemon",
            "In-Battle Last Pokemon Critical HP",
            "In-Battle Player Lose",
            "(DOUBLE) Pre-Battle Trainer#1 Overworld",
            "(DOUBLE) In-Battle Trainer#1 Defeat",
            "(DOUBLE) Post-Battle Trainer#1 Defeat Overworld",
            "(DOUBLE) Trainer#1 Player One Party Member Overworld",
            "(DOUBLE) Pre-Battle Trainer#2 Overworld",
            "(DOUBLE) In-Battle Trainer#2 Defeat",
            "(DOUBLE) Post-Battle Trainer#2 Defeat Overworld",
            "(DOUBLE) Trainer#2 Player One Party Member Overworld"
        }));
        activationComboBox.addActionListener(e -> activationConditionActionPerformed(e));
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
    private JComboBox<String> activationComboBox;
    private JLabel textLabel;
    private JTextArea trainerTextArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
