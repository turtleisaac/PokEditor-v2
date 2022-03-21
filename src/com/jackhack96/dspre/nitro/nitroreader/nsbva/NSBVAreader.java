/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbva;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dfile;
import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dheader;
import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dreader;

/**
 *
 * @author Usuario
 */
public class NSBVAreader implements G3Dreader{
    private final ByteReader data;

    public NSBVAreader(ByteReader data) {
        this.data = data;
    }
    
    @Override
    public G3Dfile readFile() {
        G3Dheader header = new G3Dheader(data);
        data.setIndex(header.getOffsets()[0]);
        
        VisAnmSet VIS0 = new VisAnmSet(data);
        return new NSBVA(VIS0);
    }
    
    
}
