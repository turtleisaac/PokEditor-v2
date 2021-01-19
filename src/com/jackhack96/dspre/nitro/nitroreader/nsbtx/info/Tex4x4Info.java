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
public class Tex4x4Info {
    
    private final int sizeTex, ofsDict, flag;
    private final long vramKey, ofsTex, ofsTexPlttIdx;

    public Tex4x4Info(ByteReader data) {
        vramKey = data.getu32();
        sizeTex = data.getu16() << 3;
        ofsDict = data.getu16();
        flag = data.getu16();
        data.skip(2);
        ofsTex = data.getu32();
        ofsTexPlttIdx = data.getu32();
    }

    public long getVramKey() {
        return vramKey;
    }

    public int getSizeTex() {
        return sizeTex;
    }

    public int getOfsDict() {
        return ofsDict;
    }

    public int getFlag() {
        return flag;
    }

    public long getOfsTex() {
        return ofsTex;
    }

    public long getOfsTexPlttIdx() {
        return ofsTexPlttIdx;
    }
    
    
}
