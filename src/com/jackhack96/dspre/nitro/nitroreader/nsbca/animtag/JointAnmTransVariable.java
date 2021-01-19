/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag;

import com.jackhack96.dspre.nitro.nitroreader.shared.ComparableTagEnum;
import java.util.HashMap;
import java.util.Map;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;

import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.FX16ARRAY;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.LAST_INTERP_MASK;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.STEP_1;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.STEP_2;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable.NNS_G3D_JNTANM_TINFO.STEP_4;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.tagComp;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;

/**
 *
 * @author Usuario
 */
public class JointAnmTransVariable extends JointAnmTrans {

    public enum NNS_G3D_JNTANM_TINFO implements ComparableTagEnum {

        STEP_1(0x00000000),
        STEP_2(0x40000000),
        STEP_4(0x80000000),
        FX16ARRAY(0x20000000),
        LAST_INTERP_MASK(0x1FFF0000);

        private final int value;

        private static Map<Integer, NNS_G3D_JNTANM_TINFO> map = new HashMap<>();

        static {
            for (NNS_G3D_JNTANM_TINFO cmd : NNS_G3D_JNTANM_TINFO.values()) {
                map.put(cmd.value, cmd);
            }
        }

        @Override
        public int getValue() {
            return value;
        }

        private NNS_G3D_JNTANM_TINFO(int opcode) {
            this.value = opcode;
        }

        public static NNS_G3D_JNTANM_TINFO valueOf(int opcode) {
            return map.get(opcode);
        }

    }

    private long info;
    private long offset;

    public JointAnmTransVariable(ByteReader data) {
        info = data.getu32();
        offset = data.getu32();
    }

    @Override
    public float[] getData(ByteReader data, long JAC_offset, int numFrame) {
        boolean step1, step2, step4, fx16array, last_interp_mask;
        step1 = tagComp(info, STEP_1);
        step2 = tagComp(info, STEP_2);
        step4 = tagComp(info, STEP_4);
        fx16array = tagComp(info, FX16ARRAY);
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
        float[] values = new float[valueCount];

        data.setIndex(JAC_offset + offset);
        if (fx16array) {
            for (int i = 0; i < valueCount; i++) {
                values[i] = data.getfx16();//(float) data.getu16() / 0x1000;
            }
        } else {
            for (int i = 0; i < valueCount; i++) {
                values[i] = data.getfx32();//(float) data.getu32() / 0x1000;
            }
        }

        return values;

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
