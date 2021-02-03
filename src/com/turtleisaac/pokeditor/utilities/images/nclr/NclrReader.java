package com.turtleisaac.pokeditor.utilities.images.nclr;

import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.utilities.images.ColorFormat;
import com.turtleisaac.pokeditor.utilities.images.ImageActions;

import java.awt.*;
import java.io.IOException;

public class NclrReader
{
    public static NclrData readFile(String file) throws IOException
    {
        Buffer buffer= new Buffer(file);

        //Header
        String headerID= buffer.readString(4);
        int endianness= buffer.readUInt16();
        int constant= buffer.readUInt16();
        long fileSize= buffer.readUInt32();
        int headerSize= buffer.readUInt16();
        int numSections= buffer.readUInt16();

        //Palette
        String paletteId= buffer.readString(4);
        long paletteSectionSize= buffer.readUInt32();
        ColorFormat depth= ColorFormat.getColorFormat(buffer.readUInt16());
        short paletteUnknown1= buffer.readShort();
        int paletteUnknown2= buffer.readInt();
        long paletteLength= buffer.readUInt32();

        if(paletteLength == 0 || paletteLength > paletteSectionSize)
            paletteLength= paletteSectionSize - 0x18;

        long colorStartOffset= buffer.readUInt32();
        long numColors= depth == ColorFormat.colors16 ? 0x10 : 0x100;
        if(paletteLength / 2 < numColors)
            numColors= paletteLength / 2;
        Color[][] palettes= new Color[(int) (paletteLength / (numColors*2))][];

        buffer.skipTo(0x18 + colorStartOffset);
        for(int i= 0; i < palettes.length; i++)
            palettes[i]= ImageActions.BGR555ToColor(buffer.readBytes((int) (numColors*2)));


        //Pallete Count Map
        String countId= "";
        long countSectionSize= 0;
        short countUnknown1= 0;
        short countUnknown2= 0;
        int countUnknown3= 0;
        int firstPaletteNum= 0;
        if(headerSize == 2)
        {
            countId= buffer.readString(4);
            countSectionSize= buffer.readUInt32();
            countUnknown1= buffer.readShort();
            countUnknown2= buffer.readShort();
            countUnknown3= buffer.readInt();
            firstPaletteNum= buffer.readUInt16();
        }

        buffer.close();


        long finalPaletteLength = paletteLength;
        long finalNumColors = numColors;
        String finalCountId = countId;
        long finalCountSectionSize = countSectionSize;
        short finalCountUnknown = countUnknown1;
        short finalCountUnknown1 = countUnknown2;
        int finalCountUnknown2 = countUnknown3;
        int finalFirstPaletteNum = firstPaletteNum;
        return new NclrData()
        {
            @Override
            public Header getHeader()
            {
                return new Header()
                {
                    @Override
                    public String getMagicId()
                    {
                        return headerID;
                    }

                    @Override
                    public int getEndianness()
                    {
                        return endianness;
                    }

                    @Override
                    public int getConstant()
                    {
                        return constant;
                    }

                    @Override
                    public long getFileSize()
                    {
                        return fileSize;
                    }

                    @Override
                    public int getHeaderSize()
                    {
                        return headerSize;
                    }

                    @Override
                    public int getNumSections()
                    {
                        return numSections;
                    }
                };
            }

            @Override
            public Palette getPalette()
            {
                return new Palette()
                {
                    @Override
                    public String getMagicId()
                    {
                        return paletteId;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return paletteSectionSize;
                    }

                    @Override
                    public ColorFormat getDepth()
                    {
                        return depth;
                    }

                    @Override
                    public short getUnknown1()
                    {
                        return paletteUnknown1;
                    }

                    @Override
                    public int getUnknown2()
                    {
                        return paletteUnknown2;
                    }

                    @Override
                    public long getPaletteLength()
                    {
                        return finalPaletteLength;
                    }

                    @Override
                    public long getColorStartOffset()
                    {
                        return colorStartOffset;
                    }

                    @Override
                    public long getNumColors()
                    {
                        return finalNumColors;
                    }

                    @Override
                    public Color[][] getPalettes()
                    {
                        return palettes;
                    }
                };
            }

            @Override
            public PaletteCountMap getPaletteCountMap()
            {
                return new PaletteCountMap()
                {
                    @Override
                    public String getMagicId()
                    {
                        return finalCountId;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return finalCountSectionSize;
                    }

                    @Override
                    public short getUnknown1()
                    {
                        return finalCountUnknown;
                    }

                    @Override
                    public short getUnknown2()
                    {
                        return finalCountUnknown1;
                    }

                    @Override
                    public int getUnknown3()
                    {
                        return finalCountUnknown2;
                    }

                    @Override
                    public int getFirstPaletteNum()
                    {
                        return finalFirstPaletteNum;
                    }
                };
            }
        };
    }
}
