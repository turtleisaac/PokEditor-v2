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
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.gui.*;
import com.turtleisaac.pokeditor.project.Project;
import net.miginfocom.swing.*;
import org.apache.commons.io.FileUtils;

/**
 * @author turtleisaac
 */
public class TrainerTextFrame extends JFrame {
    TrainerTextUtils trainerTextRetriever;
    private ArrayList<TrainerText> trainerTexts;
    private String trainerTextAssignmentFile;
    private int trainerTextBankId;
    private ArrayList<File> toDelete;
    private String[] messages;

    private ArrayList<TrainerText> thisTrainerTexts;

    private final Project project;


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

    public TrainerTextFrame(Project project, int trainerId, String trainerName) throws IOException
    {
        initComponents();
        toDelete = new ArrayList<>();
        setVisible(true);

        setTitle("Trainer Text Editor - " + trainerName);

        this.project= project;
        trainerTextRetriever = new TrainerTextUtils(project, project.getProjectPath().getAbsolutePath(), project.getBaseRom());
        trainerTextAssignmentFile = project.getDataPath() + File.separator;

        switch(project.getBaseRom())
        {
            case Platinum:
//                trainerTextBankId = 728;
                break;

            case HeartGold:
            case SoulSilver:
                messages = TextEditor.getBank(project, 728);
//                trainerTextBankId = 728;
                trainerTextAssignmentFile += "a" + File.separator + "0" + File.separator + "5" + File.separator + "7";
                break;
        }

        if(!new File(trainerTextAssignmentFile + "_").exists())
        {
            Narctowl narctowl = new Narctowl(true);
            narctowl.unpack(trainerTextAssignmentFile,trainerTextAssignmentFile + "_");
        }
        trainerTexts = trainerTextRetriever.getTrainerText(trainerTextAssignmentFile + "_" + File.separator + "0.bin");
        toDelete.add(new File(trainerTextAssignmentFile + "_"));

        thisTrainerTexts = new ArrayList<>();
        for(TrainerText trainerText : trainerTexts)
        {
            if(trainerText.getTrainerId() == trainerId)
            {
                thisTrainerTexts.add(trainerText);
            }
        }

        DefaultListModel<String> listModel= new DefaultListModel<>();


        for(TrainerText trainerText : thisTrainerTexts)
        {
            listModel.addElement("" + activationConditions.get(activationConditionToId.indexOf(trainerText.getCondition())));
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
        if(Project.isHGSS(project))
        {
            clearDirs(new File(trainerTextAssignmentFile + "_"));
        }
        else
        {
            clearDirs(new File(trainerTextAssignmentFile.substring(0, trainerTextAssignmentFile.length()-5)));
        }

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
        editPanel = new JPanel();
        activationLabel = new JLabel();
        activationComboBox = new JComboBox<>();
        textLabel = new JLabel();
        scrollPane2 = new JScrollPane();
        trainerTextArea = new JTextArea();
        saveButton = new JButton();

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
                "[grow]"));

            //---- addButton ----
            addButton.setText("Add");
            addRemovePanel.add(addButton, "cell 0 0,grow");

            //---- removeButton ----
            removeButton.setText("Remove");
            addRemovePanel.add(removeButton, "cell 0 1,grow");
        }
        contentPane.add(addRemovePanel, "cell 2 0,grow");

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
            editPanel.add(scrollPane2, "cell 0 2 2 1,grow");

            //---- saveButton ----
            saveButton.setText("Save");
            editPanel.add(saveButton, "cell 2 2,growy");
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
    private JPanel editPanel;
    private JLabel activationLabel;
    private JComboBox<String> activationComboBox;
    private JLabel textLabel;
    private JScrollPane scrollPane2;
    private JTextArea trainerTextArea;
    private JButton saveButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
