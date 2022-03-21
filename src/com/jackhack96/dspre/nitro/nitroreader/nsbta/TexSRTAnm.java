/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbta;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Dictionary40byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry40byte;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class TexSRTAnm {
    
    private List<TextureAnimation> textureAnimations;
    private int numFrames;
    
    public TexSRTAnm(ByteReader data) {
        data.mark();
        
        String anmHeader = data.getStr(4);
        this.numFrames = data.getu16();
        int flag = data.getu8();
        int texMtxMode = data.getu8();
        
        Dictionary40byte dict = new Dictionary40byte(data);
        List<DictionaryEntry40byte> entries = dict.getEntries();
                
        textureAnimations = new ArrayList<>(entries.size());
        
        for (DictionaryEntry40byte entry : entries) {
            textureAnimations.add(new TextureAnimation(data, numFrames, entry));
        }
        
        
        data.getMarkDelete();
        
    }

    public List<TextureAnimation> getTextureAnimations() {
        return textureAnimations;
    }

    public int getNumFrames() {
        return numFrames;
    }
    
    
    
    
}
