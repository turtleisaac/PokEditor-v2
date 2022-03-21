/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.shared;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Dictionary40byte {
    
    private List<DictionaryEntry40byte> entries = new ArrayList<>();

    public List<DictionaryEntry40byte> getEntries() {
        return entries;
    }

    public Dictionary40byte(ByteReader data){
        
        int count, element_size;
               
        data.getu8();
        count = data.getu8();
        data.skip(10);
        //data.skip(8);
        for (int j = 0; j < count; j++) {
            data.skip(4);
        }
        element_size = data.getu16();
        data.skip(2);
        
        if (element_size != 40){
            throw new UnsupportedOperationException("Wrong element_size");
        }
        
        for (int j = 0; j < count; j++) {
            entries.add(new DictionaryEntry40byte(
                    data.getu32(),data.getu32(),data.getu32(),data.getu32(),data.getu32(),
                    data.getu32(),data.getu32(),data.getu32(),data.getu32(),data.getu32())
            );
        }
        
        for (int j = 0; j < count; j++) {
            entries.get(j).setName(data.getStr(0x10));
        }
     
    }
    
}
