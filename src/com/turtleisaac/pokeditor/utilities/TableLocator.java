package com.turtleisaac.pokeditor.utilities;

import com.turtleisaac.pokeditor.editors.overlays.OverlayData;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Project;

import java.io.File;
import java.util.ArrayList;

/**
 * Used for locating relocated tables for things such as types,
 */
public class TableLocator
{
    private Project project;
    private ArrayList<OverlayData> overlayEntries;
    String romDataPath;

    public TableLocator(Project project)
    {
        this.project= project;
        readOverlays();
        romDataPath= project.getProjectPath().getAbsolutePath() + File.separator + project.getName() + File.separator;
    }

    public void locate(TablePointer pointerLocation)
    {
        Buffer buffer= new Buffer(romDataPath + pointerLocation.file);
        buffer.skipTo(pointerLocation.pointerOffset);

        TableLocation tableLocation= locateContainer(buffer.readInt());
        buffer= new Buffer(romDataPath + tableLocation.getFile());
        buffer.skipTo(tableLocation.getOffset());
    }

    private void readOverlays()
    {
        overlayEntries= new ArrayList<>();
        Buffer buffer= new Buffer(romDataPath + "arm9ovltable.bin");

        int numOverlays= buffer.getLength()/32;
        System.out.println("Number of Overlays: " + numOverlays + "\n");

        for(int i= 0; i < numOverlays; i++)
        {
            long overlayId= buffer.readUInt32();

            long ramAddress= buffer.readUInt32();
            long ramSize= buffer.readUInt32();

            long bssSize= buffer.readUInt32();
            long staticInitStart= buffer.readUInt32();
            long staticInitEnd= buffer.readUInt32();
            long fileId= buffer.readUInt32();
            int reserved= buffer.readInt();

            System.out.println("Overlay ID: " + overlayId);
            System.out.println("Ram Address: " + ramAddress);
            System.out.println("Ram Size: " + ramSize);
            System.out.println("BSS Size: " + bssSize);
            System.out.println("Static Initializer Start Address: " + staticInitStart);
            System.out.println("Static Initializer End Address: " + staticInitStart);
            System.out.println("File ID: " + fileId + "\n");

            overlayEntries.add(new OverlayData()
            {
                @Override
                public long getOverlayId()
                {
                    return overlayId;
                }

                @Override
                public long getRamAddress()
                {
                    return ramAddress;
                }

                @Override
                public long getRamSize()
                {
                    return ramSize;
                }

                @Override
                public long getBssSize()
                {
                    return bssSize;
                }

                @Override
                public long getStaticInitStart()
                {
                    return staticInitStart;
                }

                @Override
                public long getStaticInitEnd()
                {
                    return staticInitEnd;
                }

                @Override
                public long getFileId()
                {
                    return fileId;
                }

                @Override
                public int getReserved()
                {
                    return reserved;
                }
            });
        }

    }

    public TableLocation locateContainer(int offset)
    {
        for(int i= 0; i < overlayEntries.size(); i++)
        {
            long start= overlayEntries.get(i).getRamAddress();
            long end= start + overlayEntries.get(i).getRamSize();

            if(offset > start && offset < end)
            {
                int finalI = i;
                return new TableLocation()
                {
                    @Override
                    public String getFile()
                    {
                        return romDataPath + "overlay_" + File.separator + fixNum(finalI) + ".bin";
                    }

                    @Override
                    public long getOffset()
                    {
                        return offset - start;
                    }
                };
            }
        }

        if(offset > 0x023C8000)
            return new TableLocation()
            {
                @Override
                public String getFile()
                {
                    return romDataPath + "data" + File.separator + "weather_sys" + File.separator + "9.rlcn";
                }

                @Override
                public long getOffset()
                {
                    return offset - 0x023C8000;
                }
            };
        else if(offset > 0x02000000)
            return new TableLocation()
            {
                @Override
                public String getFile()
                {
                    return romDataPath + "arm9.bin";
                }

                @Override
                public long getOffset()
                {
                    return offset - 0x02000000;
                }
            };
        else
            return null;
    }

    private String fixNum(int num)
    {
        StringBuilder strBuilder = new StringBuilder(num);
        while(strBuilder.length() != 4)
        {
            strBuilder.insert(0, "0");
        }
        return strBuilder.toString();
    }
}
