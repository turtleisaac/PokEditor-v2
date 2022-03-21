/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx.types;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Usuario
 */
public class DirectTexture extends Texture {

    private Color[][] colors;

    public DirectTexture(String name, Color[][] colors) {
        super(name);
        this.colors = colors;
/*
        int width = colors[0].length;
        int height = colors.length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j, i, colors[i][j].getRGB());
            }
        }

        File outputfile = new File(name + ".png");
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(DirectTexture.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }

    @Override
    public BufferedImage getImage(Palette palette) {
        int height = colors.length;
        int width = colors[0].length;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j, i, colors[i][j].getRGB());
            }
        }
        
        return image;
    }

}
