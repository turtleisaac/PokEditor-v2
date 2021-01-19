package com.turtleisaac.pokeditor.editors.encounters.johto;

public interface EncounterData
{
    int getFieldRate();
    int getSurfRate();
    int getSmashRate();
    int getOldRate();
    int getGoodRate();
    int getSuperRate();
    short getPadding();
//
    byte[] getFieldLevels();
    short[] getFieldMorning();
    short[] getFieldDay();
    short[] getFieldNight();
//
    short[] getHoenn();
    short[] getSinnoh();
//
    byte[] getSurf1MinMax();
    short getSurf1();
    byte[] getSurf2MinMax();
    short getSurf2();
    byte[] getSurf3MinMax();
    short getSurf3();
    byte[] getSurf4MinMax();
    short getSurf4();
    byte[] getSurf5MinMax();
    short getSurf5();
    short[] getSurfs();
    byte[] getSurfMins();
    byte[] getSurfMaxs();
//
    short getSmash1();
    byte[] getSmash1MinMax();
    short getSmash2();
    byte[] getSmash2MinMax();
    short[] getSmashes();
    byte[] getSmashMins();
    byte[] getSmashMaxs();
//
    byte[] getOld1MinMax();
    short getOld1();
    byte[] getOld2MinMax();
    short getOld2();
    byte[] getOld3MinMax();
    short getOld3();
    byte[] getOld4MinMax();
    short getOld4();
    byte[] getOld5MinMax();
    short getOld5();
    short[] getOlds();
    byte[] getOldMins();
    byte[] getOldMaxs();
//
    byte[] getGood1MinMax();
    short getGood1();
    byte[] getGood2MinMax();
    short getGood2();
    byte[] getGood3MinMax();
    short getGood3();
    byte[] getGood4MinMax();
    short getGood4();
    byte[] getGood5MinMax();
    short getGood5();
    short[] getGoods();
    byte[] getGoodMins();
    byte[] getGoodMaxs();
//
    byte[] getSuper1MinMax();
    short getSuper1();
    byte[] getSuper2MinMax();
    short getSuper2();
    byte[] getSuper3MinMax();
    short getSuper3();
    byte[] getSuper4MinMax();
    short getSuper4();
    byte[] getSuper5MinMax();
    short getSuper5();
    short[] getSupers();
    byte[] getSuperMins();
    byte[] getSuperMaxs();
//
    short getFieldSwarm();
    short getSurfSwarm();
    short getGoodSwarm();
    short getSuperSwarm();
}
