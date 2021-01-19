/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbtx;

import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dfile;

/**
 *
 * @author Usuario
 */
public class NSBTX implements G3Dfile{
    private final TexPlttSet TEX0;

    public NSBTX(TexPlttSet TEX0) {
        this.TEX0 = TEX0;
    }

    public TexPlttSet getTEX0() {
        return TEX0;
    }
    
    
}
