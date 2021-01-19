package com.turtleisaac.pokeditor.editors;

import java.io.Serializable;

public interface ExtractionShortcut extends Serializable
{
    String getGame();
    int getValue();
}
