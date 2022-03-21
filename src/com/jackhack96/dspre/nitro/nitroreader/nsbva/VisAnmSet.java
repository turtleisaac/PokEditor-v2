/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbva;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Dictionary4byte;
import com.jackhack96.dspre.nitro.nitroreader.shared.DictionaryEntry4byte;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class VisAnmSet {
    
    private List<VisAnm> visAnimations;

    public VisAnmSet(ByteReader data) {
        data.mark();
        
        String kind = data.getStr(4);
        long size = data.getu32();
        
        Dictionary4byte dict = new Dictionary4byte(data);
        List<DictionaryEntry4byte> entries = dict.getEntries();
        visAnimations = new ArrayList<>(entries.size());
        
        for (DictionaryEntry4byte entry : entries) {
            data.resetMark();
            data.skip(entry.getOffset());
            
            //System.out.print(String.format("%08X", entry.getOffset()));
            //System.out.println("\t"+entry.getName());
            
            visAnimations.add(new VisAnm(data));
            
        }
    }
    
    
    
}
