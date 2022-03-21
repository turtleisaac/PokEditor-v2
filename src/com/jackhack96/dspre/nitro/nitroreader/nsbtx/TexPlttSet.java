/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx;

import com.jackhack96.dspre.nitro.nitroreader.nsbtx.types.Palette;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.types.Texture;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.info.TextureInfo;
import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Dictionary4byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.Dictionary8byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry4byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry8byte;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jackhack96.dspre.nitro.renderer.ImageUtils;

/**
 *
 * @author Usuario
 */
public class TexPlttSet {

    private List<Texture> textures;
    private List<Palette> palettes;

    private void calculatePaletteSize(List<PaletteEntry> paletteEntries, int sizePltt) {
        if (paletteEntries.size() < 2) {
            throw new IllegalArgumentException("NO PALETTES TO SORT");
        }

        PaletteEntry current, next;
        long size;
        boolean found = false;

        
        for (int i = 0; i < paletteEntries.size() - 1; i++) {
            current = paletteEntries.get(i);
            next = paletteEntries.get(i + 1);
            size = next.getOffset() - current.getOffset();
            if (size > 0) {
                current.setSize(size);
            } else if (size == 0) {
                for (int j = i + 2; j < paletteEntries.size() - 1; j++) {
                    next = paletteEntries.get(j);
                    size = next.getOffset() - current.getOffset();
                    if (size > 0) {
                        current.setSize(size);
                        found = true;
                    }
                }
                if (!found) {
                    size = sizePltt - current.getOffset();
                    current.setSize(size);
                }
                found = false;
            }
        }

        paletteEntries.get(paletteEntries.size() - 1).setSize(sizePltt - paletteEntries.get(paletteEntries.size() - 1).getOffset());
    }

    public TexPlttSet(ByteReader data) /*throws IOException */ {
        data.mark();

        String kind = data.getStr(4);
        long size = data.getu32();

        TextureInfo textureInfo = new TextureInfo(data);

        data.setIndex(data.getMark() + textureInfo.getTexInfo().getOfsDict());
        Dictionary8byte dictTex = new Dictionary8byte(data);
        data.setIndex(data.getMark() + textureInfo.getPlttInfo().getOfsDict());
        Dictionary4byte dictPltt = new Dictionary4byte(data);

        List<DictionaryEntry8byte> dictTexEntries = dictTex.getEntries();
        List<DictionaryEntry4byte> dictPlttEntries = dictPltt.getEntries();

        textures = new ArrayList<>(dictTexEntries.size());
        palettes = new ArrayList<>(dictPlttEntries.size());

        TexPlttReader texPlttReader = new TexPlttReader(data, textureInfo);

        // read texture data
        for (DictionaryEntry8byte dictTexEntry : dictTexEntries) {
            textures.add(texPlttReader.readTexture(data, dictTexEntry.getName(), dictTexEntry.getParam1()));
        }

        //generate palette entries from dictionary entries
        List<PaletteEntry> paletteEntries = new ArrayList<>(dictPlttEntries.size());
        for (DictionaryEntry4byte dictPlttEntry : dictPlttEntries) {
            paletteEntries.add(new PaletteEntry(dictPlttEntry.getName(), (dictPlttEntry.getOffset() % 0x10000) << 3));
        }

        // sort palette entries by offset
        Collections.sort(paletteEntries);
        if (paletteEntries.size() > 1) {
            calculatePaletteSize(paletteEntries, textureInfo.getPlttInfo().getSizePltt());
        } else if (paletteEntries.size() > 0) {
            paletteEntries.get(0).setSize(textureInfo.getPlttInfo().getSizePltt());
        }

        // get actual palette data
        long ofsPlttData = textureInfo.getPlttInfo().getOfsPlttData() + data.getMark();
        for (PaletteEntry paletteEntry : paletteEntries) {
            data.setIndex(ofsPlttData + paletteEntry.getOffset());
            palettes.add(new Palette(data, paletteEntry.getName(), paletteEntry.getSize()));
        }

//        int id = 0;
//        for (Texture texture : textures) {
//            File outputfile = new File(texture.getName()+"_"+palettes.get(id).getName() + ".png");
//            ImageIO.write(texture.getImage(palettes.get(id)), "png", outputfile);
//            id++;
//        }
    }

    public BufferedImage getImage(String texName, String palName) {
        Texture tex = null;
        Palette pal = null;
        if (texName != null) {
            for (Texture texture : textures) {
                if (texName.equals(texture.getName())) {
                    tex = texture;
                    break;
                }
            }
        }
        if (palName != null) {
            for (Palette palette : palettes) {
                if (palName.equals(palette.getName())) {
                    pal = palette;
                    break;
                }
            }
        }

        if (tex != null) {
            BufferedImage img = tex.getImage(pal);
            if (img != null) {
                return img;
            }
        }
        return ImageUtils.generateDefaultImg(16, 16, Color.red, Color.white);
    }

}
