/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbta;

import com.jackhack96.dspre.nitro.nitroreader.shared.ComparableTagEnum;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry40byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;

import static com.jackhack96.dspre.nitro.nitroreader.nsbta.TextureAnimation.NNS_G3D_TEXSRTANM_ELEM.CONST;
import static com.jackhack96.dspre.nitro.nitroreader.nsbta.TextureAnimation.NNS_G3D_TEXSRTANM_ELEM.FX16;
import static com.jackhack96.dspre.nitro.nitroreader.nsbta.TextureAnimation.NNS_G3D_TEXSRTANM_ELEM.STEP_1;
import static com.jackhack96.dspre.nitro.nitroreader.nsbta.TextureAnimation.NNS_G3D_TEXSRTANM_ELEM.STEP_2;
import static com.jackhack96.dspre.nitro.nitroreader.nsbta.TextureAnimation.NNS_G3D_TEXSRTANM_ELEM.STEP_4;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.tagComp;
import java.util.HashMap;
import java.util.Map;

public class TextureAnimation {

    public enum NNS_G3D_TEXSRTANM_ELEM implements ComparableTagEnum {

        STEP_1(0x00000000),
        STEP_2(0x40000000),
        STEP_4(0x80000000),
        CONST(0x20000000),
        FX16(0x10000000),
        LAST_INTERP_MASK(0x0000FFFF);

        private final int value;

        private static Map<Integer, NNS_G3D_TEXSRTANM_ELEM> map = new HashMap<>();

        static {
            for (NNS_G3D_TEXSRTANM_ELEM cmd : NNS_G3D_TEXSRTANM_ELEM.values()) {
                map.put(cmd.value, cmd);
            }
        }

        @Override
        public int getValue() {
            return value;
        }

        private NNS_G3D_TEXSRTANM_ELEM(int opcode) {
            this.value = opcode;
        }

        public static NNS_G3D_TEXSRTANM_ELEM valueOf(int opcode) {
            return map.get(opcode);
        }

    }

    private float[] scaleS, scaleT, rot, transS, transT;
    private int ssStep, stStep, rStep, tsStep, ttStep;
    String name;

    private int readStep(long flags){
        boolean step1, step2, step4;

        step1 = tagComp(flags, STEP_1);
        step2 = tagComp(flags, STEP_2);
        step4 = tagComp(flags, STEP_4);
        
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
    
    // READ SCALE OR TRANSLATION DATA
    private float[] readSTChannelData(ByteReader data, int numFrame, long flags, long Ex, int step) {
        boolean step1, step2, step4, isConst, fx16;

        /*
        step1 = tagComp(flags, STEP_1);
        step2 = tagComp(flags, STEP_2);
        step4 = tagComp(flags, STEP_4);*/
        isConst = tagComp(flags, CONST);
        fx16 = tagComp(flags, FX16);

        if (isConst) {
            if (fx16) {
                return new float[]{(float) (Ex % 0x10000) / 0x1000};
            }
            return new float[]{(float) Ex / 0x1000};
        }

        /*
        int step;
        if (step4) {
            step = 4;
        } else if (step2) {
            step = 2;
        } else if (step1) {
            step = 1;
        } else {
            throw new UnsupportedOperationException("NO FRAME STEP SPECIFIED");
        }*/

        int valueCount = Utils.getValueCount(numFrame, step);
        float[] values = new float[valueCount];

        data.skip(Ex);

        if (fx16) {
            for (int i = 0; i < valueCount; i++) {
                values[i] = data.getfx16();//(float) data.gets16() / 0x1000;
            }
        } else {
            for (int i = 0; i < valueCount; i++) {
                values[i] = data.getfx32();//(float) data.gets32() / 0x1000;
            }
        }

        data.resetMark();

        return values;

    }

    private float u32ToAngle(long value) {
        float sin = (float) ((short) (value % 0x10000) / 0x1000);
        float cos = (float) value / 0x1000;
        return (float) Math.toDegrees(Math.atan2(sin, cos));
    }

    private float getAngle(short sin, short cos) {
        //float sin = (float) ((short)(value % 0x10000) / 0x1000);
        //float cos = (float) value / 0x1000;
        return (float) Math.toDegrees(Math.atan2((float) sin / 0x1000, (float) cos / 0x1000));
    }

    private float[] readRotationData(ByteReader data, int numFrame, long rot, long rotEx, int step) {
        boolean step1, step2, step4, isConst, fx16;

        /*
        step1 = tagComp(rot, STEP_1);
        step2 = tagComp(rot, STEP_2);
        step4 = tagComp(rot, STEP_4);*/
        isConst = tagComp(rot, CONST);
        fx16 = tagComp(rot, FX16);

        if (fx16) {
            throw new UnsupportedOperationException("FX16 NOT ALLOWED IN ROTATION ???");
        }

        if (isConst) {
            //return new float[]{u32ToAngle(rotEx)};
            return new float[]{getAngle((short) (rotEx & 0xFFFF), (short) ((rotEx >> 16) & 0xFFFF))};
        }

        /*
        int step;

        if (step4) {
            step = 4;
        } else if (step2) {
            step = 2;
        } else if (step1) {
            step = 1;
        } else {
            throw new UnsupportedOperationException("NO FRAME STEP SPECIFIED");
        }*/

        int valueCount = Utils.getValueCount(numFrame, step);
        float[] values = new float[valueCount];

        data.skip(rotEx);

        for (int i = 0; i < valueCount; i++) {
            //values[i] = data.getu32();
            values[i] = getAngle(data.gets16(), data.gets16());
        }

        data.resetMark();

        return values;

    }

    public TextureAnimation(ByteReader data, int numFrame, DictionaryEntry40byte entry) {
        long scaleS = entry.getParam1();
        long scaleSEx = entry.getParam2();
        long scaleT = entry.getParam3();
        long scaleTEx = entry.getParam4();
        long rot = entry.getParam5();
        long rotEx = entry.getParam6();
        long transS = entry.getParam7();
        long transSEx = entry.getParam8();
        long transT = entry.getParam9();
        long transTEx = entry.getParam10();

        data.resetMark();

        this.ssStep = readStep(scaleS);
        this.stStep = readStep(scaleT);
        this.rStep = readStep(rot);
        this.tsStep = readStep(transS);
        this.ttStep = readStep(transT);
        
        this.scaleS = readSTChannelData(data, numFrame, scaleS, scaleSEx,ssStep);
        this.scaleT = readSTChannelData(data, numFrame, scaleT, scaleTEx,stStep);
        this.rot = readRotationData(data, numFrame, rot, rotEx,rStep);
        this.transS = readSTChannelData(data, numFrame, transS, transSEx,tsStep);
        this.transT = readSTChannelData(data, numFrame, transT, transTEx,ttStep);
        
        this.name = entry.getName();
    }

    public String getName() {
        return name;
    }

    public float[] getScaleS() {
        return scaleS;
    }

    public float[] getScaleT() {
        return scaleT;
    }

    public float[] getRot() {
        return rot;
    }

    public float[] getTransS() {
        return transS;
    }

    public float[] getTransT() {
        return transT;
    }

    public int getSsStep() {
        return ssStep;
    }

    public int getStStep() {
        return stStep;
    }

    public int getrStep() {
        return rStep;
    }

    public int getTsStep() {
        return tsStep;
    }

    public int getTtStep() {
        return ttStep;
    }

    
    
    
}
