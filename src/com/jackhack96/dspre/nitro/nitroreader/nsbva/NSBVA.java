/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbva;

import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dfile;

/**
 *
 * @author Usuario
 */
public class NSBVA implements G3Dfile{
    private final VisAnmSet VIS0;

    public NSBVA(VisAnmSet VIS0) {
        this.VIS0 = VIS0;
    }
}
