package com.turtleisaac.pokeditor.utilities.ncgr;

public enum TileFormat
{
    Horizontal(0),
    Linear(1)
    ;

    public final int value;

    TileFormat(int value)
    {
        this.value= value;
    }
}
