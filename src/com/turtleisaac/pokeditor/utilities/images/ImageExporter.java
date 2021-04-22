package com.turtleisaac.pokeditor.utilities.images;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.MemBuf;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;

public class ImageExporter
{
    /**
     * Exports a PNG file (yes I know ImageIO.write() exists, this is just a much better way to do things for me)
     * @param file A path to the target file
     * @param image a SpriteImage containing a sprite
     * @throws IOException
     */
    public static void exportImage(String file, SpriteImage image) throws IOException
    {
        byte[] pngHeader= new byte[] {(byte) 0x89,0x50,0x4E,0x47,0x0D,0x0A,0x1A,0x0A}; //PNG
        byte[] imageChunkHeader= new byte[] {0x49,0x48,0x44,0x52}; //IHDR
        byte[] paletteChunkHeader= new byte[] {0x50,0x4C,0x54,0x45}; //PLTE
        byte[] dataChunkHeader= new byte[] {0x49,0x44,0x41,0x54}; //IDAT
        byte[] endChunkHeader= new byte[] {0x49,0x45,0x4E,0x44}; //IEND

        //Image Header Chunk (IHDR)
        MemBuf imageHeaderBuf= MemBuf.create();
        MemBuf.MemBufWriter writer= imageHeaderBuf.writer().write(imageChunkHeader);

        int width= image.getWidth();
        int height= image.getHeight();

        int bitDepth;
        Color[] palette= image.getPalette();

        if(palette.length > 16)
        {
            bitDepth= 8;
        }
        else if(palette.length > 4)
        {
            bitDepth= 4;
        }
        else
        {
            bitDepth= 2;
        }

        int colorType= 3;
        int compressionMethod= 0;
        int filterMethod= 0;
        int interlaceMethod= 0;

        for(byte[] arr : image.getIndexGuide())
        {
            System.out.println(Arrays.toString(arr));
        }

        writer.writeInt(swapEndianness(width));
        writer.writeInt(swapEndianness(height));
//        writer.write((byte) (bitDepth & 0xff));
//        writer.write((byte) (colorType & 0xff));
//        writer.write((byte) compressionMethod);
//        writer.write((byte) interlaceMethod);
        writer.writeBytes(bitDepth,colorType,compressionMethod,filterMethod,interlaceMethod);


        //Palette Chunk (PLTE)
        MemBuf paletteBuf= MemBuf.create();
        writer= paletteBuf.writer().write(paletteChunkHeader);

        for(Color c : palette)
        {
            writer.writeBytes(c.getRed(),c.getGreen(),c.getBlue());
        }


        //Image Data Chunk (IDAT)
        MemBuf dataBuf= MemBuf.create();
        writer= dataBuf.writer().write(dataChunkHeader);

        byte[] imageData= convertScanlines(image.getIndexGuide(),bitDepth,filterMethod);
        imageData= compress(imageData);

        writer.write(imageData);


        //Image End Chunk (IEND)
        MemBuf endBuf= MemBuf.create();
        writer= endBuf.writer();

        writer.write(endChunkHeader);


        //Writing image
        BinaryWriter imageWriter= new BinaryWriter(file);
        imageWriter.write(pngHeader);

        imageWriter.writeInt(swapEndianness(imageHeaderBuf.reader().getBuffer().length-4));
        imageWriter.write(imageHeaderBuf.reader().getBuffer());
        imageWriter.writeInt(getCrc32(imageHeaderBuf.reader().getBuffer()));

        imageWriter.writeInt(swapEndianness(paletteBuf.reader().getBuffer().length-4));
        imageWriter.write(paletteBuf.reader().getBuffer());
        imageWriter.writeInt(getCrc32(paletteBuf.reader().getBuffer()));

        imageWriter.writeInt(swapEndianness(dataBuf.reader().getBuffer().length-4));
        imageWriter.write(dataBuf.reader().getBuffer());
        imageWriter.writeInt(getCrc32(dataBuf.reader().getBuffer()));

        imageWriter.writeInt(swapEndianness(endBuf.reader().getBuffer().length-4));
        imageWriter.write(endBuf.reader().getBuffer());
        imageWriter.writeInt(getCrc32(endBuf.reader().getBuffer()));

        imageWriter.close();
    }


    private static int swapEndianness(int num)
    {
        byte[] bytes= new byte[] {
                (byte) (num & 0xff),
                (byte) ((num >> 8) & 0xff),
                (byte) ((num >> 16) & 0xff),
                (byte) ((num >> 24) & 0xff)
        };

        return (bytes[3] & 0xff) | ( (bytes[2] & 0xff) << 8) | ( (bytes[1] & 0xff) << 16) | ( (bytes[0] & 0xff) << 24);
    }

    private static byte[] compress(byte[] arr) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        deflaterOutputStream.write(arr);
        deflaterOutputStream.flush();
        deflaterOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] convertScanlines(byte[][] table, int bitDepth, int filterMethod)
    {
        ArrayList<Byte> retList= new ArrayList<>();
        System.out.println();

        for (byte[] scanline : table)
        {
            if(scanline.length % 2 != 0)
                scanline= Arrays.copyOf(scanline,scanline.length+1);

            retList.add((byte) filterMethod);

            for (int x = 0; x < scanline.length; x+= 2)
            {
                switch (bitDepth)
                {
                    case 2:
                        retList.add((byte) ( ( (scanline[x] << 2) | (scanline[x + 1] & 0x3) ) << 4) );
                        break;

                    case 4:
                        retList.add((byte) ( (scanline[x] << 4) | (scanline[x + 1] & 0xf) ) );
                        break;

                    case 8:
                        retList.add(scanline[x]);
                        x -= 1;
                        break;
                }
            }
        }

        byte[] ret= new byte[retList.size()];

        for(int x= 0; x < ret.length; x++)
        {
            ret[x]= retList.get(x);
        }

        return ret;
    }

    private static CRC32 crc32;

    private static int getCrc32(byte[] arr)
    {
        if(crc32 == null)
            crc32= new CRC32();

        crc32.reset();
        crc32.update(arr);

        return swapEndianness((int) crc32.getValue());
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
