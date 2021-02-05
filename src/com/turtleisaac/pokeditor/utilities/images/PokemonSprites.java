package com.turtleisaac.pokeditor.utilities.images;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface PokemonSprites
{
    BufferedImage[] getFemaleBack();
    BufferedImage[] getShinyFemaleBack();

    BufferedImage[] getMaleBack();
    BufferedImage[] getShinyMaleBack();

    BufferedImage[] getFemaleFront();
    BufferedImage[] getShinyFemaleFront();

    BufferedImage[] getMaleFront();
    BufferedImage[] getShinyMaleFront();


    Color[] getPalette();
    Color[] getShinyPalette();
}
