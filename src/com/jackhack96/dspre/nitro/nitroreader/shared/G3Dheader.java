/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.shared;

/**
 *
 * @author Usuario
 */
public class G3Dheader {

    private int byteOrder, version, headerSize, dataBlocks;
    private long signature, fileSize;
    private long[] offsets;

    public G3Dheader(ByteReader data) {
        signature = data.getu32();
        byteOrder = data.getu16();
        version = data.getu16();
        fileSize = data.getu32();
        headerSize = data.getu16();
        dataBlocks = data.getu16();
        /*if (dataBlocks != 1) {
            throw new IllegalArgumentException("dataBlocks != 1");
        }*/
        offsets = new long[dataBlocks];
        for (int i = 0; i < dataBlocks; i++) {
            offsets[i] = data.getu32();
        }
    }

    public long getFileSize() {
        return fileSize;
    }

    public long[] getOffsets() {
        return offsets;
    }

    public long getSignature() {
        return signature;
    }

    public int getByteOrder() {
        return byteOrder;
    }

    public int getVersion() {
        return version;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public int getDataBlocks() {
        return dataBlocks;
    }

}
