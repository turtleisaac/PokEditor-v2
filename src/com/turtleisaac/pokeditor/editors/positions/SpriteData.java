package com.turtleisaac.pokeditor.editors.positions;

public interface SpriteData
{
    int getMovement();
    byte[] getUnknownSection();
    byte getSpriteYOffset();
    byte getShadowXOffset();
    SpriteDataProcessor.ShadowType getShadowType();
}
