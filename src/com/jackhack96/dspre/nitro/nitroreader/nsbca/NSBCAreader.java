/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dheader;
import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dreader;


public class NSBCAreader implements G3Dreader{
    
    ByteReader data;

    public NSBCAreader(ByteReader data) {
        this.data = data;
    }
    
//    public NSBCA getNSBCA(){
//        
//        NSBCAheader header = new NSBCAheader(data);
//        data.setIndex(header.getOffset());
//        
//        JointAnmSet JNT0 = new JointAnmSet(data);
//        
//        return new NSBCA(JNT0);
//    }

    @Override
    public NSBCA readFile() {
        
        G3Dheader header = new G3Dheader(data);
        data.setIndex(header.getOffsets()[0]);
        
        JointAnmSet JNT0 = new JointAnmSet(data);
        
        return new NSBCA(JNT0);
    }
    
}
