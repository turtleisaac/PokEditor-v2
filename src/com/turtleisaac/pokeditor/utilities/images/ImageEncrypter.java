package com.turtleisaac.pokeditor.utilities.images;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.MemBuf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageEncrypter
{
    public static void saveSprites(String path, PokemonSprites sprites, int speciesOffset, boolean primary) throws IOException
    {
        path+= File.separator;

        byte[] femaleBackNcgr= primary ? encryptPrimary(sprites.getFemaleBack(), sprites.getPalette()) : encryptSecondary(sprites.getFemaleBack(), sprites.getPalette());
        byte[] maleBackNcgr= primary ? encryptPrimary(sprites.getMaleBack(), sprites.getPalette()) : encryptSecondary(sprites.getMaleBack(), sprites.getPalette());
        byte[] femaleFrontNcgr= primary ? encryptPrimary(sprites.getFemaleFront(), sprites.getPalette()) : encryptSecondary(sprites.getFemaleFront(), sprites.getPalette());
        byte[] maleFrontNcgr= primary ? encryptPrimary(sprites.getMaleFront(), sprites.getPalette()) : encryptSecondary(sprites.getMaleFront(), sprites.getPalette());

        byte[] paletteNclr= encryptPalette(sprites.getPalette());
        byte[] shinyPaletteNclr= encryptPalette(sprites.getShinyPalette());



        BinaryWriter.writeFile(path + speciesOffset + ".ncgr", femaleBackNcgr);
        BinaryWriter.writeFile(path + (speciesOffset + 1) + ".ncgr", maleBackNcgr);
        BinaryWriter.writeFile(path + (speciesOffset + 2) + ".ncgr", femaleFrontNcgr);
        BinaryWriter.writeFile(path + (speciesOffset + 3) + ".ncgr", maleFrontNcgr);
        BinaryWriter.writeFile(path + (speciesOffset + 4) + ".nclr", paletteNclr);
        BinaryWriter.writeFile(path + (speciesOffset + 5) + ".nclr", shinyPaletteNclr);
    }




    /**
     * Encrypts the primary and secondary frames and outputs a byte[] representing an NCGR file
     * Uses the encryption method for Platinum/ HeartGold/ SoulSilver
     * @param images a BufferedImage[] containing the primary frame at index 0 and the secondary frame at index 1
     * @return a byte[] representing an NCGR file to be written later
     */
    public static byte[] encryptPrimary(BufferedImage[] images, Color[] palette)
    {
        final short[] data= initialize(images,palette);

        long num= 0;
        for(int i= 0; i < data.length; i++)
        {
            data[i]^= (num & 0xffff);
            num*= 1103515245;
            num+= 24691;
        }

        return imageOutput(data);
    }

    /**
     * Encrypts the primary and secondary frames and outputs a byte[] representing an NCGR file
     * Uses the encryption method for Diamond/ Pearl
     * @param images a BufferedImage[] containing the primary frame at index 0 and the secondary frame at index 1
     * @return a byte[] representing an NCGR file to be written later
     */
    public static byte[] encryptSecondary(BufferedImage[] images, Color[] palette)
    {
        final short[] data= initialize(images,palette);

        long num= 31315;
        for(int i= data.length-1; i >= 0; i--)
        {
            num+= data[i];
        }

        for(int i= data.length-1; i >= 0; i--)
        {
            data[i]^= (num & 0xffff);
            num*= 1103515245;
            num+= 24691;
        }

        return imageOutput(data);
    }



    public static byte[] encryptPalette(Color[] palette)
    {
        MemBuf paletteBuf= MemBuf.create();
        MemBuf.MemBufWriter paletteWriter= paletteBuf.writer().writeBytes(82, 76, 67, 78, 255, 254, 0, 1, 72, 0, 0, 0, 16, 0, 1, 0, 84, 84, 76, 80, 56, 0, 0, 0, 4, 0, 10, 0, 0, 0, 0, 0, 32, 0, 0, 0, 16, 0, 0, 0);

        for (Color color : palette)
        {
            int value = ((color.getRed() >> 3) & 0x1F) | (((color.getGreen() >> 3) & 0x1F) << 5) | (((color.getBlue() >> 3) & 0x1F) << 10);
            paletteWriter.writeShort((short) (value & 0xffff));
        }

        return paletteBuf.reader().getBuffer();
    }

    private static byte[] imageOutput(short[] data)
    {
        MemBuf imageBuf= MemBuf.create();
        MemBuf.MemBufWriter imageWriter= imageBuf.writer().writeBytes(82, 71, 67, 78, 255, 254, 0, 1, 48, 25, 0, 0, 16, 0, 1, 0, 82, 65, 72, 67, 32, 25, 0, 0, 10, 0, 20, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 25, 0, 0, 24, 0, 0, 0);

        imageWriter.write(data);

        return imageBuf.reader().getBuffer();
    }



    private static short[] initialize(BufferedImage[] images, Color[] palette)
    {
        BufferedImage image= new BufferedImage(images[0].getWidth()*2,images[0].getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g= (Graphics2D) image.getGraphics();
        g.drawImage(images[0],0,0,null);
        g.drawImage(images[1],images[0].getWidth(),0,null);

        try
        {
            ImageIO.write(image,"png", new File(System.getProperty("user.dir") + File.separator + "testImage.png"));
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }

        int idx= 0;
        int[] data= new int[image.getHeight()*image.getWidth()];
        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
               data[idx++]= indexOf(palette,image.getRGB(col,row));
            }
        }

        short[] arr= new short[image.getWidth()*image.getHeight()/4];
        for(int i= 0; i < arr.length; i++)
        {
            arr[i]= (short) (( (data[i * 4] & 0xF) | ((data[i * 4 + 1] & 0xF) << 4) | ((data[i * 4 + 2] & 0xF) << 8) | ((data[i * 4 + 3] & 0xF) << 12)) & 0xffff);
        }

        return arr;
    }

    private static int indexOf(Color[] arr, int color)
    {
        for(int i= 0; i < arr.length; i++)
        {
            if(arr[i].getRGB() == color)
                return i;
        }
        return 0;
    }
}
