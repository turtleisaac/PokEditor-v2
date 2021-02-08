package com.turtleisaac.pokeditor.utilities.images.ncer;

public interface NcerData
{

    Header getHeader();
    CellBank getCellBank();
    Label getLabel();
    UEXT getUEXT();



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
     * Section 1 - CEBK - Cell Bank
     */
    interface CellBank
    {
        String getMagicId();
        long getSectionSize();
        int getNumBanks();
        int getBankTypes(); //type of banks, 0 or 1
        long getBankDataOffset();
        long getBlockSize();
        long getPartitionDataOffset();
        long getUnused(); //Unused pointers to LABL and UEXT sections
        Bank[] getBanks();

        long getMaxPartitionSize();
        long getFirstPartitionDataOffset();

    }

    /**
     * Section 2 - LABL - Label
     */
    interface Label
    {
        String getMagicId();
        long getSectionSize();
        long[] getOffsets();
        String[] getNames();
    }

    /**
     * Section 3 - UEXT - UEXT
     */
    interface UEXT
    {
        String getMagicId();
        long getSectionSize();
        long getUnknown();
    }


    /**
     * Bank Structure
     */
    interface Bank
    {
        int getNumCells();
        int getReadOnlyCellInfo();
        long getCellOffset();
        long getPartitionOffset();
        long getPartitionSize();
        OAM[] getOAMs();

        //Extended mode
        short getXMax();
        short getYMax();
        short getXMin();
        short getYMin();
    }


    /**
     * OAM Structure
     */
    interface OAM
    {
        Obj0 getObj0();
        Obj1 getObj1();
        Obj2 getObj2();

        int getWidth();
        int getHeight();
        int getCellNumber();
    }

    interface Obj0 // 16 bits
    {
        int getYOffset(); //bits 0-7 (signed)
        boolean getRsFlag(); //bit 8 (rotation/ scale flag)
        byte getObjDisable(); //bit 9 (if r/s == 0)
        byte getDoubleSize(); //bit 10 (if r/s != 0)
        byte getObjMode(); //bits 10-11 (0 = normal; 1 = semi-transparent; 2 = window; 3 = invalid)
        byte getMosaicFlag(); //bit 12
        byte getDepth(); //bit 13 (0 = 4bit; 1 = 8bit)
        byte getShape(); //bits 14-15 (0 = square; 1 = horizontal; 2 = vertical; 3 = invalid)
    }

    interface Obj1 //16 bits
    {
        long getXOffset(); //bits 0-8 (unsigned)

        //If r/s == 0
        byte getUnused(); //bits 9-11
        byte getFlipX(); //bit 12
        byte getFlipY(); //bit 13

        //If r/s != 0
        byte getSelectParameter(); //bit 9-13 (parameter selection)

        byte getSize(); //bits 14-15
    }

    interface Obj2 //16 bits
    {
        long getTileOffset(); //bits 0-9
        byte getPriority(); //bits 10-11
        byte getIndexPalette(); //bits 12-15
    }

}
