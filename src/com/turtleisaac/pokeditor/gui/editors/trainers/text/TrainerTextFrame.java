/*
 * Created by JFormDesigner on Tue Nov 23 17:50:50 CST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.trainers.text;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerText;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerTextUtils;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Project;
import net.miginfocom.swing.*;
import org.apache.commons.io.FileUtils;

/**
 * @author turtleisaac
 */
public class TrainerTextFrame extends JFrame {
    private String[] messages;

    private HashMap<Integer, TrainerText> thisTrainerTexts;

    private static final ArrayList<Integer> activationConditionToId = new ArrayList<>(Arrays.asList(0, 1, 2, 0x0F, 0x10, 0x14, 3, 4, 5, 6, 7, 8, 9, 0xA));

    private static final ArrayList<String> activationConditions = new ArrayList<>(Arrays.asList(
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
    ));

    public TrainerTextFrame(Project project, ArrayList<TrainerText> trainerTexts, int trainerId, String trainerName) throws IOException
    {
        initComponents();
        setVisible(true);

        setTitle("Trainer Text Editor - " + trainerName);

        switch (project.getBaseRom())
        {
            case Platinum:
//                trainerTextBankId = 728;
                break;

            case HeartGold:
            case SoulSilver:
                messages = TextEditor.getBank(project, 728);
//                trainerTextBankId = 728;
                break;
        }

        thisTrainerTexts = new HashMap<>();
        TrainerText trainerText;
        for (int i = 0; i < trainerTexts.size(); i++)
        {
            trainerText = trainerTexts.get(i);
            if (trainerId == trainerText.getTrainerId())
            {
                thisTrainerTexts.put(i, trainerText);
            }
        }

        DefaultListModel<String> listModel= new DefaultListModel<>();


        for(TrainerText trainerText1 : thisTrainerTexts.values())
        {
            listModel.addElement("" + activationConditions.get(activationConditionToId.indexOf(trainerText1.getCondition())));
        }

        textList.setModel(listModel);

        ComboBoxSearchable searchable= new ComboBoxSearchable(activationComboBox);
    }

    private void textListValueChanged(ListSelectionEvent e) {
        // TODO add your code here
        activationComboBox.setSelectedIndex(activationConditionToId.indexOf(thisTrainerTexts.get(textList.getSelectedIndex()).getCondition()));
        trainerTextArea.setText(messages[thisTrainerTexts.get(textList.getSelectedIndex()).getTextId()]);
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void thisWindowClosed(WindowEvent e) {
        dispose();
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        scrollPane1 = new JScrollPane();
        textList = new JList<>();
        addRemovePanel = new JPanel();
        addButton = new JButton();
        removeButton = new JButton();
        saveButton = new JButton();
        reloadButton = new JButton();
        editPanel = new JPanel();
        activationLabel = new JLabel();
        activationComboBox = new JComboBox<>();
        textLabel = new JLabel();
        scrollPane2 = new JScrollPane();
        trainerTextArea = new JTextArea();

        //======== this ========
        setMinimumSize(new Dimension(800, 352));
        setTitle("Trainer Text Editor");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
        });
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[225,fill]" +
            "[grow,fill]" +
            "[fill]",
            // rows
            "[172]" +
            "[grow]"));

        //======== scrollPane1 ========
        {

            //---- textList ----
            textList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            textList.setModel(new AbstractListModel<String>() {
                String[] values = {
                    "Test"
                };
                @Override
                public int getSize() { return values.length; }
                @Override
                public String getElementAt(int i) { return values[i]; }
            });
            textList.addListSelectionListener(e -> textListValueChanged(e));
            scrollPane1.setViewportView(textList);
        }
        contentPane.add(scrollPane1, "cell 0 0 2 1");

        //======== addRemovePanel ========
        {
            addRemovePanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]",
                // rows
                "[grow]" +
                "[grow]" +
                "[]" +
                "[]"));

            //---- addButton ----
            addButton.setText("Add");
            addRemovePanel.add(addButton, "cell 0 0,grow");

            //---- removeButton ----
            removeButton.setText("Remove");
            addRemovePanel.add(removeButton, "cell 0 1,grow");

            //---- saveButton ----
            saveButton.setText("Save");
            addRemovePanel.add(saveButton, "cell 0 2,grow");

            //---- reloadButton ----
            reloadButton.setText("Reload");
            addRemovePanel.add(reloadButton, "cell 0 3,grow");
        }
        contentPane.add(addRemovePanel, "cell 2 0,growy");

        //======== editPanel ========
        {
            editPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[427,grow,fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[grow]"));

            //---- activationLabel ----
            activationLabel.setText("Activation Condition:");
            editPanel.add(activationLabel, "cell 0 0");

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
            editPanel.add(activationComboBox, "cell 1 0 2 1");

            //---- textLabel ----
            textLabel.setText("Text:");
            editPanel.add(textLabel, "cell 0 1");

            //======== scrollPane2 ========
            {

                //---- trainerTextArea ----
                trainerTextArea.setLineWrap(true);
                trainerTextArea.setWrapStyleWord(true);
                scrollPane2.setViewportView(trainerTextArea);
            }
            editPanel.add(scrollPane2, "cell 0 2 3 1,grow");
        }
        contentPane.add(editPanel, "cell 0 1 3 1,grow");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JScrollPane scrollPane1;
    private JList<String> textList;
    private JPanel addRemovePanel;
    private JButton addButton;
    private JButton removeButton;
    private JButton saveButton;
    private JButton reloadButton;
    private JPanel editPanel;
    private JLabel activationLabel;
    private JComboBox<String> activationComboBox;
    private JLabel textLabel;
    private JScrollPane scrollPane2;
    private JTextArea trainerTextArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
