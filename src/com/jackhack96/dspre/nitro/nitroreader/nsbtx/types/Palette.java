/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx.types;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;
import java.awt.Color;

/**
 *
 * @author Usuario
 */
public class Palette {
    
    private Color[] colors;
    private String name;

    public Palette(ByteReader data, String name, long size) {
        this.name = name;
        colors = new Color[(int)size / 2];
        
        for (int i = 0; i < size/2; i++) {
            colors[i] = Utils.u16ToColor(data.getu16());
        }
        
    }

    public Color[] getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }
    
    
    
    
    
}
