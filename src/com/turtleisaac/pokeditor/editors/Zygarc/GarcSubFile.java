package com.turtleisaac.pokeditor.editors.Zygarc;

public interface GarcSubFile
{
    long getOtafEntry();

    long getBits();
    long getStartingOffset();
    long getEndingOffset();
    long getLength();
    long getTrashBytes();
    String getName();
}
