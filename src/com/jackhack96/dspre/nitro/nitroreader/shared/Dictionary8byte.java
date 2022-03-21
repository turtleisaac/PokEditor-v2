
package com.jackhack96.dspre.nitro.nitroreader.shared;

import java.util.ArrayList;
import java.util.List;

public class Dictionary8byte {
    
    private List<DictionaryEntry8byte> entries = new ArrayList<>();

    public List<DictionaryEntry8byte> getEntries() {
        return entries;
    }

    public Dictionary8byte(ByteReader data){
        
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
        
        if (element_size != 8){
            System.err.println("Wrong element_size "+element_size);
            throw new UnsupportedOperationException("Wrong element_size");
            //exit(0);
        }
        
        for (int j = 0; j < count; j++) {
            entries.add(new DictionaryEntry8byte(data.getu32(),data.getu32()));
        }
        
        for (int j = 0; j < count; j++) {
            entries.get(j).setName(data.getStr(0x10));
        }
     
    }
    
}
