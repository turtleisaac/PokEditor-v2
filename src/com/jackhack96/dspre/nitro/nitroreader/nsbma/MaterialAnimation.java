/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbma;

import com.jackhack96.dspre.nitro.nitroreader.shared.ComparableTagEnum;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry20byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;

import static com.jackhack96.dspre.nitro.nitroreader.nsbma.MaterialAnimation.NNS_G3D_MATCANM_ELEM.CONST;
import static com.jackhack96.dspre.nitro.nitroreader.nsbma.MaterialAnimation.NNS_G3D_MATCANM_ELEM.STEP_1;
import static com.jackhack96.dspre.nitro.nitroreader.nsbma.MaterialAnimation.NNS_G3D_MATCANM_ELEM.STEP_2;
import static com.jackhack96.dspre.nitro.nitroreader.nsbma.MaterialAnimation.NNS_G3D_MATCANM_ELEM.STEP_4;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.tagComp;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.u16ToColor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialAnimation {
    
    public enum NNS_G3D_MATCANM_ELEM implements ComparableTagEnum{

        STEP_1(0x00000000),
        STEP_2(0x40000000),
        STEP_4(0x80000000),
        CONST(0x20000000),
        LAST_INTERP_MASK(0x1FFF0000),
        OFFSET_CONSTANT_MASK(0x0000FFFF);

        private final int value;

        private static Map<Integer, NNS_G3D_MATCANM_ELEM> map = new HashMap<>();

        static {
            for (NNS_G3D_MATCANM_ELEM cmd : NNS_G3D_MATCANM_ELEM.values()) {
                map.put(cmd.value, cmd);
            }
        }

        public int getValue() {
            return value;
        }

        private NNS_G3D_MATCANM_ELEM(int value) {
            this.value = value;
        }

        public static NNS_G3D_MATCANM_ELEM valueOf(int value) {
            return map.get(value);
        }

    }
    
    private List<Color> readColorChannel(ByteReader data, int numFrame, long tag){
        List<Color> colors;
                
        boolean step1, step2, step4, isConst;
        isConst = tagComp(tag, CONST);
        
        if (isConst){
            colors = new ArrayList<>(1);
            colors.add(u16ToColor((int)tag % 0x10000));
            return colors;
        }
        
        step1 = tagComp(tag, STEP_1);
        step2 = tagComp(tag, STEP_2);
        step4 = tagComp(tag, STEP_4);
        
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
        
        int offset = (int)(tag % 0x10000);
        int valueCount = Utils.getValueCount(numFrame, step);
        colors = new ArrayList<>(valueCount);
                
        data.skip(offset);
        
        for (int i = 0; i < valueCount; i++) {
            colors.add(u16ToColor(data.getu16()));
        }
        
        data.resetMark();
        
        return colors;
        
    }
    
    private List<Integer> readAlphaChannel(ByteReader data, int numFrame, long tag){
        List<Integer> alpha;
                
        boolean step1, step2, step4, isConst;
        isConst = tagComp(tag, CONST);
        
        if (isConst){
            alpha = new ArrayList<>(1);
            alpha.add((int)tag % 0x100);
            return alpha;
        }
        
        step1 = tagComp(tag, STEP_1);
        step2 = tagComp(tag, STEP_2);
        step4 = tagComp(tag, STEP_4);
        
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
        
        int offset = (int)tag % 0x10000;
        int valueCount = Utils.getValueCount(numFrame, step);
        alpha = new ArrayList<>(valueCount);
                
        data.skip(offset);
        
        
        for (int i = 0; i < valueCount; i++) {
            alpha.add(data.getu8());
        }
        
        data.resetMark();
        
        return alpha;
        
    }
    
    List<Color> diffuse, ambient, specular, emission;
    List<Integer> alpha;
    
    public MaterialAnimation(ByteReader data, int numframes, DictionaryEntry20byte entry) {
        long tagDiffuse = entry.getParam1();
        long tagAmbient = entry.getParam2();
        long tagSpecular = entry.getParam3();
        long tagEmission = entry.getParam4();
        long tagPolygonAlpha = entry.getParam5();
        
        diffuse = readColorChannel(data, numframes, tagDiffuse);
        ambient = readColorChannel(data, numframes, tagAmbient);
        specular = readColorChannel(data, numframes, tagSpecular);
        emission = readColorChannel(data, numframes, tagEmission);
        alpha = readAlphaChannel(data, numframes, tagPolygonAlpha);
        
    }

}
