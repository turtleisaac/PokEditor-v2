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
public class AlphaTexture extends Texture {

    private int[][] colorIndices;
    private int[][] alpha;

    public AlphaTexture(String name, int[][] colorIndices, int[][] alpha) {
        super(name);
        this.colorIndices = colorIndices;
        this.alpha = alpha;
    }

    public boolean hasAlpha() {
        return true;
    }

    @Override
    public BufferedImage getImage(Palette palette) {
        if (palette != null) {
            int height = colorIndices.length;
            int width = colorIndices[0].length;
            Color[][] colors = new Color[height][width];
            Color[] paletteColors = palette.getColors();

            Color currentColor;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    currentColor = paletteColors[colorIndices[i][j]];
                    colors[i][j] = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alpha[i][j]);
                }
            }

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image.setRGB(j, i, colors[i][j].getRGB());
                }
            }

            return image;
        } else {
            return null;
        }
    }
}
