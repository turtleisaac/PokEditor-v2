/*
 * Created by JFormDesigner on Wed Jan 27 21:53:11 EST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.trainers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author turtleisaac
 */
public class SmogonFrame extends JFrame
{
    private TrainerPanel parent;
    public SmogonFrame(TrainerPanel parent) {
        initComponents();
        this.parent= parent;
    }

    private void applyButtonActionPerformed(ActionEvent e)
    {
        String[] arr= teamTextArea.getText().split("\n");
        parent.importSmogonButtonActionPerformed(arr);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        scrollPane1 = new JScrollPane();
        teamTextArea = new JTextArea();
        applyButton = new JButton();

        //======== this ========
        setTitle("Smogon Import/ Export");
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[grow]" +
            "[]"));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(teamTextArea);
        }
        contentPane.add(scrollPane1, "cell 0 0,grow");

        //---- applyButton ----
        applyButton.setText("Apply Changes");
        applyButton.addActionListener(e -> applyButtonActionPerformed(e));
        contentPane.add(applyButton, "cell 0 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JScrollPane scrollPane1;
    private JTextArea teamTextArea;
    private JButton applyButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void append(String str)
    {
        teamTextArea.append(str);
    }

    public void setText(String text)
    {
        teamTextArea.setText(text);
    }

    public String getText()
    {
        return teamTextArea.getText();
    }

    public void setParent(TrainerPanel parent)
    {
        this.parent= parent;
    }
}
