/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbma;

import com.jackhack96.dspre.nitro.nitroreader.shared.ComparableTagEnum;

import static com.jackhack96.dspre.nitro.nitroreader.nsbma.MatColAnm.NNS_G3D_MATCANM_OPTION.END_TO_START_INTERPOLATION;
import static com.jackhack96.dspre.nitro.nitroreader.nsbma.MatColAnm.NNS_G3D_MATCANM_OPTION.INTERPOLATION;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Dictionary20byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry20byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Usuario
 */
public class MatColAnm {
    
    public enum NNS_G3D_MATCANM_OPTION implements ComparableTagEnum{

        INTERPOLATION(0x0001),
        END_TO_START_INTERPOLATION(0x0002);

        private final int value;

        private static Map<Integer, NNS_G3D_MATCANM_OPTION> map = new HashMap<>();

        static {
            for (NNS_G3D_MATCANM_OPTION cmd : NNS_G3D_MATCANM_OPTION.values()) {
                map.put(cmd.value, cmd);
            }
        }

        public int getValue() {
            return value;
        }

        private NNS_G3D_MATCANM_OPTION(int value) {
            this.value = value;
        }

        public static NNS_G3D_MATCANM_OPTION valueOf(int value) {
            return map.get(value);
        }

    }
    
    private List<MaterialAnimation> materialAnimations;
    private boolean interpolation, end_to_start_interpolation;
    
    public MatColAnm(ByteReader data) {
        data.mark();
        
        String anmHeader = data.getStr(4);
        int numFrame = data.getu16();
        int flag = data.getu16();
        
        interpolation = Utils.tagComp(flag, INTERPOLATION);
        end_to_start_interpolation = Utils.tagComp(flag, END_TO_START_INTERPOLATION);
        
        Dictionary20byte dict = new Dictionary20byte(data);
        List<DictionaryEntry20byte> entries = dict.getEntries();
        
        materialAnimations = new ArrayList<>(entries.size());
        
        for (DictionaryEntry20byte entry : entries) {
            data.resetMark();
            materialAnimations.add(new MaterialAnimation(data, numFrame, entry));
        }
        
        data.getMarkDelete();
    }
    
    
    
}
