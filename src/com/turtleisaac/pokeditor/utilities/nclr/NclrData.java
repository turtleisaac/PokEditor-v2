package com.turtleisaac.pokeditor.utilities.nclr;

import java.awt.*;

public interface NclrData
{
    Header getHeader();
    Palette getPalette();
    PaletteCountMap getPaletteCountMap();

    interface Header
    {
        String getMagicId();
        int getEndianness();
        int getConstant();
        long getFileSize();
        int getHeaderSize();
        int getNumSections();
    }

    /**
     * Section 1 - PLTT - Palette
     */
    interface Palette
    {
        String getMagicId();
        long getSectionSize();
        int getPaletteBitDepth();
        short getUnknown1();
        int getUnknown2();
        long getPaletteDataSize();
        long getColorStartOffset();
        long getNumColors();
        Color[][] getPalettes();
    }

    /**
     * Section 2 - PCMP - Palette Count Map
     */
    interface PaletteCountMap
    {
        String getMagicId();
        long getSectionSize();
        int getUnknown1();
        int getUnknown2(); //for some reason is always BEEF
        long getUnknown3();
        int[] getPaletteIDs();
    }

}
