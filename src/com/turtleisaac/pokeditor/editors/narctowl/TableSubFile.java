package com.turtleisaac.pokeditor.editors.narctowl;

public interface TableSubFile
{
    int getStartingOffset();
    int getEndingOffset();
    byte[] getFileContents();
    int length();

}
