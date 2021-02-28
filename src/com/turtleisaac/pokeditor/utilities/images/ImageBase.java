package com.turtleisaac.pokeditor.utilities.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.images.ncer.NcerData;
import com.turtleisaac.pokeditor.utilities.images.ncer.NcerReader;
import com.turtleisaac.pokeditor.utilities.images.ncgr.NcgrData;
import com.turtleisaac.pokeditor.utilities.images.ncgr.NcgrReader;
import com.turtleisaac.pokeditor.utilities.images.nclr.NclrData;
import com.turtleisaac.pokeditor.utilities.images.nclr.NclrReader;

import javax.imageio.ImageIO;

public class ImageBase
{
    NclrData nclr;
    NcgrData ncgr;
    NcerData ncer;
    Project project;

    protected String fileName;
    protected int id = -1;
    boolean loaded;

    byte[] original;
    int startByte;
    int zoom = 1;

    byte[] tiles;
    byte[] tilePal;
    int width, height;
    ColorFormat format;
    TileFormat tileForm;
    int tile_size;      // Pixels height
    int bpp;
    boolean canEdit;

    NcerData.Bank[] banks;
    long blockSize;

    public ImageBase()
    {

    }

    public ImageBase(Project project, String ncgr, String nclr) throws IOException
    {
        this.project= project;
        String dataPath= project.getProjectPath().getAbsolutePath() + File.separator + project.getName() + "/data";

        try
        {
            this.ncgr= NcgrReader.readFile(dataPath + ncgr,this);
            this.nclr= NclrReader.readFile(dataPath + nclr);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public ImageBase(Project project, String ncgr, String nclr, String ncer) throws IOException
    {
        this.project= project;
        String dataPath= project.getProjectPath().getAbsolutePath() + File.separator + project.getName() + "/data";

        try
        {
            this.ncgr= NcgrReader.readFile(dataPath + ncgr,this);
            this.nclr= NclrReader.readFile(dataPath + nclr);
            this.ncer= NcerReader.readFile(dataPath + ncer);
            banks= this.ncer.getCellBank().getBanks();
            blockSize= this.ncer.getCellBank().getBlockSize();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public ImageBase(Project project, NcgrData ncgr, String nclr) throws IOException
    {
        this.project= project;
        this.ncgr= ncgr;
        String dataPath= project.getProjectPath().getAbsolutePath() + File.separator + project.getName() + "/data";
        if(!new File(dataPath + nclr).getParentFile().exists())
        {
            Narctowl narctowl= new Narctowl(true);
            if(project.getBaseRom() == Game.Platinum || project.getBaseRom() == Game.Diamond || project.getBaseRom() == Game.Pearl)
                narctowl.unpack(dataPath + "/poketool/trgra/trfgra.narc",dataPath + "/poketool/trgra/trfgra");
            else if(project.getBaseRom() == Game.HeartGold || project.getBaseRom() == Game.SoulSilver)
                narctowl.unpack(dataPath + "/a/0/0/4",dataPath + "/a/0/0/4_");
        }
        new File(dataPath + "/poketool/trgra/trfgra").deleteOnExit();
        new File(dataPath + "/a/0/0/4_").deleteOnExit();

        this.ncgr= NcgrReader.readFile(dataPath + ncgr,this);
        this.nclr= NclrReader.readFile(dataPath + nclr);
    }


    public ImageBase(String projectPath, String ncgr, String nclr) throws IOException
    {
        String dataPath= projectPath + File.separator + "Platinum" + "/data";
        if(!new File(dataPath + "/poketool/trgra/trfgra").exists())
        {
            Narctowl narctowl= new Narctowl(true);
            narctowl.unpack(dataPath + "/poketool/trgra/trfgra.narc",dataPath + "/poketool/trgra/trfgra");
        }
        new File(dataPath + "/poketool/trgra/trfgra").deleteOnExit();
        this.ncgr= NcgrReader.readFile(dataPath + ncgr,this);
        this.nclr= NclrReader.readFile(dataPath + nclr);
    }
    
    public ImageBase(byte[] tiles, int width, int height, ColorFormat format, TileFormat tileForm, boolean editable, String fileName)
    {
        this.fileName = fileName;
        setTiles(tiles, width, height, format, tileForm, editable);
    }

    public ImageBase(String file, int id, String fileName)
    {
        this.id = id;
//        if (fileName == "")
//            this.fileName = Path.GetFileName(file);
//        else
//            this.fileName = fileName;

//        Read(file);
    }




    public void setBanks(NcerData.Bank[] banks, long blockSize, boolean editable)
    {
        this.banks = banks;
        this.blockSize = blockSize;
        this.canEdit = editable;
        loaded = true;
    }




    public Image getImage(int height, int width)
    {
        Color[][] pal_colors= nclr.getPalette().getPalettes();

        if(width == 0)
            width= 64;
        if(height == 0)
            height= 96;


        byte[] img_tiles;
        if (tileForm == TileFormat.Horizontal)
        {
            if (height < tile_size) height = tile_size;
            {
                img_tiles = ImageActions.linealToHorizontal(tiles, width, height, bpp, tile_size);
            }
            tilePal = ImageActions.linealToHorizontal(tilePal, width, height, 8, tile_size);
        }
        else
            img_tiles = tiles;

        return ImageActions.getImage(img_tiles, tilePal, pal_colors, format, width, height);
    }

    public Image getImage()
    {
        Color[][] pal_colors= nclr.getPalette().getPalettes();

        if(width == 0)
            width= 64;
        if(height == 0)
            height= 96;


        byte[] img_tiles;
        if (tileForm == TileFormat.Horizontal)
        {
            if (height < tile_size) height = tile_size;
            {
                img_tiles = ImageActions.linealToHorizontal(tiles, width, height, bpp, tile_size);
            }
            tilePal = ImageActions.linealToHorizontal(tilePal, width, height, 8, tile_size);
        }
        else
            img_tiles = tiles;

        return ImageActions.getImage(img_tiles, tilePal, pal_colors, format, width, height);
    }

    public Image getImageTransparent(Color transparent, int height, int width)
    {
        Color[][] pal_colors= nclr.getPalette().getPalettes();
        this.height= height;
        this.width= width;
//
//        for(Color[] arr : pal_colors)
//        {
//            for(Color color : arr)
//            {
//                System.out.print("[r=" + color.getRed() + ",g=" + color.getGreen() + ",b=" + color.getBlue() + "], ");
//            }
//            System.out.println("\n");
//        }

        if(width == 0)
            width= 64;
        if(height == 0)
            height= 96;


        byte[] img_tiles;
        if (tileForm == TileFormat.Horizontal)
        {
            if (height < tile_size) height = tile_size;
            {
//                System.out.println("Shifting img");
//                System.out.println(Arrays.toString(tiles) + "\n");
                img_tiles = ImageActions.linealToHorizontal(tiles, width, height, bpp, tile_size);
//                System.out.println(Arrays.toString(img_tiles));
            }
            tilePal = ImageActions.linealToHorizontal(tilePal, width, height, 8, tile_size);
        }
        else
            img_tiles = tiles;

        pal_colors[0][0]= transparent;

        return ImageActions.getImage(img_tiles, tilePal, pal_colors, format, width, height);
    }

    public BufferedImage getImage(int num, Color background)
    {
        return ImageActions.getImage(banks[num],blockSize,this, nclr.getPalette(), 80,80,true,1,null, background);
    }

    public BufferedImage[] getImages(Color background)
    {
        System.out.println("-----------------------------");
        ArrayList<BufferedImage> images= new ArrayList<>();
        for (int i= 0; i < banks.length; i++)
        {
            BufferedImage image= ImageActions.getImage(banks[i], blockSize, this, nclr.getPalette(), 80, 80, true, 1, null, background);

            int value= 0;
            for(int row= 0; row < image.getHeight(); row++)
            {
                for(int col= 0; col < image.getWidth(); col++)
                {
                    value^= image.getRGB(col,row);
                }
            }

            if(value != 0)
                images.add(image);
        }
        return images.toArray(new BufferedImage[0]);
    }

    public BufferedImage getImage(NclrData.Palette palette)
    {
        Color[][] pal_colors = palette.getPalettes();


        byte[] img_tiles;
        if (tileForm == TileFormat.Horizontal)
        {
            if (height < tile_size)
                height= tile_size;

            img_tiles = ImageActions.linealToHorizontal(tiles, width, height, bpp, tile_size);
            tilePal = ImageActions.linealToHorizontal(tilePal, width, height, 8, tile_size);
        }
        else
            img_tiles = tiles;

        return ImageActions.getImage(img_tiles, tilePal, pal_colors, format, width, height);
    }

    public Image getPalette()
    {
        Color[][] pal_colors= nclr.getPalette().getPalettes();
        return ImageActions.getPalette(pal_colors,format);
    }

    public BufferedImage[] getNcerImage()
    {
        Color[][] pal_colors= nclr.getPalette().getPalettes();
        CenterImage[] out= new CenterImage[ncer.getCellBank().getBanks().length];

        for(int i= 0; i < out.length; i++)
        {
            NcerData.Bank bank= ncer.getCellBank().getBanks()[i];
            CenterImage oamIndex= new CenterImage(80,80,BufferedImage.TYPE_INT_RGB);

            for(int j= 0; j < bank.getOAMs().length; j++)
            {
                NcerData.OAM oam= bank.getOAMs()[j];

                width= oam.getWidth();
                System.out.println("Width: " + width);
                height= oam.getHeight();
                System.out.println("Height: " + height);

                int xStart= (int) oam.getObj1().getXOffset();
                System.out.println("X Origin: " + xStart);
                int yStart= oam.getObj0().getYOffset();
                System.out.println("Y Origin: " + yStart);

                long start= bank.getPartitionOffset();
                long size= bank.getPartitionSize();

                byte[] img_tiles;
                if (tileForm == TileFormat.Horizontal)
                {
                    if(height < tile_size) height = tile_size;
                    {
                        img_tiles = ImageActions.linealToHorizontal(Arrays.copyOfRange(tiles,(int)start,(int)(start+size)), width, height, bpp, tile_size);
                    }
                    tilePal = ImageActions.linealToHorizontal(Arrays.copyOfRange(tilePal,(int)start,(int)(start+size)), width, height, 8, tile_size);
                }
                else
                    img_tiles = tiles;


                BufferedImage image= ImageActions.getImage(img_tiles, tilePal, pal_colors, format, width, height);
                try
                {
                    ImageIO.write(oamIndex,"png",new File(System.getProperty("user.dir") + File.separator + i + "_" + j + ".png"));
                } catch (IOException exception)
                {
                    exception.printStackTrace();
                }

//                for(int row= yStart; Math.abs(Math.max(row,yStart))-Math.abs(Math.min(row,yStart)) < height; row++)
//                {
//                    for(int col= xStart; col-xStart < width; col++)
//                    {
//                        int rgb= image.getRGB(col-xStart,row-yStart);
//
//                        //Translate values
//                        int globalRow= oam.getHeight()/2 + row;
//                        int globalCol= oam.getWidth()/2 + col;
//
//                        oamIndex.setRGB(globalCol,globalRow,rgb);
//                    }
//                }

//                int originalX= 0;
//                int originalY= 0;
//                for(int row= yStart; row < yStart + oam.getHeight(); row++)
//                {
//                    for(int col= xStart; col < xStart + oam.getWidth(); col++)
//                    {
//                        int rgb= image.getRGB(originalX++,originalY);
//
//                        System.out.print(col - oam.getWidth()/2 + ", ");
//                        System.out.println(row - oam.getHeight()/2);
//                        oamIndex.setRGB(col,row,rgb);
//                    }
//                    originalY++;
//                    originalX= 0;
//                }

                try
                {
                    oamIndex.setSection(xStart,yStart,image);
                }
                catch (ArrayIndexOutOfBoundsException ignored)
                {

                }
                System.out.println();
            }

            try
            {
                ImageIO.write(oamIndex,"png",new File(System.getProperty("user.dir") + File.separator + i + ".png"));
            } catch (IOException exception)
            {
                exception.printStackTrace();
            }
            System.out.println();

        }

        return out;
    }

    public BufferedImage[] getNcerImage2()
    {
        Color[][] pal_colors= nclr.getPalette().getPalettes();
        BufferedImage[] out= new BufferedImage[ncer.getCellBank().getBanks().length];

        for(int i= 0; i < out.length; i++)
        {
            NcerData.Bank bank= ncer.getCellBank().getBanks()[i];
            BufferedImage oamIndex= new CenterImage(80,80,BufferedImage.TYPE_INT_RGB);

            for(int j= 0; j < bank.getOAMs().length; j++)
            {
                NcerData.OAM oam= bank.getOAMs()[j];

                byte[] img_tiles;
                if (tileForm == TileFormat.Horizontal)
                {
                    if (height < tile_size) height = tile_size;
                    {
                        img_tiles = ImageActions.linealToHorizontal(tiles, width, height, bpp, tile_size);
                    }
                    tilePal = ImageActions.linealToHorizontal(tilePal, width, height, 8, tile_size);
                }
                else
                    img_tiles = tiles;

                img_tiles= ImageActions.getOAMData(oam,img_tiles,format);

                ImageActions.getImage(img_tiles, tilePal, pal_colors, format, width, height);
            }
        }

        return out;
    }

    private int translate(int coordinate, int length)
    {
        return coordinate + length/2;
    }

    public Image getPaletteColor(int i)
    {
        BufferedImage image= new BufferedImage(40,16,BufferedImage.TYPE_INT_RGB);
        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
                image.setRGB(col,row,nclr.getPalette().getPalettes()[0][i].getRGB());
            }
        }
        return image;
    }

//    public void Read(String fileIn);
//    public void Write(String fileOut, PaletteBase palette);

    public void changeStartByte(int start)
    {
        if (start < 0 || start >= original.length)
            return;

        startByte = start;

        tiles = new byte[original.length - start];
        System.arraycopy(original,start,tiles,0,tiles.length);
        tilePal = new byte[tiles.length * (tile_size / bpp)];
    }

    public void setTiles(byte[] tiles, int width, int height, ColorFormat format, TileFormat form, boolean editable)
    {
        setTiles(tiles,width,height,format,form,editable,8);
    }

    public void setTiles(byte[] tiles, int width, int height, ColorFormat format, TileFormat form, boolean editable, int tile_size)
    {
        this.tiles = tiles;
        this.format = format;
        this.tileForm = form;
        this.canEdit = editable;
        this.tile_size = tile_size;
        this.width= width;
        this.height= height;

        width = width;
        height = height;

        zoom = 1;
        //startByte = 0;
        loaded = true;

        bpp = 8;
        if (format == ColorFormat.colors16)
            bpp = 4;
        else if (format == ColorFormat.colors2)
            bpp = 1;
        else if (format == ColorFormat.colors4)
            bpp = 2;
        else if (format == ColorFormat.direct)
            bpp = 16;
        else if (format == ColorFormat.BGRA32 || format == ColorFormat.ABGR32)
            bpp = 32;

        tilePal = new byte[tiles.length * (tile_size / bpp)];

        // Get the original data for changes in startByte
        original= Arrays.copyOf(tiles,tiles.length);
    }

    public void setTiles(ImageBase new_img)
    {
        this.tiles = new_img.getTiles();
        this.format = new_img.getFormat();
        this.tileForm = new_img.getTileFormat();
        this.tile_size = new_img.getTile_size();

        width = new_img.width;
        height = new_img.height;

        zoom = 1;
        startByte = 0;
        loaded = true;

        bpp = 8;
        if (format == ColorFormat.colors16)
            bpp = 4;
        else if (format == ColorFormat.colors2)
            bpp = 1;
        else if (format == ColorFormat.colors4)
            bpp = 2;
        else if (format == ColorFormat.direct)
            bpp = 16;
        else if (format == ColorFormat.BGRA32 || format == ColorFormat.ABGR32)
            bpp = 32;

        tilePal = new byte[tiles.length * (tile_size / bpp)];

        // Get the original data for changes in startByte
        original= Arrays.copyOf(tiles,tiles.length);
    }

    public void setTiles(byte[] tiles)
    {
        this.tiles = tiles;

        zoom = 1;
        startByte = 0;
        loaded = true;

        tilePal = new byte[tiles.length * (tile_size / bpp)];

        // Get the original data for changes in startByte
        original= Arrays.copyOf(tiles,tiles.length);
    }

    
    public int getId()
    {
        return id;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    public void setFileName(String fileName)
    {
        this.fileName= fileName;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }

    public boolean canEdit()
    {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit)
    {
        this.canEdit = canEdit;
    }

    public int getZoom()
    {
        return zoom;
    }

    public void setZoom(int zoom)
    {
        this.zoom = zoom;
    }

    public int getStartByte()
    {
        return startByte;
    }

    public void setStartByte(int startByte)
    {
        this.startByte = startByte;
    }

    public int getheight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
        if (tileForm == TileFormat.Horizontal || tileForm == TileFormat.Vertical)
        {
            if (this.height < this.tile_size) this.height = this.tile_size;
            if (this.height % this.tile_size != 0)
                this.height += this.tile_size - (this.height % this.tile_size);
        }
    }

    public int getwidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
        if (tileForm == TileFormat.Horizontal || tileForm == TileFormat.Vertical)
        {
            if (this.width < this.tile_size) this.width = this.tile_size;
            if (this.width % this.tile_size != 0)
                this.width += this.tile_size - (this.width % this.tile_size);
        }
    }

    public ColorFormat getFormat()
    {
        return format;
    }

    public void setFormat(ColorFormat format)
    {
        this.format = format;
        byte[] arr= new byte[tiles.length * (tile_size / bpp)];
        System.arraycopy(tilePal,0,arr,0,tilePal.length);
        tilePal= arr;
    }

    public TileFormat getTileFormat()
    {
        return tileForm;
    }

    public void setTileFormat(TileFormat tileForm)
    {
        this.tileForm = tileForm;
    }

    public byte[] getTiles()
    {
        return tiles;
    }

    public byte[] getTilePal()
    {
        return tilePal;
    }

    public void setTilePal(byte[] tilePal)
    {
        this.tilePal = tilePal;
    }

    public int getBpp()
    {
        return bpp;
    }

    public void setBpp(int bpp)
    {
        this.bpp = bpp;
    }

    public int getTile_size()
    {
        return tile_size;
    }

    public void setTile_size(int tile_size)
    {
        this.tile_size = tile_size;
        byte[] arr= new byte[tiles.length * (tile_size / bpp)];
        System.arraycopy(tilePal,0,arr,0,tilePal.length);
        tilePal= arr;
    }

    public byte[] getOriginal()
    {
        return original;
    }

    public Color[][] getPaletteArr()
    {
        return nclr.getPalette().getPalettes();
    }
}
