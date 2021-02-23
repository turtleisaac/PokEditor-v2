package com.turtleisaac.pokeditor.utilities.images;

import com.sun.glass.ui.Size;
import com.turtleisaac.pokeditor.utilities.images.ncer.NcerData;
import com.turtleisaac.pokeditor.utilities.images.nclr.NclrData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageActions
{
    public static Color[] bgr555ToColor(byte[] bytes)
    {
        Color[] colors = new Color[bytes.length / 2];

        for (int i = 0; i < bytes.length / 2; i++)
        {
            colors[i] = bgr555ToColor(bytes[i * 2], bytes[i * 2 + 1]);
        }

        return colors;
    }

    public static Color bgr555ToColor(byte byte1, byte byte2)
    {
        int r, b, g;

        int bgr= ((byte2 & 0xff) << 8) | (byte1 & 0xff);

        r = (bgr & 0x001F) << 3;
        g = ((bgr & 0x03E0) >> 2);
        b = ((bgr & 0x7C00) >> 7);

        return new Color(r, g, b);
    }

    public static byte[] colorToBGR555(Color[] colors)
    {
        byte[] data = new byte[colors.length * 2];

        for (int i = 0; i < colors.length; i++)
        {
            byte[] bgr = colorToBGR555(colors[i]);
            data[i * 2] = bgr[0];
            data[i * 2 + 1] = bgr[1];
        }

        return data;
    }
    public static byte[] colorToBGRA555(Color color)
    {
        byte[] d = new byte[2];

        int r = color.getRed() / 8;
        int g = (color.getGreen() / 8) << 5;
        int b = (color.getBlue() / 8) << 10;
        int a = (color.getAlpha() / 255) << 15;

        int bgra = r + g + b + a;
        System.arraycopy(ByteBuffer.allocate(4).putInt(bgra).array(),0,d,0,2);

        return d;
    }
    public static byte[] colorToBGR555(Color color)
    {
        byte[] d = new byte[2];

        int r = color.getRed() / 8;
        int g = (color.getGreen() / 8) << 5;
        int b = (color.getBlue() / 8) << 10;

        int bgr= r + g + b;
        System.arraycopy(ByteBuffer.allocate(4).putInt(bgr).array(),0,d,0,2);

        return d;
    }

    public static BufferedImage getImage(Color[] colors)
    {
        int height = (colors.length / 0x10);
        if (colors.length % 0x10 != 0)
            height++;

        BufferedImage image= new BufferedImage(160, height * 10,BufferedImage.TYPE_CUSTOM);

        boolean end = false;
        for (int i = 0; i < 16 & !end; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                if (colors.length <= j + 16 * i)
                {
                    end = true;
                    break;
                }

                for (int k = 0; k < 10; k++)
                    for (int q = 0; q < 10; q++)
                    {
                        image.setRGB((j * 10 + q), (i * 10 + k), colors[j + 16 * i].getRGB());
                    }

            }
        }

        return image;
    }

    public static Color[][] palette16To256(Color[][] palette)
    {
        // Get the colours of all the palettes in BGR555 encoding
        ArrayList<Color> paletteColor = new ArrayList<>();
        for(Color[] subPalette : palette)
            paletteColor.addAll(Arrays.asList(subPalette));

        // Set the colours in one palette
        Color[][] newPal = new Color[1][];
        newPal[0] = paletteColor.toArray(new Color[0]);

        return newPal;
    }
    public static Color[][] palette256To16(Color[][] palette)
    {
        Color[][] newPal;

        int isExact = palette[0].length % 0x10;

        if (isExact == 0)
        {
            newPal = new Color[palette[0].length / 0x10][];
            for (int i = 0; i < newPal.length; i++)
            {
                newPal[i] = new Color[0x10];
                System.arraycopy(palette[0],i*0x10,newPal[i],0,0x10);
            }
        }
        else
        {
            newPal = new Color[(palette[0].length / 0x10) + 1][];
            for (int i = 0; i < newPal.length - 1; i++)
            {
                newPal[i] = new Color[0x10];
                System.arraycopy(palette[0],i*0x10,newPal[i],0,0x10);
            }
            Color[] temp = new Color[isExact];
            System.arraycopy(palette[0],palette[0].length/0x10,temp,0,isExact);
            newPal[newPal.length - 1] = temp;
        }

        return newPal;
    }

    /**
     * Region - Tiles
     */
    
//    public static byte[] AlphaIndexTo32ARGB(Color[] palette, byte[] data, ColorFormat format)
//    {
//        byte[] direct = new byte[data.length * 4];
//
//        for (int i = 0; i < data.length; i++)
//        {
//            Color color = new Color(Color.TRANSLUCENT);
//            if (format == A3I5)
//            {
//                int colorIndex = data[i] & 0x1F;
//                int alpha = (data[i] >> 5);
//                alpha = ((alpha * 4) + (alpha / 2)) * 8;
//                color = new Color(alpha,
//                        palette[colorIndex].getRed(),
//                        palette[colorIndex].getGreen(),
//                        palette[colorIndex].getBlue());
//            }
//            else if (format == A5I3)
//            {
//                int colorIndex = data[i] & 0x7;
//                int alpha = (data[i] >> 3);
//                alpha *= 8;
//                color = new Color(alpha,
//                        palette[colorIndex].getRed(),
//                        palette[colorIndex].getGreen(),
//                        palette[colorIndex].getBlue());
//            }
//
//            byte[] argb32= BitConverter.GetBytes(color.ToArgb());
//            System.arraycopy(argb32,0,direct,i*4,4);
//        }
//
//        return direct;
//    }
    
    public static byte[] bpp2ToBpp4(byte[] data)
    {
        byte[] bpp4 = new byte[data.length * 2];

        for (int i = 0; i < data.length; i++)
        {
            byte b1 = (byte)(data[i] & 0x3);
            b1+= (byte)(((data[i] >> 2) & 0x3) << 4);

            byte b2 = (byte)((data[i] >> 4) & 0x3);
            b2+= (byte)(((data[i] >> 6) & 0x3) << 4);

            bpp4[i * 2] = b1;
            bpp4[i * 2 + 1] = b2;
        }

        return bpp4;
    }

    public static BufferedImage getPalette(Color[][] palette, ColorFormat format)
    {
        BufferedImage image= new BufferedImage(64,4,BufferedImage.TYPE_INT_RGB);
        int idx= 0;
        for(int i= 0; i < palette[0].length; i++)
        {
            for(int x= 0; x < 4; x++)
            {
                image.setRGB(x + idx,0,palette[0][i].getRGB());
                image.setRGB(x + idx,1,palette[0][i].getRGB());
                image.setRGB(x + idx,2,palette[0][i].getRGB());
                image.setRGB(x + idx,3,palette[0][i].getRGB());
//                image.setRGB(x + idx,4,palette[0][i].getRGB());
//                image.setRGB(x + idx,5,palette[0][i].getRGB());
//                image.setRGB(x + idx,6,palette[0][i].getRGB());
//                image.setRGB(x + idx,7,palette[0][i].getRGB());
//                image.setRGB(x + idx,8,palette[0][i].getRGB());
//                image.setRGB(x + idx,9,palette[0][i].getRGB());
//                image.setRGB(x + idx,10,palette[0][i].getRGB());
//                image.setRGB(x + idx,11,palette[0][i].getRGB());
//                image.setRGB(x + idx,12,palette[0][i].getRGB());
//                image.setRGB(x + idx,13,palette[0][i].getRGB());
//                image.setRGB(x + idx,14,palette[0][i].getRGB());
//                image.setRGB(x + idx,15,palette[0][i].getRGB());
            }
            idx+= 4;
        }
        return image;
    }

    public static BufferedImage getImage(byte[] tiles, byte[] tile_pal, Color[][] palette, ColorFormat format, int width, int height)
    {
        return getImage(tiles,tile_pal,palette,format,width,height,8);
    }

    public static BufferedImage getImage(byte[] tiles, byte[] tile_pal, Color[][] palette, ColorFormat format, int width, int height, int start)
    {
        if (tiles.length == 0)
            return new BufferedImage(1, 1,format.value);

        BufferedImage image;
        try
        {
           image= new BufferedImage(width, height,format.value);
        }
        catch (IllegalArgumentException e)
        {
            width= 64;
            height= 96;
            image= new BufferedImage(width,height,format.value);
//            e.printStackTrace();
        }


        int pos = start;
        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                int num_pal = 0;
                if (tile_pal.length > w + h * width)
                    num_pal = tile_pal[w + h * width];

                if (num_pal >= palette.length)
                    num_pal = 0;


                Color color = getColor(tiles, palette[num_pal], format, pos++);

                image.setRGB(w, h, color.getRGB());
            }
        }


        BufferedImage leftAll= image.getSubimage(0,0,width-8,height);
        BufferedImage right8= image.getSubimage(width-8,0,8,height);

        image= new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
//        image= insertImage(image,right8,0,0);
//        image= insertImage(image,leftAll,8,0);

        Graphics2D g= (Graphics2D) image.getGraphics();
        g.drawImage(right8,0,0,null);
        g.drawImage(leftAll,8,0,null);


//        try
//        {
//            ImageIO.write(left8,"png",new File(System.getProperty("user.dir") + File.separator + "left8_" + System.currentTimeMillis() + ".png"));
//            ImageIO.write(right8,"png",new File(System.getProperty("user.dir") + File.separator + "right8_"  + System.currentTimeMillis() + ".png"));
//        } catch (IOException exception)
//        {
//            exception.printStackTrace();
//        }

        return image;
    }

    public static Color getColor(byte[] data, Color[] palette, ColorFormat format, int pos)
    {
        Color color = palette[0];
        int alpha, index;

        switch (format)
        {
            case A3I5:
                if (data.length <= pos) break;
                index = data[pos] & 0x1F;
                alpha = (data[pos] >> 5);
                alpha = ((alpha * 4) + (alpha / 2)) * 8;
                if (palette.length > index)
                    color = new Color(
                            palette[index].getRed(),
                            palette[index].getGreen(),
                            palette[index].getBlue(),alpha);

                pos++;
                break;

            case A4I4:
                if (data.length <= pos) break;
                index = data[pos] & 0xF;
                alpha = (data[pos] >> 4);
                alpha *= 16;
                if (palette.length > index)
                    color = new Color(
                            palette[index].getRed(),
                            palette[index].getGreen(),
                            palette[index].getBlue(),alpha);

                pos++;
                break;

            case A5I3:
                if (data.length <= pos) break;
                index = data[pos] & 0x7;
                alpha = (data[pos] >> 3);
                alpha *= 8;
                if (palette.length > index)
                    color = new Color(
                            palette[index].getRed(),
                            palette[index].getGreen(),
                            palette[index].getBlue(),alpha);

                pos++;
                break;

            case colors2:
                if (data.length <= (pos / 8)) break;
                byte bit1 = data[pos / 8];
                index= BitsConverter.byteToBits(bit1)[pos % 8];
                if (palette.length > index)
                    color = palette[index];
                pos++;
                break;

            case colors4:
                if (data.length <= (pos / 4)) break;
                byte bit2 = data[pos / 4];
                index = BitsConverter.byteToBit2(bit2)[pos % 4];
                if (palette.length > index)
                    color = palette[index];
                pos++;
                break;

            case colors16:
                if (data.length <= (pos / 2))
                    break;
                int bit4 = data[pos / 2] & 0xff;
                index = BitsConverter.byteToBit4(bit4)[pos % 2];
                if (palette.length > index)
                    color = palette[index];
                pos++;
                break;

            case colors256:
                if (data.length > pos && palette.length > data[pos])
                    color = palette[data[pos]];
                pos++;
                break;

            case direct:    // RGB555
                if (pos + 2 >= data.length)
                    break;

                int byteColor= ByteBuffer.allocate(2).put(data[pos]).getInt();
                color = new Color(
                        (byteColor & 0x1F) * 8,
                        ((byteColor >> 5) & 0x1F) * 8,
                        ((byteColor >> 10) & 0x1F) * 8,
                        (byteColor >> 15) == 1 ? 255 : 0);
                pos+= 2;
                break;

            case BGRA32:
                if (pos + 4 >= data.length)
                    break;

                color = new Color(data[pos], data[pos+1], data[pos+2],data[pos+3]);
                pos+= 4;
                break;

            case ABGR32:
                if (pos + 4 >= data.length)
                    break;

                color = new Color(data[pos], data[pos+1], data[pos+2], data[pos+3]);
                pos+= 4;
                break;

            case texel4x4:
                throw new RuntimeException("Compressed texel 4x4 not supported yet");
            default:
                throw new RuntimeException("Unknown color format");
        }
        return color;
    }

    public static byte[] horizontalToLineal(byte[] horizontal, int width, int height, int bpp, int tile_size)
    {
        byte[] lineal = new byte[horizontal.length];
        int tile_width = tile_size * bpp / 8;   // Calculate the number of byte per line in the tile
        // pixels per line * bits per pixel / 8 bits per byte
        int tilesX = width / tile_size;
        int tilesY = height / tile_size;

        int pos = 0;
        for (int ht = 0; ht < tilesY; ht++)
        {
            for (int wt = 0; wt < tilesX; wt++)
            {
                // Get the tile data
                for (int h = 0; h < tile_size; h++)
                {
                    for (int w = 0; w < tile_width; w++)
                    {
                        final int value = (w + h * tile_width * tilesX) + wt * tile_width + ht * tilesX * tile_size * tile_width;
                        if (value >= lineal.length)
                            continue;
                        if (pos >= lineal.length)
                            continue;

                        lineal[pos++] = horizontal[value];
                    }
                }
            }
        }

        return lineal;
    }

    public static byte[] linealToHorizontal(byte[] lineal, int width, int height, int bpp, int tile_size)
    {
        byte[] horizontal = new byte[lineal.length];
        int tile_width = tile_size * bpp / 8;   // Calculate the number of byte per line in the tile
        // pixels per line * bits per pixel / 8 bits per byte
        int tilesX = width / tile_size;
        int tilesY = height / tile_size;

        int pos = 0;
        for (int ht = 0; ht < tilesY; ht++)
        {
            for (int wt = 0; wt < tilesX; wt++)
            {
                // Get the tile data
                for (int h = 0; h < tile_size; h++)
                {
                    for (int w = 0; w < tile_width; w++)
                    {
                        final int value = (w + h * tile_width * tilesX) + wt * tile_width + ht * tilesX * tile_size * tile_width;
                        if (value >= lineal.length)
                            continue;
                        if (pos >= lineal.length)
                            continue;

                        horizontal[value] = lineal[pos++];
                    }
                }
            }
        }

        return horizontal;
    }

    public static int removeDuplicatedColors(Color[] palette, byte[] tiles)
    {
        ArrayList<Color> colors = new ArrayList<Color>();
        int first_duplicated_color = -1;

        for (int i = 0; i < palette.length; i++)
        {
            if (!colors.contains(palette[i]))
                colors.add(palette[i]);
            else        // The color is duplicated
            {
                int newIndex = colors.indexOf(palette[i]);
                replaceColor(tiles, i, newIndex);
                colors.add(new Color(248, 0, 248));

                if (first_duplicated_color == -1)
                    first_duplicated_color = i;
            }
        }

        palette = colors.toArray(new Color[0]);
        return first_duplicated_color;
    }
    
    public static int removeNotUsedColors(Color[] palette, byte[] tiles)
    {
        int first_notUsed_color = -1;

        boolean[] colors = new boolean[palette.length];
        for (int i = 0; i < palette.length; i++)
            colors[i] = false;

        for (int i = 0; i < tiles.length; i++)
            colors[tiles[i]] = true;

        for (int i = 0; i < colors.length; i++)
            if (!colors[i])
                first_notUsed_color = i;

        return first_notUsed_color;
    }
    
//    public static void Change_Color(byte[] tiles, int oldIndex, int newIndex, ColorFormat format)
//    {
//        if (format == ColorFormat.colors16) // Yeah, I should improve it
//            tiles = Helper.BitsConverter.BytesToBit4(tiles);
//        else if (format != ColorFormat.colors256)
//            throw new RuntimeException("Only supported 4bpp and 8bpp images.");
//
//        for (int i = 0; i < tiles.length; i++)
//        {
//            if (tiles[i] == oldIndex)
//                tiles[i] = (byte)newIndex;
//            else if (tiles[i] == newIndex)
//                tiles[i] = (byte)oldIndex;
//        }
//
//        if (format == ColorFormat.colors16)
//            tiles = Helper.BitsConverter.Bits4ToByte(tiles);
//    }

//    public static void Swap_Color(byte[] tiles, Color[] palette, int oldIndex, int newIndex, ColorFormat format)
//    {
//        if (format == ColorFormat.colors16) // Yeah, I should improve it
//            tiles = Helper.BitsConverter.BytesToBit4(tiles);
//        else if (format != ColorFormat.colors256)
//            throw new RuntimeException("Only supported 4bpp and 8bpp images.");
//
//        Color old_color = palette[oldIndex];
//        palette[oldIndex] = palette[newIndex];
//        palette[newIndex] = old_color;
//
//        for (int i = 0; i < tiles.length; i++)
//        {
//            if (tiles[i] == oldIndex)
//                tiles[i] = (byte)newIndex;
//            else if (tiles[i] == newIndex)
//                tiles[i] = (byte)oldIndex;
//        }
//
//        if (format == ColorFormat.colors16)
//            tiles = Helper.BitsConverter.Bits4ToByte(tiles);
//    }

    public static void replaceColor(byte[] tiles, int oldIndex, int newIndex)
    {
        for (int i = 0; i < tiles.length; i++)
        {
            if (tiles[i] == oldIndex)
                tiles[i] = (byte)newIndex;
        }
    }

//    public static void Swap_Palette(byte[] tiles, Color[] newp, Color[] oldp, ColorFormat format, double threshold)
//    {
//        if (format == colors16) // Yeah, I should improve it
//            tiles = Helper.BitsConverter.BytesToBit4(tiles);
//        else if (format != ColorFormat.colors256)
//            throw new RuntimeException("Only supported 4bpp and 8bpp images.");
//
//        ArrayList<Color> notfound = new ArrayList<>();
//        ArrayList<Color> newplist = new ArrayList<>();
//        newplist.addAll(Arrays.asList(newp));
//
//        for (int i = 0; i < tiles.length; i++)
//        {
//            Color px = oldp[tiles[i]];
//            int id = newplist.indexOf(px);
//
//            if (px.getRGB() == Color.TRANSLUCENT && id == -1)
//                id = 0;
//
//            if (id == -1)
//                id = FindNextColor(px, newp, threshold);
//
//            if (id == -1)
//            {
//                // If the color is not found, maybe is that the pixel own to another cell (overlapping cells).
//                // For this reason, there are two ways to do that:
//                // 1º Get the original hidden color from the original file                               <- In mind
//                // 2º Set this pixel as transparent to show the pixel from the other cell (tiles[i] = 0) <- Done!
//                // If there isn't overlapping cells, throw exception                                     <- In mind
//                notfound.add(px);
//                id = 0;
//            }
//
//            tiles[i] = (byte)id;
//        }
//
//        //if (notfound.size() > 0)
//        //    throw new RuntimeException("Color not found in the original palette!");
//
//        if (format == ColorFormat.colors16)
//            tiles = Helper.BitsConverter.Bits4ToByte(tiles);
//    }

    public static Size getSize(int fileSize, int bpp)
    {
        int width, height;
        int num_pix = fileSize * 8 / bpp;

        // If the image it's a square
        if (Math.pow((int)(Math.sqrt(num_pix)), 2) == num_pix)
            width = height = (int)Math.sqrt(num_pix);
        else
        {
            width = (Math.min(num_pix, 0x100));
            height = num_pix / width;
        }

        if (height == 0)
            height = 1;
        if (width == 0)
            width = 1;

        return new Size(width, height);
    }

    public static long addImage(byte[] data, byte[] newData, long blockSize)
    {
        // Add the image to the end of the data
        // Return the offset where the data is added
        ArrayList<Byte> result = new ArrayList<>();
        for(byte b : data)
            result.add(b);

        while (result.size() % blockSize != 0)
            result.add((byte) 0x00);

        long offset = (long)result.size();

        for(byte b : newData)
            result.add(b);
        while (result.size() % blockSize != 0)
            result.add((byte) 0x00);

        data= new byte[result.size()];
        for(int i= 0; i < result.size(); i++)
        {
            data[i]= result.get(i);
        }
        return offset;
    }

    public static long addImage(byte[] data, byte[] newData, long partOffset, long partSize, long blockSize, long addedLength)
    {
        // Add the image to the end of the partition data
        // Return the offset where the data has been inserted
        ArrayList<Byte> result = new ArrayList<>();
        for(byte b : data)
            result.add(b);
        long offset = partOffset + partSize;

        addedLength = (partSize % blockSize != 0) ? blockSize - partSize % blockSize : 0;
        if (offset == result.size())
            for(int i= 0; i < addedLength; i++)
                result.add((byte) 0);
        else
            for(int i= 0; i < addedLength; i++)
                result.add((int) (i + offset), (byte) 0);
        offset+= addedLength;

        if (offset == result.size())
            for(byte b : newData)
                result.add(b);
        else
            for(int i= 0; i < newData.length; i++)
                result.add((int) (i + offset), newData[i]);
        addedLength+= newData.length;

        data= new byte[result.size()];
        for(int i= 0; i < data.length; i++)
        {
            data[i]= result.get(i);
        }
        return offset;
    }

    public static int findNextColor(Color c, Color[] palette, double threshold)
    {
        int id = -1;
        double minDistance = Double.MAX_VALUE;

        // Skip the first color since it used to be the transparent color and we
        // don't want that as the best match if possible.
        for (int i = 1; i < palette.length; i++)
        {
            double x = palette[i].getRed() - c.getRed();
            double y = palette[i].getGreen() - c.getGreen();
            double z = palette[i].getBlue() - c.getBlue();
            double distance = (double)Math.sqrt(x * x + y * y + z * z);

            if (distance < minDistance)
            {
                minDistance = distance;
                id = i;
            }
        }

        // If the distance it's bigger than wanted, remove the best match
        if (minDistance > threshold)
            id = -1;

        // If still it doesn't found the color try with the first one.
        if (id == -1)
        {
            double x = palette[0].getRed() - c.getRed();
            double y = palette[0].getGreen() - c.getGreen();
            double z = palette[0].getBlue() - c.getBlue();
            double distance= Math.sqrt(x * x + y * y + z * z);

            if (distance <= threshold)
                id = 0;
        }

        if (id == -1)
            System.out.println("Color not found: " + c + ", distance: " + minDistance);

        return id;
    }

//    public static void Indexed_Image(BufferedImage img, ColorFormat cf, byte[] tiles, Color[] palette)
//    {
//        // It's a slow method but it should work always
//        int width = img.getWidth();
//        int height = img.getHeight();
//
//        ArrayList<Color> coldif = new ArrayList<>();
//        int[][] data = new int[width * height][2];
//
//        // Get the indexed data
//        for (int h = 0; h < height; h++)
//        {
//            for (int w = 0; w < width; w++)
//            {
//                Color pix = new Color(img.getRGB(w, h));
//                Color apix = new Color(pix.getRed(), pix.getGreen(), pix.getBlue());   // Without alpha value
//
//                if (pix.getAlpha() == 0)
//                    apix = new Color(Color.TRANSLUCENT);
//
//                // Add the color to the provisional palette
//                if (!coldif.contains(apix))
//                    coldif.add(apix);
//
//                // Get the index and save the alpha value
//                data[w + h * width][0] = coldif.indexOf(apix);  // Index
//                data[w + h * width][1] = pix.getAlpha();        // Alpha value
//            }
//        }
//
//        int max_colors = 0;     // Maximum colors per palette
//        int bpc = 0;            // Bits per color
//        switch (cf)
//        {
//            case A3I5: max_colors = 32; bpc = 8; break;
//            case colors4: max_colors = 4; bpc = 2; break;
//            case colors16: max_colors = 16; bpc = 4; break;
//            case colors256: max_colors = 256; bpc = 8; break;
//            case texel4x4: throw new RuntimeException("Texel 4x4 not supported yet.");
//            case A5I3: max_colors = 8; bpc = 8; break;
//            case direct: max_colors = 0; bpc = 16; break;
//            case colors2: max_colors = 2; bpc = 1; break;
//            case A4I4: max_colors = 16; bpc = 8; break;
//        }
//
//        // Not dithering method for now, I hope you input a image with less than the maximum colors
//        if (coldif.size() > max_colors && cf != ColorFormat.direct)
//            throw new RuntimeException("The image has more colors than permitted.\n" + (coldif.size() + 1) + " unique colors!");
//
//        // Finally get the set the tile array with the correct format
//        tiles = new byte[width * height * bpc / 8];
//        for (int i = 0, j = 0; i < tiles.length; )
//        {
//            switch (cf)
//            {
//                case colors2:
//                case colors4:
//                case colors16:
//                case colors256:
//                    for (int b = 0; b < 8; b+= bpc)
//                        if (j < data.length)
//                            tiles[i] |= (byte)(data[j++][0] << b);
//
//                    i++;
//                    break;
//
//                case A3I5:
//                    byte alpha1 = (byte)(data[j][1] * 8 / 256);
//                    byte va1 = (byte)data[j++][0];
//                    va1 |= (byte)(alpha1 << 5);
//                    tiles[i++] = va1;
//                    break;
//
//                case A4I4:
//                    byte alpha3 = (byte)(data[j][1] * 16 / 256);
//                    byte va3 = (byte)data[j++][0];
//                    va3 |= (byte)(alpha3 << 4);
//                    tiles[i++] = va3;
//                    break;
//
//                case A5I3:
//                    byte alpha2 = (byte)(data[j][1] * 32 / 256);
//                    byte va2 = (byte)data[j++][0];
//                    va2 |= (byte)(alpha2 << 3);
//                    tiles[i++] = va2;
//                    break;
//
//                case direct:
//                    byte[] v = ColorToBGRA555(new Color(data[j][1], coldif.get(data[j++][0])));
//                    tiles[i++] = v[0];
//                    tiles[i++] = v[1];
//                    break;
//
//                case texel4x4:
//                    // Not supported
//                    break;
//            }
//        }
//
//        palette = coldif.toArray(new Color[0]);
//    }

    /**
     * Region - Map
     */

//    public static byte[] Apply_Map(NTFS[] map, byte[] tiles, byte[] tile_pal, int bpp, int tile_size, int startInfo = 0)
//    {
//        int tile_length = tile_size * tile_size * bpp / 8;
//        int num_tiles = tiles.length / tile_length;
//
//        ArrayList<Byte> bytes = new ArrayList<Byte>();
//        tile_pal = new byte[(map.length - startInfo) * tile_size * tile_size];
//
//        for (int i = startInfo; i < map.length; i++)
//        {
//            if (map[i].nTile >= num_tiles)
//                map[i].nTile = 0;
//
//            byte[] currTile = new byte[tile_length];
//            if (map[i].nTile * tile_length + tile_length > tiles.length)
//                map[i].nTile = 0;
//
//            if (tile_length < tiles.length)
//                Array.Copy(tiles, map[i].nTile * tile_length, currTile, 0, tile_length);
//
//            if (map[i].xFlip == 1)
//                currTile = XFlip(currTile, tile_size, bpp);
//            if (map[i].yFlip == 1)
//                currTile = YFlip(currTile, tile_size, bpp);
//
//            bytes.addRange(currTile);
//
//            for (int t = 0; t < tile_size * tile_size; t++)
//                tile_pal[i * tile_size * tile_size + t] = map[i].nPalette;
//        }
//
//        return bytes.toArray();
//    }
//
    public static byte[] XFlip(byte[] tile, int tile_size, int bpp)
    {
        byte[] newTile = new byte[tile.length];
        int tile_width = tile_size * bpp / 8;

        for (int h = 0; h < tile_size; h++)
        {
            for (int w = 0; w < tile_width / 2; w++)
            {
                final int index = ((tile_width - 1) - w) + h * tile_width;
                byte b = tile[index];
                newTile[w + h * tile_width] = Reverse_Bits(b, bpp);

                b = tile[w + h * tile_width];
                newTile[index] = Reverse_Bits(b, bpp);
            }
        }
        return newTile;
    }

    public static byte Reverse_Bits(byte b, int length)
    {
        byte rb = 0;

        if (length == 4)
            rb = (byte)((b << 4) + (b >> 4));
        else if (length == 8)
            return b;

        return rb;
    }

    public static byte[] YFlip(byte[] tile, int tile_size, int bpp)
    {
        byte[] newTile = new byte[tile.length];
        int tile_width = tile_size * bpp / 8;

        for (int h = 0; h < tile_size / 2; h++)
        {
            for (int w = 0; w < tile_width; w++)
            {
                newTile[w + h * tile_width] = tile[w + (tile_size - 1 - h) * tile_width];
                newTile[w + (tile_size - 1 - h) * tile_width] = tile[w + h * tile_width];
            }
        }
        return newTile;
    }

//    public static NTFS[] Create_BasicMap(int num_tiles, int startTile = 0, byte palette = 0)
//    {
//        NTFS[] map = new NTFS[num_tiles];
//
//        for (int i = startTile; i < num_tiles; i++)
//        {
//            map[i] = new NTFS();
//            map[i].nPalette = palette;
//            map[i].yFlip = 0;
//            map[i].xFlip = 0;
//            //if (i >= startFillTile)
//            //    map[i].nTile = (int)fillTile;
//            //else
//            map[i].nTile = (int)(i + startTile);
//        }
//
//        return map;
//    }
//    public static NTFS[] Create_Map(byte[] data, int bpp, int tile_size, byte palette = 0)
//    {
//        int ppt = tile_size * tile_size; // pixels per tile
//        int tile_length = ppt * bpp / 8;
//
//        // Divide the data in tiles
//        byte[][] tiles = new byte[data.length / tile_length][];
//        for (int i = 0; i < tiles.length; i++)
//        {
//            tiles[i] = new byte[tile_length];
//            Array.Copy(data, i * tiles[i].length, tiles[i], 0, tiles[i].length);
//        }
//
//        NTFS[] map = new NTFS[tiles.length];
//        ArrayList<byte[]> newtiles = new ArrayList<byte[]>();
//        for (int i = 0; i < map.length; i++)
//        {
//            map[i].nPalette = palette;
//            map[i].xFlip = 0;
//            map[i].yFlip = 0;
//
//            int index = -1;
//            byte flipX = 0;
//            byte flipY = 0;
//
//            for (int t = 0; t < newtiles.size(); t++)
//            {
//                if (Compare_Array(newtiles[t], tiles[i]))
//                {
//                    index = t;
//                    break;
//                }
//                if (Compare_Array(newtiles[t], XFlip(tiles[i], tile_size, bpp)))
//                {
//                    index = t;
//                    flipX = 1;
//                    break;
//                }
//                if (Compare_Array(newtiles[t], YFlip(tiles[i], tile_size, bpp)))
//                {
//                    index = t;
//                    flipY = 1;
//                    break;
//                }
//                if (Compare_Array(newtiles[t], YFlip(XFlip(tiles[i], tile_size, bpp), tile_size, bpp)))
//                {
//                    index = t;
//                    flipX = 1;
//                    flipY = 1;
//                    break;
//                }
//            }
//
//            if (index > -1)
//                map[i].nTile = (int)index;
//            else
//            {
//                map[i].nTile = (int)newtiles.size();
//                newtiles.add(tiles[i]);
//            }
//            map[i].xFlip = flipX;
//            map[i].yFlip = flipY;
//        }
//
//        // Save the new tiles
//        data = new byte[newtiles.size() * tile_length];
//        for (int i = 0; i < newtiles.size(); i++)
//            for (int j = 0; j < newtiles[i].length; j++)
//                data[j + i * tile_length] = newtiles[i][j];
//        return map;
//    }
//    public static boolean Compare_Array(byte[] d1, byte[] d2)
//    {
//        if (d1.length != d2.length)
//            return false;
//
//        for (int i = 0; i < d1.length; i++)
//            if (d1[i] != d2[i])
//                return false;
//
//        return true;
//    }
//
//    public static NTFS MapInfo(int value)
//    {
//        NTFS mapInfo = new NTFS();
//
//        mapInfo.nTile = (int)(value & 0x3FF);
//        mapInfo.xFlip = (byte)((value >> 10) & 1);
//        mapInfo.yFlip = (byte)((value >> 11) & 1);
//        mapInfo.nPalette = (byte)((value >> 12) & 0xF);
//
//        return mapInfo;
//    }
//    public static int MapInfo(NTFS map)
//    {
//        int npalette = map.nPalette << 12;
//        int yFlip = map.yFlip << 11;
//        int xFlip = map.xFlip << 10;
//        int data = npalette + yFlip + xFlip + map.nTile;
//
//        return (int)data;
//    }

    /**
     * Region - OAM
     */

    public static Size getOAMSize(byte shape, byte size)
    {
        Size imageSize = new Size();

        switch (shape)
        {
            case 0x00:  // Square
                switch (size)
                {
                    case 0x00:
                        imageSize = new Size(8, 8);
                        break;
                    case 0x01:
                        imageSize = new Size(16, 16);
                        break;
                    case 0x02:
                        imageSize = new Size(32, 32);
                        break;
                    case 0x03:
                        imageSize = new Size(64, 64);
                        break;
                }
                break;
            case 0x01:  // Horizontal
                switch (size)
                {
                    case 0x00:
                        imageSize = new Size(16, 8);
                        break;
                    case 0x01:
                        imageSize = new Size(32, 8);
                        break;
                    case 0x02:
                        imageSize = new Size(32, 16);
                        break;
                    case 0x03:
                        imageSize = new Size(64, 32);
                        break;
                }
                break;
            case 0x02:  // Vertical
                switch (size)
                {
                    case 0x00:
                        imageSize = new Size(8, 16);
                        break;
                    case 0x01:
                        imageSize = new Size(8, 32);
                        break;
                    case 0x02:
                        imageSize = new Size(16, 32);
                        break;
                    case 0x03:
                        imageSize = new Size(32, 64);
                        break;
                }
                break;
        }

        return imageSize;
    }

    public static BufferedImage getImage(NcerData.Bank bank, long blockSize, ImageBase img, NclrData.Palette pal, int max_width, int max_height, boolean image, int zoom, int[] index, Color background)
    {
        Size size = new Size(max_width * zoom, max_height * zoom);
        NcerData.OAM[] oams= bank.getOAMs();
        BufferedImage bankImage = new BufferedImage(size.width, size.height,BufferedImage.TYPE_INT_RGB);

        for(int row= 0; row < bankImage.getHeight(); row++)
        {
            for(int col= 0; col < bankImage.getWidth(); col++)
            {
                bankImage.setRGB(col,row,background.getRGB());
            }
        }

        BufferedImage cell;
        for (int i = 0; i < oams.length; i++)
        {
            NcerData.OAM oam= oams[i];

            if(oam == null)
                break;

            boolean draw = false;
            if (index == null)
                draw = true;
            else
                for (int j : index)
                    if (j == i)
                    {
                        draw = true;
                        break;
                    }

            if (!draw)
                continue;

            if (oam.getWidth() == 0x00 || oam.getHeight() == 0x00)
                continue;

            long tileOffset = oam.getObj2().getTileOffset();
            tileOffset = (tileOffset << (byte)blockSize);

            if (image)
            {
                ImageBase cell_img = new ImageBase();
                cell_img.setTiles(img.tiles.clone(), oam.getWidth(), oam.getHeight(), img.format, img.tileForm, false);
                cell_img.setTilePal(img.tilePal);
                cell_img.changeStartByte((int)(tileOffset * 0x20 + bank.getPartitionOffset()));

                byte num_pal = oam.getObj2().getIndexPalette();
                if (num_pal >= pal.getNumColors())
                    num_pal = 0;
                Arrays.fill(cell_img.tilePal, num_pal);


                cell= cell_img.getImage(pal);

                int x= (int) oam.getObj1().getXOffset();
                int y= oam.getObj0().getYOffset();
                x+= bankImage.getWidth()/2;
                y+= bankImage.getHeight()/2;

                Graphics2D g= (Graphics2D) bankImage.getGraphics();
                g.drawImage(cell,x,y,null);
            }
        }

        return bankImage;
    }

    public static byte[] getOAMData(NcerData.OAM oam, byte[] image, ColorFormat format)
    {
        if (format == ColorFormat.colors16)
            image= BitsConverter.bytesToBit4(image);

        ArrayList<Byte> data = new ArrayList<>();
        int y1 = 128 + oam.getObj0().getYOffset();
        int y2 = y1 + oam.getHeight();
        int x1 = (int) (256 + oam.getObj1().getXOffset());
        int x2 = x1 + oam.getWidth();

        for (int ht = 0; ht < 256; ht++)
            for (int wt = 0; wt < 512; wt++)
                if (ht >= y1 && ht < y2)
                    if (wt >= x1 && wt < x2)
                        data.add(image[wt + ht * 512]);

        if (format == ColorFormat.colors16)
            return BitsConverter.Bits4ToByte(data.toArray(new Byte[0]));
        else
            return BitsConverter.toArray(data);
    }

    public static int OAMComparison(NcerData.OAM c1, NcerData.OAM c2)
    {
        if (c1.getObj2().getPriority() < c2.getObj2().getPriority())
            return 1;
        else if (c1.getObj2().getPriority() > c2.getObj2().getPriority())
            return -1;
        else   // Same priority
        {
            if (c1.getCellNumber() < c2.getCellNumber())
                return 1;
            else if (c1.getCellNumber() > c2.getCellNumber())
                return -1;
            else // Same cell
                return 0;
        }
    }


    private static BufferedImage insertImage(BufferedImage image, BufferedImage toInsert, int x, int y)
    {
        System.out.println("\n(" + x + "," + y + ")");
        x+= image.getWidth()/2;
        y+= image.getHeight()/2;

        toInsert= copyOfImage(toInsert);

        try
        {
            ImageIO.write(image,"png",new File(System.getProperty("user.dir") + File.separator + "ORIG_" + image.hashCode() + ".png"));
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }

//        if(toInsert.getHeight() == 80 || toInsert.getWidth() == 80)
//            return image;

//        if(y > 80 || y < 0 || x > 80 || x < 0)
//            return image;

        System.out.println("(" + x + "," + y + ")");


        ArrayList<Integer> colorList= new ArrayList<>();
        for(int row= 0; row < toInsert.getHeight(); row++)
        {
            for(int col= 0; col < toInsert.getWidth(); col++)
            {
                colorList.add(toInsert.getRGB(col,row));
            }
        }

//        if(y + toInsert.getHeight() > image.getHeight() || x + toInsert.getWidth() > image.getWidth())
//            return image;

        int idx= 0;
        for(int row= y; row - y < toInsert.getHeight(); row++)
        {
            for(int col= x; col - x < toInsert.getWidth(); col++)
            {
                try
                {
                    image.setRGB(col,row,colorList.get(idx++));
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    System.err.println("\n(" + col + "," + row + ")");
                    System.err.println("(Image Width: " + image.getWidth() + ", Image Height: " + image.getHeight() + ")");
                    System.err.println("(Insert Width: " + toInsert.getWidth() + ", Insert Height: " + toInsert.getHeight() + ")");
                    e.printStackTrace();
//                    System.exit(1);
                }
            }
        }

        System.out.println("\n\n");

        try
        {
            ImageIO.write(image,"png",new File(System.getProperty("user.dir") + File.separator + "MOD_" + image.hashCode() + ".png"));
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
        return image;
    }

    public static BufferedImage copyOfImage(BufferedImage image)
    {
        BufferedImage newImage= new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
                newImage.setRGB(col,row,image.getRGB(col,row));
            }
        }
        return newImage;
    }


    public static class BitsConverter
    {
        //To bits
        public static byte[] byteToBits(byte data)
        {
            ArrayList<Byte> bits = new ArrayList<>();

            for (int j = 7; j >= 0; j--)
                bits.add((byte)((data >> j) & 1));

            byte[] bytes= new byte[bits.size()];
            for(int i= 0; i < bytes.length; i++)
            {
                bytes[i]= bits.get(i);
            }

            return bytes;
        }

        public static byte[] byteToBit2(byte data)
        {
            byte[] bit2 = new byte[4];

            bit2[0] = (byte)(data & 0x3);
            bit2[1] = (byte)((data >> 2) & 0x3);
            bit2[2] = (byte)((data >> 4) & 0x3);
            bit2[3] = (byte)((data >> 6) & 0x3);

            return bit2;
        }

        public static byte[] byteToBit4(int data)
        {
            byte[] bit4 = new byte[2];

            bit4[0] = (byte)(data & 0x0F);
            bit4[1] = (byte)((data & 0xF0) >> 4);

            return bit4;
        }

        public static byte[] bytesToBit4(byte[] data)
        {
            byte[] bit4 = new byte[data.length * 2];
            for (int i = 0; i < data.length; i++)
            {
                byte[] b4 = byteToBit4(data[i]);
                bit4[i * 2] = b4[0];
                bit4[i * 2 + 1] = b4[1];
            }
            return bit4;
        }

        // To Byte
        public static byte[] BitsToBytes(byte[] bits)
        {
            ArrayList<Byte> bytes = new ArrayList<>();

            for (int i = 0; i < bits.length; i+= 8)
            {
                byte newByte= 0;
                int b = 0;
                for (int j = 7; j >= 0; j--, b++)
                {
                    newByte+= (byte)(bits[i + b] << j);
                }
                bytes.add(newByte);
            }

            return toArray(bytes);
        }
        
        public static byte Bit4ToByte(byte[] data)
        {
            return (byte)(data[0] + (data[1] << 4));
        }
        
        public static byte Bit4ToByte(byte b1, byte b2)
        {
            return (byte)(b1 + (b2 << 4));
        }
        
        public static byte[] Bits4ToByte(byte[] data)
        {
            byte[] b = new byte[data.length / 2];

            for (int i = 0; i < data.length; i+= 2)
                b[i / 2] = Bit4ToByte(data[i], data[i + 1]);

            return b;
        }

        public static byte[] Bits4ToByte(Byte[] data)
        {
            byte[] b = new byte[data.length / 2];

            for (int i = 0; i < data.length; i+= 2)
                b[i / 2] = Bit4ToByte(data[i], data[i + 1]);

            return b;
        }
        
        //Custom
        
        public static byte[] toArray(Byte[] arr)
        {
            byte[] ret= new byte[arr.length];
            for(int i= 0; i < ret.length; i++)
            {
                ret[i]= arr[i];
            }

            return ret;
        }
        
        public static byte[] toArray(ArrayList<Byte> bytes)
        {
            return toArray(bytes.toArray(new Byte[0]));
        }
    }
}
