package com.turtleisaac.pokeditor.editors.moves.gen5;

public interface MoveDataGen5
{
    short getType();
    short getEffectCategory();
    short getCategory();
    short getPower();

    short getAccuracy();
    short getPp();
    short getPriority();
    short getMinHits();
    short getMaxHits();

    int getResultEffect();
    short getEffectChance();
    short getStatus();
    short getMinTurns();
    short getMaxTurns();

    short getCrit();
    short getFlinch();
    int getEffect();
    short getRecoil();
    short getHealing();
    short getTarget();

    short getStat1();
    short getStat2();
    short getStat3();

    short getMagnitude1();
    short getMagnitude2();
    short getMagnitude3();

    short getStatChance1();
    short getStatChance2();
    short getStatChance3();
    int getFlag();

    boolean getContact();
    boolean getChargingTurn();
    boolean getRechargeTurn();
    boolean getBlockedProtect();
    boolean getMagicCoat();
    boolean getSnatch();
    boolean getMirrorMove();
    boolean getPunchingMove();
    boolean getSoundMove();
    boolean getGravity();
    boolean getMeltTarget();
    boolean getNonAdjacent();
    boolean getHealingMove();
    boolean getSubstituteHit();
}
