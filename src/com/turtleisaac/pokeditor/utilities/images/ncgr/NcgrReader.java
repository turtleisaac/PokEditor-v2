package com.turtleisaac.pokeditor.utilities.images.ncgr;

import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.utilities.images.ColorFormat;
import com.turtleisaac.pokeditor.utilities.images.ImageBase;
import com.turtleisaac.pokeditor.utilities.images.TileFormat;

import java.io.IOException;

public class NcgrReader
{
    public static NcgrData readFile(String file, ImageBase base) throws IOException
    {
        Buffer buffer= new Buffer(file);

        //Header
        String headerID= buffer.readString(4);
        int endianness= buffer.readUInt16();
        int constant= buffer.readUInt16();
        long fileSize= buffer.readUInt32();
        int headerSize= buffer.readUInt16();
        int numSections= buffer.readUInt16();

        //Character Data
        String dataID= buffer.readString(4);
        long dataSectionSize= buffer.readUInt32();
        int numTilesY= buffer.readUInt16();
        int numTilesX= buffer.readUInt16();
        ColorFormat depth= ColorFormat.getColorFormat(buffer.readInt());
        short dataUnknown1= buffer.readShort();
        short dataUnknown2= buffer.readShort();
        TileFormat tileOrder= (buffer.readInt() & 0xff) == 0 ? TileFormat.Horizontal : TileFormat.Linear;
        long tileDataSize= buffer.readUInt32();
        int dataUnknown3= buffer.readInt();
        byte[] data= buffer.readBytes((int) tileDataSize);

        if(numTilesX != 0xffff)
        {
            numTilesX*= 8;
            numTilesY*= 8;
        }

        //Character Position
        String positionID= "";
        long positionSectionSize= 0;
        int positionUnknown1= 0;
        int characterSize= 0;
        int numCharacters= 0;
        if(numSections == 2 && buffer.getPosition() < buffer.getLength())
        {
            System.out.println("CPOS exists");
            positionID= buffer.readString(4);
            positionSectionSize= buffer.readUInt32();
            positionUnknown1= buffer.readInt();
            characterSize= buffer.readUInt16();
            numCharacters= buffer.readUInt16();
        }
        buffer.close();
        base.setTiles(data,numTilesX,numTilesY,depth,tileOrder,true);

        //Establishing final variables
        int finalNumTilesY = numTilesY;
        int finalNumTilesX = numTilesX;
        String finalPositionID = positionID;
        long finalPositionSectionSize = positionSectionSize;
        int finalPositionUnknown = positionUnknown1;
        int finalCharacterSize = characterSize;
        int finalNumCharacters = numCharacters;
        return new NcgrData()
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
            public CharacterData getCharacterData()
            {
                return new CharacterData()
                {
                    @Override
                    public String getMagicId()
                    {
                        return dataID;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return dataSectionSize;
                    }

                    @Override
                    public int getNumTiles_Y()
                    {
                        return finalNumTilesY;
                    }

                    @Override
                    public int getNumTiles_X()
                    {
                        return finalNumTilesX;
                    }

                    @Override
                    public ColorFormat getDepth()
                    {
                        return depth;
                    }

                    @Override
                    public short getUnknown1()
                    {
                        return dataUnknown1;
                    }

                    @Override
                    public short getUnknown2()
                    {
                        return dataUnknown2;
                    }

                    @Override
                    public TileFormat getTileOrder()
                    {
                        return tileOrder;
                    }

                    @Override
                    public long getTileDataSize()
                    {
                        return tileDataSize;
                    }

                    @Override
                    public int getUnknown3()
                    {
                        return dataUnknown3;
                    }

                    @Override
                    public byte[] getData()
                    {
                        return data;
                    }
                };
            }

            @Override
            public CharacterPosition getCharacterPosition()
            {
                return new CharacterPosition()
                {
                    @Override
                    public String getMagicId()
                    {
                        return finalPositionID;
                    }

                    @Override
                    public long getSectionSize()
                    {
                        return finalPositionSectionSize;
                    }

                    @Override
                    public int getUnknown1()
                    {
                        return finalPositionUnknown;
                    }

                    @Override
                    public int getCharacterSize()
                    {
                        return finalCharacterSize;
                    }

                    @Override
                    public int getNumCharacters()
                    {
                        return finalNumCharacters;
                    }
                };
            }
        };
    }

    public static void writeFile(NcgrData data)
    {

    }
}
