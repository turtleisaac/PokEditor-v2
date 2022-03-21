/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca;

import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dfile;

/**
 *
 * @author Usuario
 */
public class NSBCA implements G3Dfile{
    
    private JointAnmSet JNT0;

    public NSBCA(JointAnmSet JNT0) {
        this.JNT0 = JNT0;
    }

    public JointAnmSet getJNT0() {
        return JNT0;
    }
    
    
    
}
