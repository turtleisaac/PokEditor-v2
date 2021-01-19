package com.turtleisaac.pokeditor.editors.moves.gen4;

public interface MoveDataGen4
{
    int getEffect();
    short getCategory();
    short getPower();

    short getType();
    short getAccuracy();
    short getPp();
    short getAdditionalEffect();

    int getRange();
    byte getPriority();
    short getFlag();

    short getContestEffect();
    short getContestType();
}
