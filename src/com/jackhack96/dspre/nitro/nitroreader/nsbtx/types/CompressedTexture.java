/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx.types;

import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.TRANSPARENT;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.avgColor;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.getColor2Mode3;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.getColor3Mode3;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Usuario
 */
public class CompressedTexture extends Texture {

    private int[][] indices;
    private int[][] modeMatrix;
    private int[][] paletteOffsets;

    public CompressedTexture(String name, int[][] indices, int[][] modeArray, int[][] paletteOffsets) {
        super(name);
        this.indices = indices;
        this.modeMatrix = modeArray;
        this.paletteOffsets = paletteOffsets;
//        for (int i = 0; i < indices.length; i++) {
//            for (int j = 0; j < indices[i].length; j++) {
//                System.out.print(String.format("%1d", indices[i][j]) + " ");
//            }
//            System.out.println("");
//        }
//        System.out.println("");
    }

    @Override
    public BufferedImage getImage(Palette palette) {
        if (palette != null) {
            int height = indices.length;
            int width = indices[0].length;
            Color[][] colors = new Color[height][width];
            Color[] paletteColors = palette.getColors();
            int paletteColorsIndex;

            Color[] blockColors = new Color[4];

            for (int i = 0; i < height; i += 4) {
                for (int j = 0; j < width; j += 4) {
                    paletteColorsIndex = paletteOffsets[i / 4][j / 4];
                    blockColors[0] = paletteColors[paletteColorsIndex];
                    blockColors[1] = paletteColors[paletteColorsIndex + 1];
                    switch (modeMatrix[i / 4][j / 4]) {
                        case 0:
                            blockColors[2] = paletteColors[paletteColorsIndex + 2];
                            blockColors[3] = TRANSPARENT;
                            break;
                        case 1:
                            blockColors[2] = avgColor(blockColors[0], blockColors[1]);
                            blockColors[3] = TRANSPARENT;
                            break;
                        case 2:
                            blockColors[2] = paletteColors[paletteColorsIndex + 2];
                            blockColors[3] = paletteColors[paletteColorsIndex + 3];
                            break;
                        case 3:
                            blockColors[2] = getColor2Mode3(blockColors[0], blockColors[1]);
                            blockColors[3] = getColor3Mode3(blockColors[0], blockColors[1]);
                            break;
                    }
                    for (int k = 0; k < 4; k++) {
                        for (int l = 0; l < 4; l++) {
                            colors[i + k][j + l] = blockColors[indices[i + k][j + l]];
                        }
                    }
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
