package com.turtleisaac.pokeditor.utilities;

import java.io.File;

public enum TablePointer
{

    /**
     * Platinum
     */
    //CPUE
    PrizeMoney_CPUE("overlay" + File.separator + "overlay_0016.bin",0x816c,0x00),
    ClassGender_CPUE("arm9.bin",0x793B4,0x00)
    ;


    public final String file;
    public final long pointerOffset;
    public final long countOffset;

    TablePointer(String file, long pointerOffset, long countOffset)
    {
        this.file= file;
        this.pointerOffset = pointerOffset;
        this.countOffset= countOffset;
    }
}
