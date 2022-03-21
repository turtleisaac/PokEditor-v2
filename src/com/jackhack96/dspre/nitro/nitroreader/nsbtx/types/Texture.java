/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx.types;

import java.awt.image.BufferedImage;

/**
 *
 * @author Usuario
 */
public abstract class Texture {

    private String name;

    public Texture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
    
    
    public abstract BufferedImage getImage(Palette palette);
    
    
    
}
