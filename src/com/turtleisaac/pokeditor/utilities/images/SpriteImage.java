package com.turtleisaac.pokeditor.utilities.images;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class SpriteImage
{
    private byte[][] indexGuide;
    private Color[] palette;

    private BufferedImage storedImage;
    boolean update;

    public SpriteImage(Color[] palette)
    {
        indexGuide= new byte[80][80];
        byte[] arr= new byte[80];
        Arrays.fill(arr, (byte) 0);
        Arrays.fill(indexGuide,arr);

        this.palette= palette;

        update= true;
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

        update= true;
    }

    public SpriteImage(Image image, JPanel parent)
    {
        if(image.getWidth(null) != image.getHeight(null) && image.getWidth(null) != 80)
        {
            JOptionPane.showMessageDialog(parent,"This image is not 80x80","PokEditor",JOptionPane.ERROR_MESSAGE);
            return;
        }

        BufferedImage bufferedImage;
        if(image instanceof BufferedImage)
        {
            bufferedImage= (BufferedImage) image;
        }
        else
        {
            bufferedImage= new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
            Graphics2D bGr = bufferedImage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();
        }

        ArrayList<Color> colorList= new ArrayList<>();
        for(int row= 0; row < bufferedImage.getHeight(); row++)
        {
            for(int col= 0; col < bufferedImage.getWidth(); col++)
            {
                Color color= new Color(bufferedImage.getRGB(col,row));
                if(!colorList.contains(color))
                    colorList.add(color);
            }
        }

        if(colorList.size() > 16)
        {
            JOptionPane.showMessageDialog(parent,"This image is not indexed to 16 colors","PokEditor",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else
        {
            while(colorList.size() < 16)
            {
                colorList.add(Color.MAGENTA);
            }
        }

        indexGuide= new byte[80][80];

        for(int row= 0; row < indexGuide.length; row++)
        {
            for(int col= 0; col < indexGuide[row].length; col++)
            {
                indexGuide[row][col]= (byte) colorList.indexOf(new Color(bufferedImage.getRGB(col,row)));
            }
        }

        palette= colorList.toArray(new Color[0]);
        update= true;
    }

    /**
     * Creates a BufferedImage using <code>indexGuide</code> and <code>palette</code>
     * @return a BufferedImage representation of this <code>SpriteImage</code>
     */
    public BufferedImage getImage()
    {
        if(update)
        {
            storedImage= new BufferedImage(indexGuide[0].length, indexGuide.length,BufferedImage.TYPE_INT_RGB);
            for(int row= 0; row < indexGuide.length; row++)
            {
                for(int col= 0; col < indexGuide[row].length; col++)
                {
                    storedImage.setRGB(col,row,palette[indexGuide[row][col]].getRGB());
                }
            }
            update= false;
        }

        return storedImage;
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

    public SpriteImage indexSelf(JPanel parent)
    {
        SpriteImage newSprite= new SpriteImage(getImage(),parent);
        indexGuide= newSprite.getIndexGuide();
        setPalette(newSprite.getPalette());
        return this;
    }

    public SpriteImage updateColor(int index, Color replacement)
    {
        palette[index]= replacement;
        update= true;
        return this;
    }

    public SpriteImage replaceColor(Color toReplace, Color replacement)
    {
        for(int i= 0; i < palette.length; i++)
        {
            if(palette[i].equals(toReplace))
            {
                palette[i]= replacement;
                break;
            }
        }
        return this;
    }

    public SpriteImage replacePalette(Color[] paletteGuide, Color[] newPalette, JPanel parent)
    {
        indexSelf(parent);

        ArrayList<Color> colorGuideList= new ArrayList<>(Arrays.asList(paletteGuide));

        for(int i= 0; i < palette.length; i++)
        {
            for(int row= 0; row < indexGuide.length; row++)
            {
                for(int col= 0; col < indexGuide[row].length; col++)
                {
                    if(indexGuide[row][col] == i)
                        indexGuide[row][col]= (byte) -colorGuideList.indexOf(palette[i]);
                }
            }
        }

        for(int row= 0; row < indexGuide.length; row++)
        {
            for(int col= 0; col < indexGuide[row].length; col++)
            {
                indexGuide[row][col]= (byte) Math.abs(indexGuide[row][col]);
            }
        }

        palette= newPalette;
        update= true;
        return this;
    }

    public SpriteImage alignPalette(Color[] paletteGuide, JPanel parent)
    {
        indexSelf(parent);

        ArrayList<Color> colorGuideList= new ArrayList<>(Arrays.asList(paletteGuide));

        for(int i= 0; i < palette.length; i++)
        {
            for(int row= 0; row < indexGuide.length; row++)
            {
                for(int col= 0; col < indexGuide[row].length; col++)
                {
                    if(indexGuide[row][col] == i)
                        indexGuide[row][col]= (byte) -colorGuideList.indexOf(palette[i]);
                }
            }
        }

        for(int row= 0; row < indexGuide.length; row++)
        {
            for(int col= 0; col < indexGuide[row].length; col++)
            {
                indexGuide[row][col]= (byte) Math.abs(indexGuide[row][col]);
            }
        }

        update= true;
        return this;
    }

    /**
     * Creates a copy of the provided image using the palette of the SpriteImage object this method is executed from
     * @param image a SpriteImage to apply a palette to
     * @return a SpriteImage identical to the provided one except with a different palette
     */
    public SpriteImage createCopyWithPalette(SpriteImage image)
    {
        return new SpriteImage(image.getIndexGuide(),palette);
    }

    /**
     * Creates a copy of the provided image using the provided palette and the SpriteImage the method is executed from
     * @param palette a Color[] to apply to the copy of this SpriteImage
     * @return a SpriteImage with the image of the SpriteImage object this method is executed from but the provided palette applied
     */
    public SpriteImage createCopyWithImage(Color[] palette)
    {
        return new SpriteImage(indexGuide,palette);
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
        update= true;
        return this;
    }

    public byte[][] getIndexGuide()
    {
        return indexGuide;
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
