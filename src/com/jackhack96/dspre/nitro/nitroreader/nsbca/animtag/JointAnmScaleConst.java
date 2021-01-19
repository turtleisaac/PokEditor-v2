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
public class JointAnmScaleConst extends JointAnmScale{

    private final float const_scale;
    private final float const_invScale;

    public JointAnmScaleConst(ByteReader data) {
        const_scale = data.getfx32();//(float)data.getu32()/0x1000;
        const_invScale = data.getfx32();//(float)data.getu32()/0x1000;
    }

    public JointAnmScaleConst() {
        this.const_scale = 1.0f;
        this.const_invScale = 1.0f;
    }

    @Override
    public float[] getData(ByteReader data, long JAC_offset, int numframes) {
        return new float[]{const_scale};
    }
    
    @Override
    public int getFrameStep() {
        return 1;
    }
    
    
}
