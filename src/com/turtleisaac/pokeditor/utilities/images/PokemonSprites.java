package com.turtleisaac.pokeditor.utilities.images;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface PokemonSprites
{
    SpriteImage[] getFemaleBack();
    SpriteImage[] getShinyFemaleBack();

    SpriteImage[] getMaleBack();
    SpriteImage[] getShinyMaleBack();

    SpriteImage[] getFemaleFront();
    SpriteImage[] getShinyFemaleFront();

    SpriteImage[] getMaleFront();
    SpriteImage[] getShinyMaleFront();


    Color[] getPalette();
    Color[] getShinyPalette();
}
