/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx.types;

import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.TRANSPARENT;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Usuario
 */
public class IndexTexture extends Texture {

    private int[][] colorIndices;
    private boolean hasAlpha;

    public IndexTexture(String name, int[][] colorIndices, boolean hasAlpha) {
        super(name);
        this.colorIndices = colorIndices;
        this.hasAlpha = hasAlpha;
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

    @Override
    public BufferedImage getImage(Palette palette) {
        if (palette != null) {
            int height = colorIndices.length;
            int width = colorIndices[0].length;
            Color[][] colors = new Color[height][width];
            Color[] paletteColors = palette.getColors();

            Color color0 = paletteColors[0];
            if (hasAlpha) {
                paletteColors[0] = TRANSPARENT;
            }

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    colors[i][j] = paletteColors[colorIndices[i][j]];
                }
            }

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    image.setRGB(j, i, colors[i][j].getRGB());
                }
            }

            paletteColors[0] = color0;

            return image;
        } else {
            return null;
        }
    }

}
