package com.turtleisaac.pokeditor.utilities;

import java.io.File;

public enum TablePointer
{

    /**
     * Platinum
     */
    //CPUE
    PrizeMoney_CPUE("overlay" + File.separator + "overlay_0016.bin",0x816c,0x00,0), //Trainer Class Prize Money Table
    ClassGender_CPUE("arm9.bin",0x793B4,0x00,0) //Trainer Class Gender Table
    ;


    public final String file;
    public final long pointerOffset;
    public final long countOffset;
    public final int countPointerLength;

    /**
     * A representation of the file where a pointer to a table is located, the offset of the pointer, the offset representing the number of entries to read, and the number of bytes to read in order to obtain the number of entries to read
     * @param file The file where the pointer is located
     * @param pointerOffset The offset where the pointer to the table is located
     * @param countOffset The offset where the number of entries to read is located
     */
    TablePointer(String file, long pointerOffset, long countOffset, int countPointerLength)
    {
        this.file= file;
        this.pointerOffset = pointerOffset;
        this.countOffset= countOffset;
        this.countPointerLength= countPointerLength;
    }
}
