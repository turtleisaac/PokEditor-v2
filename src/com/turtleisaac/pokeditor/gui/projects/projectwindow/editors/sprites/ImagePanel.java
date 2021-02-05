package com.turtleisaac.pokeditor.gui.projects.projectwindow.editors.sprites;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel
{
    private Image image;

    public ImagePanel(Image image)
    {
        this.image= image;
    }

    @Override
    protected void printComponent(Graphics g)
    {
        super.printComponent(g);
        g.drawImage(image,0,0,this);
    }
}
