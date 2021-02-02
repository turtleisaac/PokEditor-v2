package com.turtleisaac.pokeditor.utilities.nclr;

import com.turtleisaac.pokeditor.framework.Buffer;

import java.awt.*;

public class NclrReader
{
    public static NclrData readFile(String file)
    {
        Buffer buffer= new Buffer(file);

        //Header
        String headerID= buffer.readString(4);
        int endianness= buffer.readUInt16();
        int constant= buffer.readUInt16();
        long fileSize= buffer.readUInt32();
        int headerSize= buffer.readUInt16();
        int numSections= buffer.readUInt16();

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
                        return null;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return 0;
                    }

                    @Override
                    public int getPaletteBitDepth()
                    {
                        return 0;
                    }

                    @Override
                    public short getUnknown1()
                    {
                        return 0;
                    }

                    @Override
                    public int getUnknown2()
                    {
                        return 0;
                    }

                    @Override
                    public long getPaletteDataSize()
                    {
                        return 0;
                    }

                    @Override
                    public long getColorStartOffset()
                    {
                        return 0;
                    }

                    @Override
                    public long getNumColors()
                    {
                        return 0;
                    }

                    @Override
                    public Color[][] getPalettes()
                    {
                        return new Color[0][];
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
                        return null;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return 0;
                    }

                    @Override
                    public int getUnknown1()
                    {
                        return 0;
                    }

                    @Override
                    public int getUnknown2()
                    {
                        return 0;
                    }

                    @Override
                    public long getUnknown3()
                    {
                        return 0;
                    }

                    @Override
                    public int[] getPaletteIDs()
                    {
                        return new int[0];
                    }
                };
            }
        };
    }
}
