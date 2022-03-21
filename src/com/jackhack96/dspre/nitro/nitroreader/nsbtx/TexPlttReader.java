/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx;

import com.jackhack96.dspre.nitro.nitroreader.nsbtx.info.TextureInfo;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.types.AlphaTexture;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.types.DirectTexture;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.types.CompressedTexture;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.types.IndexTexture;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.types.Texture;
import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.bitPosCheck;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.expand31To255;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.expand7To255;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.u16ToColorAlpha;
import java.awt.Color;

/**
 *
 * @author Usuario
 */
public class TexPlttReader {

    private int last4x4offset = -1;
    private final long ofsTex, ofs4x4Tex, ofsPlttData; //ABSOLUTE INDICES
    private final ByteReader tex4x4IdxData;
    

    public TexPlttReader(ByteReader data, TextureInfo textureInfo) {
        ofsTex = textureInfo.getTexInfo().getOfsTex() + data.getMark();
        ofs4x4Tex = textureInfo.getTex4x4Info().getOfsTex() + data.getMark();
        ofsPlttData = textureInfo.getPlttInfo().getOfsPlttData() + data.getMark();

        if (textureInfo.getTex4x4Info().getSizeTex() > 0) {
            tex4x4IdxData = new ByteReader(data.getbArray());
            tex4x4IdxData.setIndex(textureInfo.getTex4x4Info().getOfsTexPlttIdx() + data.getMark());
        } else {
            tex4x4IdxData = null;
        }
    }

    // FORMAT 1
    private AlphaTexture readA3I5(ByteReader data, String name, int height, int width) {
        int[][] colorIndices = new int[height][width];
        int[][] alpha = new int[height][width];

        int value;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                value = data.getu8();
                colorIndices[i][j] = value & 0x1F;
                alpha[i][j] = expand7To255(value >> 5);
            }
        }

        return new AlphaTexture(name, colorIndices, alpha);
    }

    // FORMAT 2
    private IndexTexture read4Color(ByteReader data, String name, int height, int width, boolean hasAlpha) {
        int[][] colorIndices = new int[height][width];
        int[][] alpha = null;

        int value;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j += 4) {
                value = data.getu8();
                colorIndices[i][j] = value & 0x3;
                colorIndices[i][j + 1] = (value & 0xC) >> 2;
                colorIndices[i][j + 2] = (value & 0x30) >> 4;
                colorIndices[i][j + 3] = (value & 0xC0) >> 6;
            }
        }

        return new IndexTexture(name, colorIndices, hasAlpha);
    }

    // FORMAT 3
    private IndexTexture read16Color(ByteReader data, String name, int height, int width, boolean hasAlpha) {
        int[][] colorIndices = new int[height][width];
        int[][] alpha = null;

        int value;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j += 2) {
                value = data.getu8();
                colorIndices[i][j] = value & 0xF;
                colorIndices[i][j + 1] = (value & 0xF0) >> 4;
            }
        }

        return new IndexTexture(name, colorIndices, hasAlpha);
    }

    // FORMAT 4
    private IndexTexture read256Color(ByteReader data, String name, int height, int width, boolean hasAlpha) {
        int[][] colorIndices = new int[height][width];
        int[][] alpha = null;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                colorIndices[i][j] = data.getu8();
            }
        }

        return new IndexTexture(name, colorIndices, hasAlpha);
    }

    // FORMAT 5
    private CompressedTexture readTexel4x4(ByteReader data, String name, int height, int width) {
        int[][] indices = new int[height][width];
        int[][] modeMatrix = new int[height / 4][width / 4];
        int[][] paletteOffsets = new int[height / 4][width / 4];
        int indexData;
        long tex4x4Data;

        for (int i = 0; i < height; i += 4) {
            for (int j = 0; j < width; j += 4) {
                tex4x4Data = data.getu32();
                indexData = tex4x4IdxData.getu16();
                paletteOffsets[i / 4][j / 4] = (indexData & 0x3FFF) * 2; // *2 because palettes are u16 arrays
                modeMatrix[i / 4][j / 4] = indexData >> 14;
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 4; l++) {
                        indices[i + k][j + l] = Utils.getBits(tex4x4Data, 2, (l + k * 4) * 2);
                    }
                }
            }
        }

        return new CompressedTexture(name, indices, modeMatrix, paletteOffsets);
    }

    // FORMAT 6
    private AlphaTexture readA5I3(ByteReader data, String name, int height, int width) {
        int[][] colorIndices = new int[height][width];
        int[][] alpha = new int[height][width];

        int value;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                value = data.getu8();
                colorIndices[i][j] = value & 0x7;
                alpha[i][j] = expand31To255(value >> 3);
            }
        }

        return new AlphaTexture(name, colorIndices, alpha);
    }
    
    // FORMAT 7
    private DirectTexture readDirect(ByteReader data, String name, int height, int width) {
        Color[][] colors = new Color[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                colors[i][j] = u16ToColorAlpha(data.getu16());
            }
        }

        return new DirectTexture(name, colors);
    }

    public Texture readTexture(ByteReader data, String name, long texImageParam) {
        int format = (int)(texImageParam >> 26) & 0x7;
        int height = (int)(texImageParam >> 23) & 0x7;
        int width = (int)(texImageParam >> 20) & 0x7;
        int offset = (int)(texImageParam & 0xFFFF) << 3;
        boolean hasAlpha = bitPosCheck(texImageParam, 29);

        width = 8 << width;
        height = 8 << height;
        
        //System.out.println("\n\n");
        
        switch (format) {
            case 1:
                data.setIndex(ofsTex + offset);
                return readA3I5(data, name, height, width);
            case 2:
                data.setIndex(ofsTex + offset);
                return read4Color(data, name, height, width, hasAlpha);
            case 3:
                data.setIndex(ofsTex + offset);
                return read16Color(data, name, height, width, hasAlpha);
            case 4:
                data.setIndex(ofsTex + offset);
                return read256Color(data, name, height, width, hasAlpha);
            case 5:
                if (offset < last4x4offset) {
                    throw new UnsupportedOperationException("4x4-Texel Textures not sorted");
                }
                last4x4offset = offset;
                data.setIndex(ofs4x4Tex + offset);
                return readTexel4x4(data, name, height, width);
            case 6:
                data.setIndex(ofsTex + offset);
                return readA5I3(data, name, height, width);
            case 7:
                data.setIndex(ofsTex + offset);
                return readDirect(data, name, height, width);
            default:
                throw new IllegalArgumentException("UNKNOWN FORMAT " + format);
        }
        
    }

}
