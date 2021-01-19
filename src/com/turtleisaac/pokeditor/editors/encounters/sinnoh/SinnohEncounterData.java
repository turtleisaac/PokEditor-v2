package com.turtleisaac.pokeditor.editors.encounters.sinnoh;

public interface SinnohEncounterData
{
    int getFieldRate();
    int[] getFieldLevels();
    int[] getFieldEncounters();

    int[] getSwarmEncounters();
    int[] getDayEncounters();
    int[] getNightEncounters();

    int[] getRadarEncounters();

    int[] getRuby();
    int[] getSapphire();
    int[] getEmerald();
    int[] getFireRed();
    int[] getLeafGreen();

    int getSurfRate();
    int[] getSurfMaxs();
    int[] getSurfMins();
    int[] getSurfEncounters();

    int getOldRate();
    int[] getOldMaxs();
    int[] getOldMins();
    int[] getOldEncounters();

    int getGoodRate();
    int[] getGoodMaxs();
    int[] getGoodMins();
    int[] getGoodEncounters();

    int getSuperRate();
    int[] getSuperMaxs();
    int[] getSuperMins();
    int[] getSuperEncounters();
}
