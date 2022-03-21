/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.getIdentityMatrix;

/**
 *
 * @author Usuario
 */
public class JointAnmRotConst extends JointAnmRot{

    private final long const_offset;

    public JointAnmRotConst(ByteReader data) {
        const_offset = data.getu32();
    }

    public JointAnmRotConst() {
        const_offset = -1;
    }

    @Override
    public float[][][] getData(ByteReader data, long JAC_offset, long ofsRot3, long ofsRot5, int numframes){
        if (const_offset == -1){
            return new float[][][]{getIdentityMatrix()};
        }
        data.setIndex(JAC_offset + const_offset);
        return new float[][][]{readMatrix(data, JAC_offset, ofsRot3, ofsRot5, const_offset)};
    }

    @Override
    public int getFrameStep() {
        return 1;
    }
    
    
}
