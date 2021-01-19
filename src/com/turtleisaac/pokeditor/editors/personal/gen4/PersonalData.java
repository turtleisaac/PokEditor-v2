package com.turtleisaac.pokeditor.editors.personal.gen4;

public interface PersonalData
{
    int getNum();
    int getHP();
    int getAtk();
    int getDef();
    int getSpe();
    int getSpAtk();
    int getSpDef();
    int getType1();
    int getType2();
    int getCatchRate();
    int getBaseExp();

    int getHpEv();
    int getSpeEv();
    int getAtkEv();
    int getDefEv();
    int getSpAtkEv();
    int getSpDefEv();
    int getPadding();

    int getUncommonItem();
    int getRareItem();
    int getGenderRatio();
    int getHatchMultiplier();
    int getBaseHappiness();
    int getExpRate();
    int getEggGroup1();
    int getEggGroup2();
    int getAbility1();
    int getAbility2();
    int getRunChance();
    int getDexColor();

    boolean getTm(int idx);
}
