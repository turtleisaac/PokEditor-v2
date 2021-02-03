/*
 * Created by JFormDesigner on Tue Feb 02 16:47:18 EST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.editors.sprites;

import javax.swing.*;

import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.images.ImageActions;
import com.turtleisaac.pokeditor.utilities.images.ImageBase;
import com.turtleisaac.pokeditor.utilities.images.ncgr.NcgrData;
import com.turtleisaac.pokeditor.utilities.images.ncgr.NcgrReader;
import com.turtleisaac.pokeditor.utilities.images.nclr.NclrData;
import com.turtleisaac.pokeditor.utilities.images.nclr.NclrReader;
import net.miginfocom.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Truck
 */
public class SpritePanel extends JPanel
{
    private Project project;
    public SpritePanel()
    {
        initComponents();

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        label2 = new JLabel();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]"));

        //---- label1 ----
        label1.setText("text");
        add(label1, "cell 0 0");

        //---- label2 ----
        label2.setText("text");
        add(label2, "cell 0 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel label2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void setProject(Project project) throws IOException
    {
        this.project= project;
        ImageBase imageBase= new ImageBase(project,"/poketool/trgra/trfgra/115.ncgr","/poketool/trgra/trfgra/116.nclr");
        label1.setIcon(new ImageIcon(imageBase.Get_Image()));
        label2.setIcon(new ImageIcon(imageBase.Get_Palette()));
    }
}
