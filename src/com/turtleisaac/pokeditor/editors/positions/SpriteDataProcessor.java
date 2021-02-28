package com.turtleisaac.pokeditor.editors.positions;

import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Project;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpriteDataProcessor
{
    public static ArrayList<File> toDelete;

    public static SpriteData[] getPositionData(Project project) throws IOException
    {
        toDelete= new ArrayList<>();
        String dataPath= project.getDataPath();

        File positionNarcPath;
        File positionDirPath;

        switch (project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                positionNarcPath= new File(dataPath + File.separator +"poketool" + File.separator + "poke_edit" + File.separator + "poke_data.narc");
                positionDirPath= new File(dataPath + File.separator +"poketool" + File.separator + "poke_edit" + File.separator + "poke_data");
                throw new RuntimeException("Invalid game: " + project.getBaseRom());

            case Platinum:
                positionNarcPath= new File(dataPath + File.separator +"poketool" + File.separator + "poke_edit" + File.separator + "pl_poke_data.narc");
                positionDirPath= new File(dataPath + File.separator +"poketool" + File.separator + "poke_edit" + File.separator + "pl_poke_data");
                break;

            case HeartGold:
            case SoulSilver:
                positionNarcPath= new File(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "8" + File.separator + "0");
                positionDirPath= new File(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "8" + File.separator + "0_");
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        if(!positionDirPath.exists())
        {
            Narctowl narctowl= new Narctowl(true);
            narctowl.unpack(positionNarcPath.getAbsolutePath(),positionDirPath.getAbsolutePath());
        }

        toDelete.add(positionDirPath);

        Buffer buffer= new Buffer(positionDirPath.getAbsolutePath() + File.separator + "0.bin");
        ArrayList<SpriteData> dataList= new ArrayList<>();

        for(int i= 0; i < buffer.getLength()/89; i++)
        {
            int movement= buffer.readByte();
            byte[] unknown= buffer.readBytes(85);
            byte yOffset= (byte) buffer.readByte();
            byte xOffset= (byte) buffer.readByte();
            int shadowSize= buffer.readByte();
            ShadowType shadowType = null;

            switch (shadowSize)
            {
                case 0:
                    shadowType= ShadowType.None;
                    break;
                case 1:
                    shadowType= ShadowType.Small;
                    break;
                case 2:
                    shadowType= ShadowType.Medium;
                    break;
                case 3:
                    shadowType= ShadowType.Large;
                    break;
            }

            ShadowType finalShadowType = shadowType;
            dataList.add(new SpriteData()
            {
                @Override
                public int getMovement()
                {
                    return movement;
                }

                @Override
                public byte[] getUnknownSection()
                {
                    return unknown;
                }

                @Override
                public byte getSpriteYOffset()
                {
                    return yOffset;
                }

                @Override
                public byte getShadowXOffset()
                {
                    return xOffset;
                }

                @Override
                public ShadowType getShadowType()
                {
                    return finalShadowType;
                }
            });
        }

        return dataList.toArray(new SpriteData[0]);
    }

    public static int getHeightModifier(Project project, int species, SpriteType spriteType)
    {
        toDelete= new ArrayList<>();
        String dataPath= project.getDataPath();
        int selected= species*4;

        File positionNarcPath;
        File positionDirPath;

        switch (project.getBaseRom())
        {
            case Diamond:
            case Pearl:
            case Platinum:
                positionNarcPath= new File(dataPath + File.separator +"poketool" + File.separator + "pokegra" + File.separator + "height.narc");
                positionDirPath= new File(dataPath + File.separator +"poketool" + File.separator + "pokegra" + File.separator + "height");
                break;

            case HeartGold:
            case SoulSilver:
                positionNarcPath= new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "5");
                positionDirPath= new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "5_");
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        if(!positionDirPath.exists())
        {
            try
            {
                if(!Project.isHGSS(project))
                {
                    Narctowl narctowl= new Narctowl(true);
                    narctowl.unpack(positionNarcPath.getAbsolutePath(),positionDirPath.getAbsolutePath());
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        toDelete.add(positionDirPath);

        Buffer buffer= new Buffer(positionDirPath.getAbsolutePath() + File.separator + (selected + spriteType.value) + ".bin");


        int ret;

        try
        {
            ret= -(buffer.readByte() & 0xff);
        }
        catch (RuntimeException e)
        {
            ret= 0;
        }

        return ret;
    }

    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(SpriteDataProcessor::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    public enum SpriteType
    {
        Female_Back(0),
        Male_Back(1),
        Female_Front(2),
        Male_Front(3);

        final int value;

        SpriteType(int value)
        {
            this.value = value;
        }
    }

    public enum ShadowType
    {
        None,
        Small,
        Medium,
        Large
    }
}
