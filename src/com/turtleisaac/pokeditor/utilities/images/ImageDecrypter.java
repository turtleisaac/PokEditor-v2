package com.turtleisaac.pokeditor.utilities.images;

import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.StringWork;
import com.turtleisaac.pokeditor.utilities.images.formats.nclr.NclrReader;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageDecrypter
{
    private static final int width= 160;
    private static final int height= 80;

    public static PokemonSprites getSprites(String path, int species, boolean primary)
    {
        path+= File.separator;
        Color[] palette= null;
        Color[] shinyPalette= null;
        try
        {
            palette= NclrReader.readFile(path + (species + 4) + ".bin").getPalette().getPalettes()[0];
            shinyPalette= NclrReader.readFile(path + (species + 5) + ".bin").getPalette().getPalettes()[0];
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        if(palette != null)
        {
            while(backgroundDuplicated(palette))
            {
                palette[0]= new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            }
        }

        if(shinyPalette != null)
        {
            while(backgroundDuplicated(shinyPalette))
            {
                shinyPalette[0]= new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            }
        }

        SpriteImage[] femaleBack= primary ? decryptPrimary(path + species + ".bin",palette) : decryptSecondary(path + species + ".bin",palette);
        SpriteImage[] shinyFemaleBack= primary ? decryptPrimary(path + species + ".bin",shinyPalette) : decryptSecondary(path + species + ".bin",shinyPalette);

        SpriteImage[] maleBack= primary ? decryptPrimary(path + (species + 1) + ".bin",palette) : decryptSecondary(path + (species + 1) + ".bin",palette);
        SpriteImage[] shinyMaleBack= primary ? decryptPrimary(path + (species + 1) + ".bin",shinyPalette) : decryptSecondary(path + (species + 1) + ".bin",shinyPalette);

        SpriteImage[] femaleFront= primary ? decryptPrimary(path + (species + 2) + ".bin",palette) : decryptSecondary(path + (species + 2) + ".bin",palette);
        SpriteImage[] shinyFemaleFront= primary ? decryptPrimary(path + (species + 2) + ".bin",shinyPalette) : decryptSecondary(path + (species + 2) + ".bin",shinyPalette);

        SpriteImage[] maleFront= primary ? decryptPrimary(path + (species + 3) + ".bin",palette) : decryptSecondary(path + (species + 3) + ".bin",palette);
        SpriteImage[] shinyMaleFront= primary ? decryptPrimary(path + (species + 3) + ".bin",shinyPalette) : decryptSecondary(path + (species + 3) + ".bin",shinyPalette);


        Color[] finalPalette = palette;
        Color[] finalShinyPalette = shinyPalette;
        return new PokemonSprites()
        {
            @Override
            public SpriteImage[] getFemaleBack()
            {
                return femaleBack;
            }

            @Override
            public SpriteImage[] getShinyFemaleBack()
            {
                return shinyFemaleBack;
            }

            @Override
            public SpriteImage[] getMaleBack()
            {
                return maleBack;
            }

            @Override
            public SpriteImage[] getShinyMaleBack()
            {
                return shinyMaleBack;
            }

            @Override
            public SpriteImage[] getFemaleFront()
            {
                return femaleFront;
            }

            @Override
            public SpriteImage[] getShinyFemaleFront()
            {
                return shinyFemaleFront;
            }

            @Override
            public SpriteImage[] getMaleFront()
            {
                return maleFront;
            }

            @Override
            public SpriteImage[] getShinyMaleFront()
            {
                return shinyMaleFront;
            }

            @Override
            public Color[] getPalette()
            {
                return finalPalette;
            }

            @Override
            public Color[] getShinyPalette()
            {
                return finalShinyPalette;
            }
        };
    }

    /**
     * Decrypts the encrypted NCGR files used in Platinum, HeartGold, and SoulSilver
     * @param file the path to the NCGR file to decrypt
     * @param palette a Color[] which contains the colors to assign to the images being constructed
     * @return a SpriteImage[] of length two - index 0 being the "normal" frame, and index 1 being the second frame used for animation
     */
    public static SpriteImage[] decryptPrimary(String file, Color[] palette)
    {
        final int[] data= initialize(file,width,height);
        SpriteImage frame1= new SpriteImage(palette);
        SpriteImage frame2= new SpriteImage(palette);

        if(data != null)
        {
            int num= data[0];
            for(int i= 0; i < data.length; i++)
            {
                data[i]= data[i] ^ (num & 0xffff);
                num*= 1103515245;
                num+= 24691;
            }

            byte[] arr= new byte[width*height];
            for(int i= 0; i < arr.length/4; i++)
            {
                arr[i*4] = (byte) (data[i] & 0xf);
                arr[i*4+1] = (byte) ((data[i] >> 4) & 0xf);
                arr[i*4+2] = (byte) ((data[i] >> 8) & 0xf);
                arr[i*4+3] = (byte)((data[i] >> 12) & 0xf);
            }

            byte[][] pixelTable= new byte[height][width];
            int idx= 0;
            for(int row= 0; row < height; row++)
            {
                for(int col= 0; col < width; col++)
                {
                    pixelTable[row][col]= arr[idx++];
                }
            }

            byte[][] table1= new byte[80][80];
            for(int row= 0; row < table1.length; row++)
            {
                System.arraycopy(pixelTable[row], 0, table1[row], 0, table1[row].length);
            }

            byte[][] table2= new byte[80][80];
            for(int row= 0; row < table2.length; row++)
            {
                System.arraycopy(pixelTable[row], 80, table2[row], 0, table2[row].length);
            }

            frame1= new SpriteImage(table1,palette);
            frame2= new SpriteImage(table2,palette);
        }

        return new SpriteImage[] {frame1, frame2};
    }

    /**
     * Decrypts the encrypted NCGR files primarily used in Diamond and Pearl, although it is sometimes used in HeartGold and SoulSilver
     * This encryption uses an algorithm identical to that used in Platinum, excpet that the tiles are stored in inverse order
     * @param file the path to the NCGR file to decrypt
     * @param palette a Color[] which contains the colors to assign to the images being constructed
     * @return a SpriteImage[] of length two - index 0 being the "normal" frame, and index 1 being the second frame used for animation
     */
    public static SpriteImage[] decryptSecondary(String file, Color[] palette)
    {
        final int[] data= initialize(file,width,height);
        SpriteImage frame1= new SpriteImage(palette);
        SpriteImage frame2= new SpriteImage(palette);

        if(data != null)
        {
            int num= data[width*height];

            for(int i= width*height - 1; i >= 0; i--)
            {
                data[i]= data[i] ^ (num & 0xffff);
                num*= 1103515245;
                num+= 24691;
            }

            byte[] arr= new byte[width*height];
            for(int i= 0; i < arr.length/4; i++)
            {
                arr[i*4] = (byte) (data[i] & 0xf);
                arr[i*4+1] = (byte) ((data[i] >> 4) & 0xf);
                arr[i*4+2] = (byte) ((data[i] >> 8) & 0xf);
                arr[i*4+3] = (byte)((data[i] >> 12) & 0xf);
            }

            byte[][] pixelTable= new byte[height][width];
            int idx= 0;
            for(int row= 0; row < height; row++)
            {
                for(int col= 0; col < width; col++)
                {
                    pixelTable[row][col]= arr[idx++];
                }
            }

            byte[][] table1= new byte[80][80];
            for(int row= 0; row < table1.length; row++)
            {
                System.arraycopy(pixelTable[row], 0, table1[row], 0, table1[row].length);
            }

            byte[][] table2= new byte[80][80];
            for(int row= 0; row < table2.length; row++)
            {
                System.arraycopy(pixelTable[row], 80, table2[row], 0, table2[row].length);
            }

            frame1= new SpriteImage(table1,palette);
            frame2= new SpriteImage(table1,palette);
        }

        return new SpriteImage[] {frame1, frame2};

    }

    private static int[] initialize(String file, int width, int height)
    {
        if(!new File(file).exists())
            return null;

        Buffer buffer= new Buffer(file);

        if(buffer.getLength() == 0)
            return null;

        buffer.skipTo(48);
        int[] data= new int[width*height/4];
        for(int i= 0; i < data.length; i++)
        {
            data[i]= buffer.readUInt16();
        }

        try
        {
            buffer.close();
        } catch (IOException exception)
        {
            System.err.println("Buffer close error in image decrypter");
            exception.printStackTrace();
        }

        return data;
    }

    private static boolean backgroundDuplicated(Color[] palette)
    {
        Color background= palette[0];
        for(int i= 1; i < palette.length; i++)
        {
            if(palette[i].equals(background))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean spriteExists(String path, int species)
    {
        path+= File.separator;

        boolean exists= false;

        int numFilesLen = ("" + new File(path).listFiles().length).length();
        String image1 = StringWork.appendLeadingZeros(species, numFilesLen);
        String image2 = StringWork.appendLeadingZeros(species + 1, numFilesLen);

        if(new File(path + image1 + ".bin").exists() || new File(path + image2 + ".bin").exists())
            exists = true;

        return exists;
    }
}
