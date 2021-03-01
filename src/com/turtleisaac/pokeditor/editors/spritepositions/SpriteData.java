package com.turtleisaac.pokeditor.editors.spritepositions;

public interface SpriteData
{
    int getUnknownByte();
    int getMovement();
    byte[] getUnknownSection();
    byte getSpriteYOffset();
    byte getShadowXOffset();
    SpriteDataProcessor.ShadowType getShadowType();
}
