package com.turtleisaac.pokeditor.utilities.images;

public enum ColorFormat
{
    A3I5(1),           // 8 bits -> 0-4: index; 5-7: alpha
    colors4(2),        // 2 bits for 4 colors
    colors16(3),       // 4 bits for 16 colors
    colors256(4),      // 8 bits for 256 colors
    texel4x4(5),       // 32 bits, 2 bits per Texel (only in textures)
    A5I3(6),           // 8 bits -> 0-2: index; 3-7: alpha
    direct(7),         // 16 bits, color with BGR555 encoding
    colors2(8),        // 1 bit for 2 colors
    BGRA32(9),         // 32 bits -> ABGR
    A4I4(10),
    ABGR32(11);

    public final int value;

    ColorFormat(int value)
    {
        this.value= value;
    }

    public static ColorFormat getColorFormat(int value)
    {
        switch (value)
        {
            case 1: return A3I5;
            case 2: return colors4;
            case 3: return colors16;
            case 4: return colors256;
            case 5: return texel4x4;
            case 6: return A5I3;
            case 7: return direct;
            case 8: return colors2;
            case 9: return BGRA32;
            case 10: return A4I4;
            case 11: return ABGR32;
            default:
                throw new RuntimeException("Invalid color format: " + value);
        }
    }
}
