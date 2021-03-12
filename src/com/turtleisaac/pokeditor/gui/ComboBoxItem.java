package com.turtleisaac.pokeditor.gui;

public class ComboBoxItem
{
    private String str;

    public ComboBoxItem(String str)
    {
        this.str= str;
    }

    public ComboBoxItem(int num)
    {
        str= "" + num;
    }

    @Override
    public String toString()
    {
        return str;
    }
}
