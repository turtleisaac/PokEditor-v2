/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag;

import com.jackhack96.dspre.nitro.nitroreader.shared.ComparableTagEnum;
import java.util.HashMap;
import java.util.Map;

import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmRotVariable.NNS_G3D_JNTANM_RINFO.LAST_INTERP_MASK;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.STEP_1;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.STEP_2;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.STEP_4;
import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.tagComp;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;

public class JointAnmRotVariable extends JointAnmRot {

    public enum NNS_G3D_JNTANM_RINFO implements ComparableTagEnum {

        STEP_1(0x00000000),
        STEP_2(0x40000000),
        STEP_4(0x80000000),
        LAST_INTERP_MASK(0x1FFF0000);

        private final int value;

        private static Map<Integer, NNS_G3D_JNTANM_RINFO> map = new HashMap<>();

        static {
            for (NNS_G3D_JNTANM_RINFO cmd : NNS_G3D_JNTANM_RINFO.values()) {
                map.put(cmd.value, cmd);
            }
        }

        public int getValue() {
            return value;
        }

        private NNS_G3D_JNTANM_RINFO(int opcode) {
            this.value = opcode;
        }

        public static NNS_G3D_JNTANM_RINFO valueOf(int opcode) {
            return map.get(opcode);
        }

    }

    private long info;
    private long offset;

    public JointAnmRotVariable(ByteReader data) {
        info = data.getu32();
        offset = data.getu32();
    }

    @Override
    public float[][][] getData(ByteReader data, long JAC_offset, long ofsRot3, long ofsRot5, int numFrame) {
        boolean step1, step2, step4, last_interp_mask;
        step1 = tagComp(info, STEP_1);
        step2 = tagComp(info, STEP_2);
        step4 = tagComp(info, STEP_4);
        last_interp_mask = tagComp(info, LAST_INTERP_MASK);

        int step;

        if (step4) {
            step = 4;
        } else if (step2) {
            step = 2;
        } else if (step1) {
            step = 1;
        } else {
            throw new UnsupportedOperationException("NO FRAME STEP SPECIFIED");
        }

        //number of values stored in nsbca
        int valueCount = Utils.getValueCount(numFrame, step);
        float[][][] rotation_matrices = new float[valueCount][3][3];

        data.setIndex(JAC_offset + offset);
        for (int i = 0; i < valueCount; i++) {
            rotation_matrices[i] = readMatrix(data, JAC_offset, ofsRot3, ofsRot5, data.getu16());
        }

        return rotation_matrices;

    }

    @Override
    public int getFrameStep() {
        boolean step1, step2, step4;
        step1 = tagComp(info, STEP_1);
        step2 = tagComp(info, STEP_2);
        step4 = tagComp(info, STEP_4);

        if (step4) {
            return 4;
        } else if (step2) {
            return 2;
        } else if (step1) {
            return 1;
        } else {
            throw new UnsupportedOperationException("NO FRAME STEP SPECIFIED");
        }
    }
}
