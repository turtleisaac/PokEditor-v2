/*
 * Created by JFormDesigner on Wed Dec 23 19:29:55 EST 2020
 */

package com.turtleisaac.pokeditor.gui.projects.newproject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

import com.jackhack96.dspre.nitro.rom.ROMUtils;
import com.jackhack96.jNdstool.main.JNdstool;
import com.turtleisaac.pokeditor.gui.MyFilter;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.ProjectWindow;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.console.ConsoleWindow;
import com.turtleisaac.pokeditor.project.Project;
import net.miginfocom.swing.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * @author turtleisaac
 */
public class NewProject extends JPanel {

    private JFrame parent;
    private JFrame frame;
    private boolean baseRomGood;
    private boolean directoryGood;

    private ConsoleWindow console;

    public NewProject(JFrame parent, ConsoleWindow console) {
        initComponents();

        this.parent= parent;
        baseRomGood= false;
        directoryGood= false;

        this.console= console;

        Dimension minimum= new Dimension();
        minimum.setSize(600,240);

        frame= new JFrame("Create Project");
        frame.setMinimumSize(minimum);
        frame.setPreferredSize(minimum);
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);

        DefaultFormatter formatter= new DefaultFormatter();
        formatter.setCommitsOnValidEdit(true);
        DefaultFormatterFactory factory= new DefaultFormatterFactory(formatter);
        projectNameTextField.setFormatterFactory(factory);
        projectNameTextField.setText("");
        projectLocationTextField.setFormatterFactory(factory);
        projectLocationTextField.setText("");
        baseRomTextField.setFormatterFactory(factory);
        baseRomTextField.setText("");

        projectLocationTextField.setText(System.getProperty("user.dir") + File.separator);
        createButton.setEnabled(false);




        projectNameTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                String locationText= projectLocationTextField.getText();

                String dirPath= locationText.substring(0,locationText.lastIndexOf(File.separator)+1);

                if(projectNameTextField.getText().length() > 0)
                {
                    if(locationText.substring(locationText.lastIndexOf(File.separator) + 1).equals(projectNameTextField.getText().substring(0,projectNameTextField.getText().length()-1)))
                        projectLocationTextField.setText(dirPath + projectNameTextField.getText());

                    if(directoryGood && baseRomGood)
                        createButton.setEnabled(true);
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String locationText= projectLocationTextField.getText();

                String dirPath= locationText.substring(0,locationText.lastIndexOf(File.separator)+1);

                if(projectNameTextField.getText().length() > 0)
                {
                    if(locationText.substring(locationText.lastIndexOf(File.separator) + 1).equals(projectNameTextField.getText().substring(0,projectNameTextField.getText().length()-1)))
                        projectLocationTextField.setText(dirPath + projectNameTextField.getText());

                    if(directoryGood && baseRomGood)
                        createButton.setEnabled(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                String locationText= projectLocationTextField.getText();

                String dirPath= locationText.substring(0,locationText.lastIndexOf(File.separator)+1);

                if(projectNameTextField.getText().length() > 0)
                {
                    if(locationText.substring(locationText.lastIndexOf(File.separator) + 1).equals(projectNameTextField.getText().substring(0,projectNameTextField.getText().length()-1)))
                        projectLocationTextField.setText(dirPath + projectNameTextField.getText());

                    if(directoryGood && baseRomGood)
                        createButton.setEnabled(true);
                }
            }
        });

        projectLocationTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                File directory= new File(projectLocationTextField.getText());

                if(projectLocationTextField.getText().equals(System.getProperty("user.dir")) || projectLocationTextField.getText().equals(System.getProperty("user.dir") + File.separator))
                {
                    directoryExistsLabel.setText("This is the directory containing PokEditor!");
                    createButton.setEnabled(false);
                    directoryGood= false;
                }
                else if(!directory.exists())
                {
                    directoryExistsLabel.setText("This directory doesn't exist! A new one will be created.");
                    directoryGood= true;
                }
                else if(directory.isDirectory() && Objects.requireNonNull(directory.listFiles()).length != 0)
                {
                    directoryExistsLabel.setText("This directory exists! It will be overwritten.");
                    directoryGood= true;
                }

                if(directoryGood && projectNameTextField.getText().length() > 0 && baseRomGood)
                    createButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                File directory= new File(projectLocationTextField.getText());

                if(projectLocationTextField.getText().equals(System.getProperty("user.dir")))
                {
                    directoryExistsLabel.setText("This is the directory containing PokEditor!");
                    createButton.setEnabled(false);
                    directoryGood= false;
                }
                else if(!directory.exists())
                {
                    directoryExistsLabel.setText("This directory doesn't exist! A new one will be created.");
                    directoryGood= true;
                }
                else if(directory.isDirectory() && Objects.requireNonNull(directory.listFiles()).length != 0)
                {
                    directoryExistsLabel.setText("This directory exists! It will be overwritten.");
                    directoryGood= true;
                }

                if(directoryGood && projectNameTextField.getText().length() > 0 && baseRomGood)
                    createButton.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                File directory= new File(projectLocationTextField.getText());

                if(projectLocationTextField.getText().equals(System.getProperty("user.dir")))
                {
                    directoryExistsLabel.setText("This is the directory containing PokEditor!");
                    createButton.setEnabled(false);
                    directoryGood= false;
                }
                else if(!directory.exists())
                {
                    directoryExistsLabel.setText("This directory doesn't exist! A new one will be created.");
                    directoryGood= true;
                }
                else if(directory.isDirectory() && Objects.requireNonNull(directory.listFiles()).length != 0)
                {
                    directoryExistsLabel.setText("This directory exists! It will be overwritten.");
                    directoryGood= true;
                }

                if(directoryGood && projectNameTextField.getText().length() > 0 && baseRomGood)
                    createButton.setEnabled(true);
            }
        });

        baseRomTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                String baseRom= baseRomTextField.getText();

                if(new File(baseRom).exists())
                {
                    String name;
                    String gameCode;
                    try
                    {
                        name= ROMUtils.getROMName(baseRom);
                        gameCode= ROMUtils.getROMCode(baseRom);
                        if(isGood(gameCode))
                        {
                            baseRomConfirmLabel.setText(name + ", " + gameCode);
                            baseRomGood= true;
                            baseRomConfirmLabel.setForeground(new Color(36, 168, 38, 255));
                        }
                        else
                        {
                            baseRomConfirmLabel.setText("Invalid Rom: " + name + ", " + gameCode);
                            baseRomConfirmLabel.setForeground(new Color(238, 21, 21, 255));
                        }



                        if(directoryGood && projectNameTextField.getText().length() > 0 && baseRomGood)
                            createButton.setEnabled(true);
                    }
                    catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                        baseRomConfirmLabel.setText("Not a Nintendo DS ROM");
                        baseRomGood= false;
                        createButton.setEnabled(false);
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                String baseRom= baseRomTextField.getText();

                if(new File(baseRom).exists())
                {
                    String name;
                    String gameCode;
                    try
                    {
                        name= ROMUtils.getROMName(baseRom);
                        gameCode= ROMUtils.getROMCode(baseRom);
                        if(isGood(gameCode))
                        {
                            baseRomConfirmLabel.setText(name + ", " + gameCode);
                            baseRomGood= true;
                            baseRomConfirmLabel.setForeground(new Color(36, 168, 38, 255));
                        }
                        else
                        {
                            baseRomConfirmLabel.setText("Invalid Rom: " + name + ", " + gameCode);
                            baseRomConfirmLabel.setForeground(new Color(238, 21, 21, 255));
                        }



                        if(directoryGood && projectNameTextField.getText().length() > 0 && baseRomGood)
                            createButton.setEnabled(true);
                    }
                    catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                        baseRomConfirmLabel.setText("Not a Nintendo DS ROM");
                        baseRomGood= false;
                        createButton.setEnabled(false);
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                String baseRom= baseRomTextField.getText();

                if(new File(baseRom).exists())
                {
                    String name;
                    String gameCode;
                    try
                    {
                        name= ROMUtils.getROMName(baseRom);
                        gameCode= ROMUtils.getROMCode(baseRom);
                        if(isGood(gameCode))
                        {
                            baseRomConfirmLabel.setText(name + ", " + gameCode);
                            baseRomGood= true;
                            baseRomConfirmLabel.setForeground(new Color(36, 168, 38, 255));
                        }
                        else
                        {
                            baseRomConfirmLabel.setText("Invalid Rom: " + name + ", " + gameCode);
                            baseRomConfirmLabel.setForeground(new Color(238, 21, 21, 255));
                        }



                        if(directoryGood && projectNameTextField.getText().length() > 0 && baseRomGood)
                            createButton.setEnabled(true);
                    }
                    catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                        baseRomConfirmLabel.setText("Not a Nintendo DS ROM");
                        baseRomGood= false;
                        createButton.setEnabled(false);
                    }
                }
            }
        });
    }

    private void createButtonActionPerformed(ActionEvent e)
    {
        File directory= new File(projectLocationTextField.getText().trim());
        if(!directory.exists())
        {
            if(!directory.mkdir())
            {
                System.err.println("Path: " + directory.getAbsolutePath());
                throw new RuntimeException("Unable to create directory. Check write perms");
            }
        }

        Project project= new Project(projectNameTextField.getText().trim(),projectLocationTextField.getText(),"pokeditor");
        project.setLanguage("ENG");
        project.setBaseRom(Project.parseBaseRom(baseRomConfirmLabel.getText().split(", ")[1]));
        project.setBaseRomGameCode(baseRomConfirmLabel.getText().split(", ")[1]);

        String backupPath= projectLocationTextField.getText().trim() + File.separator + "backups";
        File backupDir= new File(backupPath);
//
//        if(!backupDir.exists())
//        {
//            if(!backupDir.mkdir())
//            {
//                System.err.println("Path: " + backupPath);
//                throw new RuntimeException("Unable to create directory. Check write perms");
//            }
//        }

//        try
//        {
//            Files.copy(Paths.get(baseRomTextField.getText()),Paths.get(backupPath + File.separator + "original.nds"));
//        }
//        catch (IOException exception)
//        {
//            exception.printStackTrace();
//        }


        try
        {
            JNdstool.main("-x",baseRomTextField.getText(),projectLocationTextField.getText() + File.separator + projectNameTextField.getText());
        } catch (IOException exception)
        {
            exception.printStackTrace();
            return;
        }


        try
        {
            BufferedWriter writer= new BufferedWriter(new FileWriter(projectLocationTextField.getText() + File.separator + projectNameTextField.getText() + ".pokeditor"));
            writer.write(project.getXml());
            writer.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        try
        {
            ProjectWindow projectWindow= new ProjectWindow(projectLocationTextField.getText() + File.separator + projectNameTextField.getText() + ".pokeditor", parent,console);
            projectWindow.setLocationRelativeTo(parent);
            projectWindow.getTutorial().setLocationRelativeTo(projectWindow);
            projectWindow.getTutorial().setVisible(true);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        parent.setVisible(false);
        frame.dispose();
    }

    private void projectLocationButtonActionPerformed(ActionEvent e)
    {
        JFileChooser fc= new JFileChooser(System.getProperty("user.dir"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogTitle("Choose Project Directory");

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (e.getSource() == projectLocationButton) {
            int returnVal = fc.showOpenDialog(this.projectLocationButton);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                projectLocationTextField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private void baseRomButtonActionPerformed(ActionEvent e) {
        JFileChooser fc= new JFileChooser(System.getProperty("user.dir"));
        fc.setDialogTitle("Select ROM");
        fc.setAcceptAllFileFilterUsed(true);

        fc.setFileFilter(new MyFilter("Nintendo DS ROM",".nds",".srl"));

        if (e.getSource() == baseRomButton) {
            int returnVal = fc.showOpenDialog(this.baseRomButton);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                baseRomTextField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        projectNameLabel = new JLabel();
        projectNameTextField = new JFormattedTextField();
        projectLocationLabel = new JLabel();
        projectLocationTextField = new JFormattedTextField();
        projectLocationButton = new JButton();
        directoryExistsLabel = new JLabel();
        baseRomLabel = new JLabel();
        baseRomTextField = new JFormattedTextField();
        baseRomButton = new JButton();
        baseRomConfirmLabel = new JLabel();
        createButton = new JButton();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[grow,fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[grow]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- projectNameLabel ----
        projectNameLabel.setText("Project Name:");
        add(projectNameLabel, "cell 0 0");
        add(projectNameTextField, "cell 1 0");

        //---- projectLocationLabel ----
        projectLocationLabel.setText("Project Location:");
        add(projectLocationLabel, "cell 0 1");
        add(projectLocationTextField, "cell 1 1");

        //---- projectLocationButton ----
        projectLocationButton.setText("Choose");
        projectLocationButton.addActionListener(e -> projectLocationButtonActionPerformed(e));
        add(projectLocationButton, "cell 2 1,alignx center,growx 0");
        add(directoryExistsLabel, "cell 1 2");

        //---- baseRomLabel ----
        baseRomLabel.setText("Base ROM:");
        add(baseRomLabel, "cell 0 3");
        add(baseRomTextField, "cell 1 3");

        //---- baseRomButton ----
        baseRomButton.setText("Choose");
        baseRomButton.addActionListener(e -> baseRomButtonActionPerformed(e));
        add(baseRomButton, "cell 2 3");
        add(baseRomConfirmLabel, "cell 1 4");

        //---- createButton ----
        createButton.setText("Create");
        createButton.addActionListener(e -> createButtonActionPerformed(e));
        add(createButton, "cell 2 9");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel projectNameLabel;
    private JFormattedTextField projectNameTextField;
    private JLabel projectLocationLabel;
    private JFormattedTextField projectLocationTextField;
    private JButton projectLocationButton;
    private JLabel directoryExistsLabel;
    private JLabel baseRomLabel;
    private JFormattedTextField baseRomTextField;
    private JButton baseRomButton;
    private JLabel baseRomConfirmLabel;
    private JButton createButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    private boolean isGood(String gameCode)
    {
        return gameCode.startsWith("CPU")  || gameCode.startsWith("IPK") || gameCode.startsWith("IPG");

    }
}
