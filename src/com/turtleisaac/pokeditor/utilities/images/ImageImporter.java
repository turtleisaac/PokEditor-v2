package com.turtleisaac.pokeditor.utilities.images;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.InflaterInputStream;

public class ImageImporter
{
    public static void main(String[] args) throws IOException
    {
        SpriteImage image= importImage("/Users/Danny/Downloads/testImage5.png");

        ImageExporter.exportImage("/Users/Danny/Downloads/testImage6.png",image);
    }

    /**
     * Imports a PNG file (yes I know ImageIO.read() exists, this is just a much better way to do things for me)
     * @param file A path to an indexed PNG file
     * @return a SpriteImage containing an exact representation of the original indexed PNG file
     * @throws IOException
     */
    public static SpriteImage importImage(String file) throws IOException
    {
        byte[] fileContents= Buffer.readFile(file);

        int paletteIdx= 0;
        int imageDataIdx= 0;

        byte[] paletteChunkHeader= new byte[] {0x50,0x4C,0x54,0x45}; //PLTE
        byte[] dataChunkHeader= new byte[] {0x49,0x44,0x41,0x54}; //IDAT
        byte[] endChunkHeader= new byte[] {0x49,0x45,0x4E,0x44}; //IEND

        for(int i= 0; i < fileContents.length - 4; i++)
        {
            byte[] thisFour= Arrays.copyOfRange(fileContents,i,i+4);

            if(Arrays.equals(thisFour,paletteChunkHeader))
            {
                paletteIdx= i;
            }

            if(Arrays.equals(thisFour,dataChunkHeader))
            {
                imageDataIdx= i;
            }

            if(Arrays.equals(thisFour,endChunkHeader))
            {
                break;
            }
        }


        Buffer buffer= new Buffer(file);
        buffer.skipBytes(16); //jumps to IHDR chunk

        int width= swapEndianness(buffer.readInt());
        int height= swapEndianness(buffer.readInt());

        int bitDepth= buffer.readByte();
        int colorType= buffer.readByte();
        int compressionMethod= buffer.readByte();
        int filterMethod= buffer.readByte();
        int interlaceMethod= buffer.readByte();


        if(colorType != 3)
        {
            throw new RuntimeException("Not an indexed image: " + colorType);
        }

        if(compressionMethod != 0)
        {
            throw new RuntimeException("Invalid image compression method: " + compressionMethod);
        }

        if(filterMethod != 0)
        {
            throw new RuntimeException("Invalid filter method: " + filterMethod);
        }

        if(interlaceMethod < 0 || interlaceMethod > 1)
        {
            throw new RuntimeException("Invalid interlace method: " + interlaceMethod);
        }

        System.out.println("Bit Depth: " + bitDepth);
        ArrayList<Color> colorList= new ArrayList<>();
        buffer.skipTo(paletteIdx-4);

        int chunkLength= swapEndianness(buffer.readInt());
        buffer.skipBytes(4);

        for(int i= 0; i < chunkLength/3; i++)
        {
            int r= buffer.readByte();
            int g= buffer.readByte();
            int b= buffer.readByte();

            colorList.add(new Color(r,g,b));
        }

        buffer.skipTo(imageDataIdx-4);
        chunkLength= swapEndianness(buffer.readInt());
        buffer.skipBytes(4);

        byte[] imageData= buffer.readBytes(chunkLength);
        imageData= decompress(imageData);

        return new SpriteImage(createScanlines(imageData,bitDepth,filterMethod,width,height),colorList.toArray(new Color[0]));
    }


    private static int swapEndianness(int num)
    {
        byte[] bytes= new byte[] {(byte) (num & 0xff), (byte) ((num >> 8) & 0xff), (byte) ((num >> 16) & 0xff),(byte) ((num >> 24) & 0xff)};

        return (bytes[3] & 0xff) | ((bytes[2] & 0xff) << 8) | ((bytes[1] & 0xff) << 16) | ((bytes[0] & 0xff) << 24);
    }

    private static byte[] decompress(byte[] arr) throws IOException
    {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arr);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream);

        byte[] ret= new byte[0];
        byte[] buf = new byte[1024];
        int rlen= -1;
        while ((rlen = inflaterInputStream.read(buf)) != -1)
        {
            int current= ret.length;
            ret= Arrays.copyOf(ret,ret.length + rlen);
            System.arraycopy(buf,0,ret,current,rlen);
        }

        return ret;
    }

    private static byte[][] createScanlines(byte[] arr, int bitDepth, int filterMethod,int width, int height)
    {
        byte[][] ret= new byte[height][width];
        int numBytes= (int) Math.ceil((double) bitDepth*width/8);

        int idx= 0;
        for(int i= 0; i < ret.length; i++)
        {
            byte[] scanline= Arrays.copyOfRange(arr,idx,idx+numBytes+1);
//            System.out.println(hexToString(scanline));
            idx+= numBytes+1;

            ArrayList<Byte> byteList= new ArrayList<>();

            if(filterMethod != 0)
            {
                byteList.add(scanline[0]);
            }


            for(byte b : Arrays.copyOfRange(scanline,1,scanline.length))
            {
                switch (bitDepth)
                {
                    case 2:
                        int section= (b >> 4) & 0xf;
                        byteList.add((byte) ((section >> 2) & 0x3));
                        byteList.add((byte) (section & 0x3));
                        break;

                    case 4:
                        byteList.add((byte) ((b >> 4) & 0xf));
                        byteList.add((byte) (b & 0xf));
                        break;

                    case 8:
                        byteList.add(b);
                        break;
                }
            }


            scanline= new byte[byteList.size()];
            for(int x= 0; x < byteList.size(); x++)
            {
                scanline[x]= byteList.get(x);
            }

            ret[i]= scanline;

            if(ret[i].length > width)
            {
                ret[i]= Arrays.copyOf(ret[i],width);
            }
        }

//        System.out.println();
//        for(byte[] bytes : ret)
//        {
//            System.out.println(Arrays.toString(bytes));
//        }


        return ret;
    }

    private static String hexToString(byte[] arr)
    {
        StringBuilder ret= new StringBuilder("[");
        for(byte b : arr)
        {
            String s= Integer.toHexString(b & 0xff);
            if(s.length() == 1)
                s= 0 + s;

            ret.append("0x").append(s).append(",");
        }
        ret.deleteCharAt(ret.length()-1);
        ret.append("]");

        return ret.toString();
    }
}
