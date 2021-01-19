/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca;

import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry4byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.Dictionary4byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class JointAnmSet {
    
    private List<JointAnm> animations;
    
    public JointAnmSet(ByteReader data) {
        data.mark();
        
        String kind = data.getStr(4);
        long size = data.getu32();
        
        Dictionary4byte dict = new Dictionary4byte(data);
        List<DictionaryEntry4byte> entries = dict.getEntries();
        animations = new ArrayList<>(entries.size());
        
        for (DictionaryEntry4byte entry : entries) {
            data.setIndex(data.getMark());
            data.skip(entry.getOffset());
            
            //System.out.print(String.format("%08X", entry.getOffset()));
            //System.out.println("\t"+entry.getName());
            
            animations.add(new JointAnm(data));
            
        }
        
    }

    public List<JointAnm> getAnimations() {
        return animations;
    }
    
    
    
}
