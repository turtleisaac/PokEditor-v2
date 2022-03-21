package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.editor.tables.renderers;

import com.turtleisaac.pokeditor.gui.EditorComboBox;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class EditorComboBoxRenderer extends EditorComboBox implements TableCellRenderer
{

    public EditorComboBoxRenderer(String[] items)
    {
        super(items);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setShowGrid(true);

        if (isSelected)
        {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        }
        else
        {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelectedItem(value);
        return this;
    }
}
