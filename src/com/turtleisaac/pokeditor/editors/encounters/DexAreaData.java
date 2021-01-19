package com.turtleisaac.pokeditor.editors.encounters;

import java.util.ArrayList;

public interface DexAreaData
{
    ArrayList<Integer> getMorningIndoors();
    ArrayList<Integer> getDayIndoors();
    ArrayList<Integer> getNightIndoors();

    ArrayList<Integer> getMorningOutdoors();
    ArrayList<Integer> getDayOutdoors();
    ArrayList<Integer> getNightOutdoors();

    ArrayList<Integer> getHeadbuttIndoors();
    ArrayList<Integer> getHeadbuttOutdoors();
}
