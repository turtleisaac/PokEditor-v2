package com.turtleisaac.pokeditor.utilities.images.formats.nclr;

import com.turtleisaac.pokeditor.utilities.images.ColorFormat;

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
        ColorFormat getDepth();
        short getUnknown1();
        int getUnknown2();
        long getPaletteLength();
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
        short getUnknown1();
        short getUnknown2(); //for some reason is always BEEF
        int getUnknown3();
        int getFirstPaletteNum();
    }

}
