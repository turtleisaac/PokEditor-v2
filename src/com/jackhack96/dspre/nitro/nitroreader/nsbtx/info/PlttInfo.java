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
public class PlttInfo {
    
    private final int sizePltt, ofsDict, flag;
    private final long vramKey, ofsPlttData;

    public PlttInfo(ByteReader data) {
        vramKey = data.getu32();
        sizePltt = data.getu16() << 3;
        flag = data.getu16();
        ofsDict = data.getu16();
        data.skip(2);
        ofsPlttData = data.getu32();
    }

    public long getVramKey() {
        return vramKey;
    }

    public int getSizePltt() {
        return sizePltt;
    }

    public int getOfsDict() {
        return ofsDict;
    }

    public int getFlag() {
        return flag;
    }

    public long getOfsPlttData() {
        return ofsPlttData;
    }
    
    
    
}
