package com.turtleisaac.pokeditor.editors.items;

import com.turtleisaac.pokeditor.framework.MemBuf;

import java.util.ArrayList;

public class ItemTableParser
{
    public static ArrayList<ItemTableEntry> parseTable(byte[] tableArr, int numEntries)
    {
        MemBuf itemTableBuf = MemBuf.create();
        itemTableBuf.writer().write(tableArr);

        MemBuf.MemBufReader reader = itemTableBuf.reader();
        ArrayList<ItemTableEntry> itemTable = new ArrayList<>();

        for(int i = 0; i < numEntries; i++)
        {
            int data = reader.readUnsignedShort();
            int graphics = reader.readUnsignedShort();
            int palette = reader.readUnsignedShort();
            int agbID = reader.readUnsignedShort();
            itemTable.add(new ItemTableEntry()
            {
                @Override
                public int getDataArchive()
                {
                    return data;
                }

                @Override
                public int getGraphicsArchive()
                {
                    return graphics;
                }

                @Override
                public int getPaletteArchive()
                {
                    return palette;
                }

                @Override
                public int getPreviousID()
                {
                    return agbID;
                }
            });
        }

        return itemTable;
    }
}
