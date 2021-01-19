package com.turtleisaac.pokeditor.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Setup extends JOptionPane
{
    JFrame frame;

    public Setup()
    {

        frame= new JFrame();
        frame.setTitle("Choose a Theme");

        Dimension dimension= new Dimension(300,300);
        frame.setPreferredSize(dimension);
        frame.setMinimumSize(dimension);

        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        showOptionDialog(null, "Choose a Theme", "Choose a Theme", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] {"Darcula","Dark","Light"}, JOptionPane.UNINITIALIZED_VALUE);


    }

    public void windowClosing(WindowEvent e) {
        int a=JOptionPane.showConfirmDialog(frame,"Are you sure?");
        if(a==JOptionPane.YES_OPTION){
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }


    public static void main(String[] args) {
        new Setup();
    }
}
