/*
 * Created by JFormDesigner on Thu Jan 21 15:19:22 EST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.editors.trainers;

import javax.swing.*;
import javax.swing.border.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerPokemonData;
import net.miginfocom.swing.*;

/**
 * @author Truck
 */
public class TrainerPokemonPanel extends JPanel
{
    private TrainerPokemonData pokemonData;

    public TrainerPokemonPanel(TrainerPokemonData pokemon, boolean moves, boolean item)
    {
        initComponents();
        pokemonData= pokemon;

        ComboBoxSearchable speciesSearchable= new ComboBoxSearchable(speciesComboBox);
        ComboBoxSearchable heldItemSearchable= new ComboBoxSearchable(heldItemComboBox);
        ComboBoxSearchable move1Searchable= new ComboBoxSearchable(move1ComboBox);
        ComboBoxSearchable move2Searchable= new ComboBoxSearchable(move2ComboBox);
        ComboBoxSearchable move3Searchable= new ComboBoxSearchable(move3ComboBox);
        ComboBoxSearchable move4Searchable= new ComboBoxSearchable(move4ComboBox);

        setMovesEnabled(moves);
        setItemEnabled(item);
    }



    public void setMovesEnabled(boolean bool)
    {
        move1ComboBox.setEnabled(bool);
        move2ComboBox.setEnabled(bool);
        move3ComboBox.setEnabled(bool);
        move4ComboBox.setEnabled(bool);
    }

    public void setItemEnabled(boolean bool)
    {
        heldItemComboBox.setEnabled(bool);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        movesPanel = new JPanel();
        move1ComboBox = new JComboBox();
        move2ComboBox = new JComboBox();
        move3ComboBox = new JComboBox();
        move4ComboBox = new JComboBox();
        sealPanel = new JPanel();
        comboBox2 = new JComboBox();
        generalPanel = new JPanel();
        speciesLabel = new JLabel();
        speciesComboBox = new JComboBox();
        heldItemLabel = new JLabel();
        heldItemComboBox = new JComboBox();
        pidPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        formLabel = new JLabel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        comboBox1 = new JComboBox();
        formSlider = new JSlider();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]" +
            "[338,grow,fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]"));

        //======== movesPanel ========
        {
            movesPanel.setBorder(new TitledBorder("Moves"));
            movesPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[grow,fill]" +
                "[grow,fill]",
                // rows
                "[grow]" +
                "[grow]"));
            movesPanel.add(move1ComboBox, "cell 0 0");
            movesPanel.add(move2ComboBox, "cell 1 0");
            movesPanel.add(move3ComboBox, "cell 0 1");
            movesPanel.add(move4ComboBox, "cell 1 1");
        }
        add(movesPanel, "cell 1 0,grow");

        //======== sealPanel ========
        {
            sealPanel.setBorder(new TitledBorder("Pok\u00e9 Ball Seals"));
            sealPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]"));
            sealPanel.add(comboBox2, "cell 0 0");
        }
        add(sealPanel, "cell 2 0,grow");

        //======== generalPanel ========
        {
            generalPanel.setBorder(new TitledBorder("General"));
            generalPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[grow,fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- speciesLabel ----
            speciesLabel.setText("Species");
            generalPanel.add(speciesLabel, "cell 0 0,grow");
            generalPanel.add(speciesComboBox, "cell 0 1,grow");

            //---- heldItemLabel ----
            heldItemLabel.setText("Held Item");
            generalPanel.add(heldItemLabel, "cell 0 2,grow");
            generalPanel.add(heldItemComboBox, "cell 0 3,grow");
        }
        add(generalPanel, "cell 0 0,growy");

        //======== pidPanel ========
        {
            pidPanel.setBorder(new TitledBorder("PID Generation"));
            pidPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[grow,fill]" +
                "[fill]" +
                "[grow,fill]",
                // rows
                "[]" +
                "[]"));

            //---- label1 ----
            label1.setText("Level");
            pidPanel.add(label1, "cell 0 0,alignx center,growx 0");

            //---- label2 ----
            label2.setText("IV Value");
            pidPanel.add(label2, "cell 1 0,alignx center,growx 0");

            //---- label3 ----
            label3.setText("Ability");
            pidPanel.add(label3, "cell 2 0,alignx center,growx 0");

            //---- label4 ----
            label4.setText("Nature");
            pidPanel.add(label4, "cell 3 0 2 1,alignx center,growx 0");

            //---- formLabel ----
            formLabel.setText("Form No.");
            pidPanel.add(formLabel, "cell 5 0,alignx center,growx 0");

            //---- textField1 ----
            textField1.setToolTipText("A value ranging from 1 to 100");
            pidPanel.add(textField1, "cell 0 1");

            //---- textField2 ----
            textField2.setToolTipText("A value ranging from 0 to 31");
            pidPanel.add(textField2, "cell 1 1");

            //---- textField3 ----
            textField3.setToolTipText("Changes which ability posessed by this species is used (HGSS only)");
            pidPanel.add(textField3, "cell 2 1");
            pidPanel.add(comboBox1, "cell 3 1 2 1");

            //---- formSlider ----
            formSlider.setPaintLabels(true);
            formSlider.setPaintTicks(true);
            formSlider.setSnapToTicks(true);
            formSlider.setValue(0);
            formSlider.setMaximum(1);
            formSlider.setMajorTickSpacing(1);
            pidPanel.add(formSlider, "cell 5 1");
        }
        add(pidPanel, "cell 0 1 3 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel movesPanel;
    private JComboBox move1ComboBox;
    private JComboBox move2ComboBox;
    private JComboBox move3ComboBox;
    private JComboBox move4ComboBox;
    private JPanel sealPanel;
    private JComboBox comboBox2;
    private JPanel generalPanel;
    private JLabel speciesLabel;
    private JComboBox speciesComboBox;
    private JLabel heldItemLabel;
    private JComboBox heldItemComboBox;
    private JPanel pidPanel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel formLabel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox1;
    private JSlider formSlider;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
