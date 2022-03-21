/*
 * Created by JFormDesigner on Wed Dec 15 17:55:23 CST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.editor;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.editor.tables.PersonalTable;
import net.miginfocom.swing.*;

import java.awt.*;

/**
 * @author turtleisaac
 */
public class SheetEditorFrame extends JFrame {
    public SheetEditorFrame(Sheet... sheets) {
        FlatLightLaf.install();
        initComponents();

        for(Sheet s : sheets)
        {
            if(s.getSheetType() != null)
            {
                switch(s.getSheetType())
                {
                    case Personal:
                        tabbedPane1.addTab("Personal", new DefaultSheetPanel(new PersonalTable(s.getData())));
                }
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menu2 = new JMenu();
        menu3 = new JMenu();
        tabbedPane1 = new JTabbedPane();

        //======== this ========
        setTitle("Sheet Editor");
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[grow,fill]"));

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("File");
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("Edit");
            }
            menuBar1.add(menu2);

            //======== menu3 ========
            {
                menu3.setText("Help");
            }
            menuBar1.add(menu3);
        }
        setJMenuBar(menuBar1);
        contentPane.add(tabbedPane1, "");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenu menu2;
    private JMenu menu3;
    private JTabbedPane tabbedPane1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
