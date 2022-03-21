/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;

/**
 *
 * @author Usuario
 */
public abstract class JointAnmScale {
    
    public abstract float[] getData(ByteReader data, long JAC_offset, int numframes);
    
    public abstract int getFrameStep();
}
