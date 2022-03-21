package com.turtleisaac.pokeditor.editors.overlays;

public interface OverlayData
{
    long getOverlayId();

    long getRamAddress();
    long getRamSize();

    long getBssSize();
    long getStaticInitStart();
    long getStaticInitEnd();
    long getFileId();
    int getReserved();
}
