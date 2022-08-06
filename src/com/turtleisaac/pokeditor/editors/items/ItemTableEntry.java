package com.turtleisaac.pokeditor.editors.items;

public interface ItemTableEntry
{
    int getDataArchive(); // u16 arc_data
    int getGraphicsArchive(); // u16 arc_cgx
    int getPaletteArchive(); // u16 arc_pal
    int getPreviousID(); //u16 agb_id
}
