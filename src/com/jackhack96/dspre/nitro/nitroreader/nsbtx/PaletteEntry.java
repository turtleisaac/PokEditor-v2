/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx;

/**
 *
 * @author Usuario
 */
public class PaletteEntry implements Comparable<PaletteEntry> {

    private final String name;
    private final long offset;
    private long size;

    public PaletteEntry(String name, long offset) {
        this.name = name;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }
    

    public void setSize(long size) {
        this.size = size;
    }
    
    @Override
    public int compareTo(PaletteEntry o) {
        if (o.offset > offset) {
            return -1;
        } else if (offset > o.offset) {
            return 1;
        } else {
            return 0;
        }
    }

}
