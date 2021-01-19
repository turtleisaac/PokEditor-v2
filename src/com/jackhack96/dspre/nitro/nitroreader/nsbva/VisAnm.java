/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbva;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class VisAnm {
    
//    private boolean getBit(byte[] visData, int pos){
//        
//    }
    
    List<VisibiltyAnimation> animations;

    public VisAnm(ByteReader data) {
        data.mark();

        String anmHeader = data.getStr(4);
        int numFrame = data.getu16();
        int numNode = data.getu16();
        int size = data.getu16();
        data.skip(2);
        
        animations = new ArrayList<>(numNode);
        
        for (int i = 0; i < numNode; i++) {
            animations.add(new VisibiltyAnimation(numFrame));
        }
        
        long visData = data.getu32();
        int count = 0;
        
        for (int i = 0; i < numFrame; i++) {
            for (int j = 0; j < numNode; j++) {
                animations.get(j).addFrame(Utils.flagComp(visData, 1));
                visData >>= 1;
                count++;
                if (count % 32 == 0){
                    visData = data.getu32();
                }
            }
        }
        
        System.out.println("");

        data.getMarkDelete();
    }

}
