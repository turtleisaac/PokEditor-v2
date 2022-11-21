package com.turtleisaac.pokeditor.editors.spritepositions;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.narctowl.NarcWork;
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
        if(toDelete == null)
            toDelete= new ArrayList<>();

        String dataPath= project.getDataPath();
        String unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;

        File positionNarcPath;
        File positionDirPath;

        switch (project.getBaseRom())
        {
            case Platinum:
                positionNarcPath= new File(dataPath + File.separator +"poketool" + File.separator + "poke_edit" + File.separator + "pl_poke_data.narc");
                positionDirPath= new File(unpackedFolderPath + "poke_data");
                break;

            case HeartGold:
            case SoulSilver:
                positionNarcPath= new File(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "8" + File.separator + "0");
                positionDirPath= new File(unpackedFolderPath + "poke_data");
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

        Buffer buffer= new Buffer(NarcWork.getSubfilePath(0, positionDirPath));
        ArrayList<SpriteData> dataList= new ArrayList<>();

        //the length of the data structure for each species entry is 89 bytes (offsets 0-88)
        for(int i= 0; i < buffer.getLength()/89; i++)
        {
            int unknownByte= buffer.readByte(); //byte 0
            int movement= buffer.readByte(); //byte 1
            byte[] unknownSection1= buffer.readBytes(42); //bytes 2-43
            int backMovement= buffer.readByte(); //byte 44
            byte[] unknownSection2= buffer.readBytes(41); //bytes 45-85
            byte yOffset= (byte) buffer.readByte(); //byte 86
            byte xOffset= (byte) buffer.readByte(); //byte 87
            int shadowSize= buffer.readByte(); //byte 88
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
                public int getUnknownByte()
                {
                    return unknownByte;
                }

                @Override
                public int getMovement()
                {
                    return movement;
                }

                @Override
                public byte[] getUnknownSection1()
                {
                    return unknownSection1;
                }

                @Override
                public int getBackMovement()
                {
                    return backMovement;
                }

                @Override
                public byte[] getUnknownSection2()
                {
                    return unknownSection2;
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
        buffer.close();

        return dataList.toArray(new SpriteData[0]);
    }

    public static void applyPositionChanges(Project project, SpriteData[] spritePositionData) throws IOException
    {
        String dataPath= project.getDataPath();
        String unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;

        File positionNarcPath;
        File positionDirPath;

        switch (project.getBaseRom())
        {
            case Platinum:
                positionNarcPath= new File(dataPath + File.separator +"poketool" + File.separator + "poke_edit" + File.separator + "pl_poke_data.narc");
                positionDirPath= new File(unpackedFolderPath + "poke_data");
                break;

            case HeartGold:
            case SoulSilver:
                positionNarcPath= new File(dataPath + File.separator + "a" + File.separator + "1" + File.separator + "8" + File.separator + "0");
                positionDirPath= new File(unpackedFolderPath + "poke_data");
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        BinaryWriter writer= new BinaryWriter(NarcWork.getSubfilePath(0, positionDirPath));

        for (SpriteData spriteData : spritePositionData)
        {
            int unknownByte= spriteData.getUnknownByte();
            int movement= spriteData.getMovement();
            byte[] unknownSection1= spriteData.getUnknownSection1();
            int backMovement= spriteData.getBackMovement();
            byte[] unknownSection2= spriteData.getUnknownSection2();
            byte yOffset= spriteData.getSpriteYOffset();
            byte xOffset= spriteData.getShadowXOffset();
            int shadowType= spriteData.getShadowType().value;

            writer.writeByte(unknownByte);
            writer.writeByte(movement);
            writer.write(unknownSection1);
            writer.writeByte(backMovement);
            writer.write(unknownSection2);
            writer.writeByte(yOffset);
            writer.writeByte(xOffset);
            writer.writeByte(shadowType);
        }
        writer.close();

        Narctowl narctowl= new Narctowl(true);
        narctowl.pack(positionDirPath.getAbsolutePath(),"",positionNarcPath.getAbsolutePath());
    }




    public static int getHeightModifier(Project project, int species, SpriteType spriteType)
    {
        if(toDelete == null)
            toDelete= new ArrayList<>();

        String dataPath= project.getDataPath();
        String unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;
        int selected= species*4;

        File positionNarcPath;
        File positionDirPath;

        switch (project.getBaseRom())
        {
            case Diamond:
            case Pearl:
            case Platinum:
                positionNarcPath= new File(dataPath + File.separator +"poketool" + File.separator + "pokegra" + File.separator + "height.narc");
                positionDirPath= new File(unpackedFolderPath + "height");
                break;

            case HeartGold:
            case SoulSilver:
                positionNarcPath= new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "5");
                positionDirPath= new File(unpackedFolderPath + "height");
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        if(!positionDirPath.exists())
        {
            try
            {
                Narctowl narctowl= new Narctowl(true);
                narctowl.unpack(positionNarcPath.getAbsolutePath(),positionDirPath.getAbsolutePath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        toDelete.add(positionDirPath);

        Buffer buffer= new Buffer(NarcWork.getSubfilePath(selected + spriteType.value, positionDirPath));

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

    public static void applyHeightChanges(Project project, int species, int femaleBack, int maleBack, int femaleFront, int maleFront) throws IOException
    {
        String dataPath= project.getDataPath();
        String unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;
        int selected= species*4;

        File positionNarcPath;
        File positionDirPath;

        switch (project.getBaseRom())
        {
            case Diamond:
            case Pearl:
            case Platinum:
                positionNarcPath= new File(dataPath + File.separator +"poketool" + File.separator + "pokegra" + File.separator + "height.narc");
                positionDirPath= new File(unpackedFolderPath + "height");
                break;

            case HeartGold:
            case SoulSilver:
                positionNarcPath= new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "5");
                positionDirPath= new File(unpackedFolderPath + "height");
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }


        BinaryWriter writer= new BinaryWriter(NarcWork.getSubfilePath(selected, positionDirPath));
        if(femaleBack != 0)
        {
            writer.writeByte(Math.abs(femaleBack));
        }

        writer= new BinaryWriter(NarcWork.getSubfilePath(selected + 1, positionDirPath));
        if(maleBack != 0)
        {
            writer.writeByte(Math.abs(maleBack));
        }

        writer= new BinaryWriter(NarcWork.getSubfilePath(selected + 2, positionDirPath));
        if(femaleFront != 0)
        {
            writer.writeByte(Math.abs(femaleFront));
        }

        writer= new BinaryWriter(NarcWork.getSubfilePath(selected + 3, positionDirPath));
        if(maleFront != 0)
        {
            writer.writeByte(Math.abs(maleFront));
        }

        writer.close();

        Narctowl narctowl= new Narctowl(true);
        narctowl.pack(positionDirPath.getAbsolutePath(),"",positionNarcPath.getAbsolutePath());
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
        None(0),
        Small(1),
        Medium(2),
        Large(3);

        final int value;

        ShadowType(int value) {this.value= value;}
    }
}
