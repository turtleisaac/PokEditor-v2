package com.turtleisaac.pokeditor.utilities.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SpriteImage
{
    private byte[][] indexGuide;
    private Color[] palette;

    public SpriteImage(Color[] palette)
    {
        indexGuide= new byte[80][80];
        byte[] arr= new byte[80];
        Arrays.fill(arr, (byte) 0);
        Arrays.fill(indexGuide,arr);

        this.palette= palette;
    }

    /**
     * Constructs a <code>SpriteImage</code>
     * @param indexGuide a byte[][] representing the index in the palette to pull the color from for each pixel in the sprite
     * @param palette a Color[] of length 16 or less containing the colors to be used in the image
     */
    public SpriteImage(byte[][] indexGuide, Color[] palette)
    {
        this.indexGuide = indexGuide;
        this.palette= palette;
    }

    /**
     * Creates a BufferedImage using <code>indexGuide</code> and <code>palette</code>
     * @return a BufferedImage representation of this <code>SpriteImage</code>
     */
    public BufferedImage getImage()
    {
        BufferedImage ret= new BufferedImage(indexGuide[0].length, indexGuide.length,BufferedImage.TYPE_INT_RGB);
        for(int row= 0; row < indexGuide.length; row++)
        {
            for(int col= 0; col < indexGuide[row].length; col++)
            {
                ret.setRGB(col,row,palette[indexGuide[row][col]].getRGB());
            }
        }

        return ret;
    }

    /**
     * Creates a BufferedImage with a transparent background using <code>indexGuide</code> and <code>palette</code>
     * @return a BufferedImage representation of this <code>SpriteImage</code>
     */
    public BufferedImage getTransparentImage()
    {
        BufferedImage ret= new BufferedImage(indexGuide[0].length, indexGuide.length,BufferedImage.TYPE_INT_ARGB);
        for(int row= 0; row < indexGuide.length; row++)
        {
            for(int col= 0; col < indexGuide[row].length; col++)
            {
                if(indexGuide[row][col] != 0)
                    ret.setRGB(col,row,palette[indexGuide[row][col]].getRGB());
            }
        }

        return ret;
    }

    /**
     * Creates a scaled BufferedImage using <code>indexGuide</code> and <code>palette</code>
     * @return a scaled BufferedImage representation of this <code>SpriteImage</code>
     */
    public BufferedImage getResizedImage()
    {
        BufferedImage image= new BufferedImage(indexGuide[0].length, indexGuide.length,BufferedImage.TYPE_INT_RGB);
        for(int row= 0; row < indexGuide.length; row++)
        {
            for(int col= 0; col < indexGuide[row].length; col++)
            {
                image.setRGB(col,row,palette[indexGuide[row][col]].getRGB());
            }
        }

        BufferedImage resizedImage = new BufferedImage(160, 160, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, 160, 160, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public int getWidth()
    {
        return indexGuide[0].length;
    }

    public int getHeight()
    {
        return indexGuide.length;
    }

    public Color[] getPalette()
    {
        return palette;
    }

    public SpriteImage setPalette(Color[] palette)
    {
        this.palette = palette;
        return this;
    }

    public byte[][] getIndexGuide()
    {
        return indexGuide;
    }

    public SpriteImage updateColor(int index, Color replacement)
    {
        palette[index]= replacement;
        return this;
    }

    public int getCoordinateValue(int x, int y)
    {
        return indexGuide[y][x];
    }

    /**
     * Combines two <code>SpriteImage</code> objects to product a single <code>SpriteImage</code>
     * @param image1 the master <code>SpriteImage</code>, its palette is to be used by the composite <code>SpriteImage</code>
     * @param image2 the slave <code>SpriteImage</code>, its palette is thrown out
     * @return a composite <code>SpriteImage</code> composed of <code>image1</code> and <code>image2</code> side by side
     */
    public static SpriteImage getCompositeImage(SpriteImage image1, SpriteImage image2)
    {
        byte[][] ret= new byte[image1.getHeight()][image1.getWidth() + image2.getWidth()];

        for(int row= 0; row < image1.getHeight(); row++)
        {
            System.arraycopy(image1.getIndexGuide()[row],0,ret[row],0,image1.getWidth());
            System.arraycopy(image2.getIndexGuide()[row],0,ret[row],image1.getWidth(),image2.getWidth());
        }

        return new SpriteImage(ret,image1.getPalette());
    }
}
