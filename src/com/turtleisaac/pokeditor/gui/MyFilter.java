package com.turtleisaac.pokeditor.gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MyFilter extends FileFilter
{
    private String[] extensions;
    private String description;

    public MyFilter(String description, String... extensions)
    {
        this.extensions= extensions;
        this.description= description;
    }

    public boolean accept(File f)
    {
        for(String str : extensions)
        {
            if (f.getName().endsWith(str))
                return true;
            else if (f.getName().endsWith(str))
                return true;
            else if (f.isDirectory())
                return true;
        }
        return false;
    }

    @Override
    public String getDescription()
    {
        StringBuilder extensions= new StringBuilder(" (");
        String extension;

        for(int i= 0; i < this.extensions.length; i++)
        {
            extension= this.extensions[i];
            extensions.append("*").append(extension);
            if(i != this.extensions.length-1)
                extensions.append(", ");
        }
        extensions.append(")");

        return description + extensions.toString();
    }
}
