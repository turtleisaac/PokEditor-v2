package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.editor;

import com.turtleisaac.pokeditor.sheets.JohtoSheets;
import com.turtleisaac.pokeditor.sheets.SinnohSheets;

public interface Sheet
{
    public Object[][] getData();
    public SinnohSheets getSheetType();
    public JohtoSheets getAltSheetType();
}
