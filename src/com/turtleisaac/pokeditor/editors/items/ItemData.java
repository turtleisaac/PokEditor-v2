package com.turtleisaac.pokeditor.editors.items;

public interface ItemData
{
    int getPrice();
    short getEffect();
    short getPower();
    short getPluck();
    short getFlingEffect();
    short getFlingPower();
    short getNaturalPower();

    byte getNaturalType();
    boolean getDiscardUnable();
    boolean getAbleRegister();
    byte getFieldPocketNumber();
    byte getBattlePocketNumber();

    short getFieldFunction();
    short getBattleFunction();
    short getWorkType();

    boolean getSlpRecovery();
    boolean getPsnRecovery();
    boolean getBrnRecovery();
    boolean getFrzRecovery();
    boolean getPrlzRecovery();
    boolean getCnfsRecovery();
    boolean getAttractRecovery();
    boolean getAbilityGuard();

    boolean getRevive();
    boolean getReviveAll();
    boolean getLevelUp();
    boolean getEvolution();
    byte getAtkUp();

    byte getDefUp();
    byte getSpAtkUp();

    byte getSpDefUp();
    byte getSpeedUp();

    byte getAccuracyUp();
    byte getCritUp();
    boolean getPpUp();
    boolean getPpUp3();

    boolean getPpRecovery();
    boolean getPpFullRecovery();
    boolean getHpRecovery();
    boolean getHpEv();
    boolean getAtkEv();
    boolean getDefEv();
    boolean getSpeedEv();
    boolean getSpAtkEv();

    boolean getSpDefEv();
    boolean getFriendExp1();
    boolean getFriendExp2();
    boolean getFriendExp3();

    byte getHpEvChange();
    byte getAtkEvChange();
    byte getDefEvChange();
    byte getSpeedEvChange();
    byte getSpAtkEvChange();
    byte getSpDefEvChange();
    short getHpRecoveryAmount();
    short getPpRecoveryAmount();
    byte getFriendExpChange1();
    byte getFriendExpChange2();
    byte getFriendExpChange3();
}
