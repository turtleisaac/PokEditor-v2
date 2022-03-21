/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbta;

import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dfile;

/**
 *
 * @author Usuario
 */
public class NSBTA implements G3Dfile{
    private final TexSRTAnmSet SRT0;

    public NSBTA(TexSRTAnmSet SRT0) {
        this.SRT0 = SRT0;
    }

    public TexSRTAnmSet getSRT0() {
        return SRT0;
    }
    
    
    
}
