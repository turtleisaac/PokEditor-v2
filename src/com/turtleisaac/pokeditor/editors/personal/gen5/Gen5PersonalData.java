package com.turtleisaac.pokeditor.editors.personal.gen5;

public interface Gen5PersonalData
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
    int getStage();

    int getHpEv();
    int getSpeEv();
    int getAtkEv();
    int getDefEv();
    int getSpAtkEv();
    int getSpDefEv();
    int getPadding();

    int getItem1();
    int getItem2();
    int getItem3();
    int getGenderRatio();
    int getHatchMultiplier();
    int getBaseHappiness();
    int getExpRate();
    int getEggGroup1();
    int getEggGroup2();
    int getAbility1();
    int getAbility2();
    int getAbility3();
    int getRunChance();
    int getFormID();
    int getForm();
    int getNumForms();
    int getDexColor();
    int getBaseExp();
    int getHeight();
    int getWeight();

    boolean getTm(int idx);
}
