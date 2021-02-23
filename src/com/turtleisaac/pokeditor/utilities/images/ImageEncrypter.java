package com.turtleisaac.pokeditor.utilities.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageEncrypter
{
    /**
     * Encrypts the primary and secondary frames and outputs a byte[] representing an NCGR file
     * Uses the encryption method for Platinum/ HeartGold/ SoulSilver
     * @param images a BufferedImage[] containing the primary frame at index 0 and the secondary frame at index 1
     * @return a byte[] representing an NCGR file to be written later
     */
    public static byte[] encryptPrimary(BufferedImage[] images)
    {
        final int[] data= initialize(images);

        int num= 0;

        for(int i= 0; i < data.length; i++)
        {
            data[i]= data[i] ^ (num & 0xffff);
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
    public static byte[] encryptSecondary(BufferedImage[] images)
    {
        final int[] data= initialize(images);

        int num= 31315;
        for(int i= data.length-1; i >= 0; i--)
        {
            num+= data[i];
        }

        for(int i= data.length-1; i >= 0; i--)
        {
            data[i]= data[i] ^ (num & 0xffff);
            num*= 1103515245;
            num+= 24691;
        }

        return imageOutput(data);
    }


    public static byte[] encryptPalette(Color[] palette)
    {
        Byte[] header = new Byte[] {82, 76, 67, 78, (byte) 255, (byte) 254, 0, 1, 72, 0, 0, 0, 16, 0, 1, 0, 84, 84, 76, 80, 56, 0, 0, 0, 4, 0, 10, 0, 0, 0, 0, 0, 32, 0, 0, 0, 16, 0, 0, 0};
        ArrayList<Byte> data= new ArrayList<>(Arrays.asList(header));

        for(int i= 0; i < palette.length; i++)
        {
            int value= ((palette[i].getRed() >> 3) & 0x1F) | (((palette[i].getGreen() >> 3) & 0x1F) << 5) | (((palette[i].getBlue() >> 3) & 0x1F) << 10);
            data.add((byte) ((value >> 8) & 0xff));
            data.add((byte) (value & 0xff));
        }


        byte[] ret= new byte[data.size()];
        for(int i= 0; i < data.size(); i++)
        {
            ret[i]= data.get(i);
        }

        return ret;
    }


    private static int[] initialize(BufferedImage[] images)
    {
        BufferedImage image= new BufferedImage(images[0].getWidth()*2,images[0].getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g= (Graphics2D) image.getGraphics();
        g.drawImage(images[0],0,0,null);
        g.drawImage(images[1],images[0].getWidth(),0,null);

        ArrayList<Integer> dataList= new ArrayList<>();
        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
                dataList.add(image.getRGB(col,row));
            }
        }
        int[] data= dataList.stream().mapToInt(Integer::intValue).toArray();

        int[] arr= new int[image.getWidth()*image.getHeight()/4];
        for(int i= 0; i < arr.length; i++)
        {
            arr[i]= (data[i * 4] & 0xF) | ((data[i * 4 + 1] & 0xF) << 4) | ((data[i * 4 + 2] & 0xF) << 8) | ((data[i * 4 + 3] & 0xF) << 12);
        }

        return arr;
    }

    private static byte[] imageOutput(int[] data)
    {
        byte[] ret = new byte[] {82, 71, 67, 78, (byte) 255, (byte) 254, 0, 1, 48, 25, 0, 0, 16, 0, 1, 0, 82, 65, 72, 67, 32, 25, 0, 0, 10, 0, 20, 0, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 25, 0, 0, 24, 0, 0, 0};
        int dataOffset= ret.length; //48
        ret= Arrays.copyOf(ret,ret.length + data.length);

        for(int i= 0; i < data.length; i++)
        {
            ret[i + dataOffset]= (byte) data[i];
        }

        return ret;
    }
}
