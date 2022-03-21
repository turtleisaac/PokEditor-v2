package com.turtleisaac.pokeditor.editors.spritepositions;

public interface SpriteData
{
    int getUnknownByte();
    int getMovement();
    byte[] getUnknownSection1();
    int getBackMovement();
    byte[] getUnknownSection2();
    byte getSpriteYOffset();
    byte getShadowXOffset();
    SpriteDataProcessor.ShadowType getShadowType();
}
