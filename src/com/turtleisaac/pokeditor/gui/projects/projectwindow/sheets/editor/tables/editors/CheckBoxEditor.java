package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.editor.tables.editors;

import com.turtleisaac.pokeditor.gui.EditorComboBox;

import javax.swing.*;

public class CheckBoxEditor extends DefaultCellEditor
{
    public CheckBoxEditor()
    {
        super(new JCheckBox());
    }
}
