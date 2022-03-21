package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.editor.tables.editors;

import com.turtleisaac.pokeditor.gui.EditorComboBox;

import javax.swing.*;

public class EditorComboBoxEditor extends DefaultCellEditor
{
    public EditorComboBoxEditor(String[] items)
    {
        super(new EditorComboBox(items));
    }
}
