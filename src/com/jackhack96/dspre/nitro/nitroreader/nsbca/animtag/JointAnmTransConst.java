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
public class JointAnmTransConst extends JointAnmTrans {

    private final float const_trans;

    public JointAnmTransConst(ByteReader data) {
        const_trans = data.getfx32();//(float)data.getu32()/0x1000;
    }
    
    public JointAnmTransConst() {
        const_trans = 0;
    }

    @Override
    public float[] getData(ByteReader data, long JAC_offset, int numframes) {
        return new float[]{const_trans};
    }

    @Override
    public int getFrameStep() {
        return 1;
    }

}
