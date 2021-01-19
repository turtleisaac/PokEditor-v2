package com.turtleisaac.pokeditor.editors.narctowl;

public interface NarcSubFile
{
    int getStartingOffset();
    int getEndingOffset();
    int getTrashBytes();
    String getName();
}
