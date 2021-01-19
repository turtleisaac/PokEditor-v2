package com.turtleisaac.pokeditor.editors.encounters.johto.headbutt;

import java.util.ArrayList;
//
public interface HeadbuttData
{
    int getNumTrees();
    int getNumSpecialTrees();
//
    short[] getNormalEncounters();
    int[] getNormalMins();
    int[] getNormalMaxs();
//
    short[] getSpecialEncounters();
    int[] getSpecialMins();
    int[] getSpecialMaxs();
//
    ArrayList<TreeCoordinates> getNormalCoordinates();
    ArrayList<TreeCoordinates> getSpecialCoordinates();
}
