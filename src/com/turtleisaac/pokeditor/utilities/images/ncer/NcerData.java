package com.turtleisaac.pokeditor.utilities.images.ncer;

public interface NcerData
{

    Header getHeader();
    CharacterData getCharacterData();


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
     * Section 1 - CEBK - Cell Bank
     */

    interface CharacterData
    {
        byte[] getMagicID();
        long getSectionSize();
        long getNumImages();
        int getUnknown1();
        long getBoundarySize();
        //16 Bytes Padding
        byte[][] getCellTable(); //8 bytes per entry

        int getNumCells();
        int getUnkown2();
        long getCellDataOffset(); //Relative to this section, not absolute
        byte[][] getCellData(); //(Starts at Number of Cells * 8 | each cell is made up of 6 bytes)

        //See https://www.romhacking.net/documents/%5B469%5Dnds_formats.htm for info on table
    }




}
