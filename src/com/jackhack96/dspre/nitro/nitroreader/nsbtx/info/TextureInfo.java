/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx.info;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;

/**
 *
 * @author Usuario
 */
public class TextureInfo {
    
    private final TexInfo texInfo;
    private final Tex4x4Info tex4x4Info;
    private final PlttInfo plttInfo;

    public TextureInfo(ByteReader data) {
        texInfo = new TexInfo(data);
        tex4x4Info = new Tex4x4Info(data);
        plttInfo = new PlttInfo(data);
    }

    public TexInfo getTexInfo() {
        return texInfo;
    }

    public Tex4x4Info getTex4x4Info() {
        return tex4x4Info;
    }

    public PlttInfo getPlttInfo() {
        return plttInfo;
    }
    
    
    
    
    
}
