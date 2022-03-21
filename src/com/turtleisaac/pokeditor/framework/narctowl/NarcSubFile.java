package com.turtleisaac.pokeditor.framework.narctowl;

public interface NarcSubFile
{
    long getStartingOffset();
    long getEndingOffset();
    long getTrashBytes();
    String getName();
}
