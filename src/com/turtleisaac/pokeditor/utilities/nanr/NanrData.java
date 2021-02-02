package com.turtleisaac.pokeditor.utilities.nanr;

public interface NanrData
{

    interface Header
    {
        byte[] getMagicId();
        int getEndianness();
        int getConstant();
        long getFileSize();
        int getHeaderSize();
        int getNumSections();
    }


    /**
     * Section 1 - ABNK - Animation Bank
     */

    //Magic ID
    long getSectionSize();
    int getNumAnimations();
    int getFrameCount();
    int getUnknownConstant();
    int getUnknown1();
    int getUnknown2();
    //8 Bytes Padding
    //Animation Blocks ****

    /**
     * Section 2
     */

    long getNumFrames();
    int getUnknown3();
    int getUnknown4();
    int getUnknown5();
    int getFirstFrameOffset(); //Relative to this section, not absolute
    //Frame Blocks ****

    /**
     * Section 3
     */

    long getUnknownOffset();
    int getFrameWidth();
    byte[] getUnknown6();
    //Frame Data ****
}
