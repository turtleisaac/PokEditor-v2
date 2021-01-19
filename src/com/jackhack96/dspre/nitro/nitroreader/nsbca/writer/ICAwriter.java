/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca.writer;

import java.util.List;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.JointAnm;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.NSBCA;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.NodeAnimation;

public class ICAwriter {

    private NSBCA nsbca;

    public ICAwriter(NSBCA nsbca) {
        this.nsbca = nsbca;
    }
    
    

    private void writeICA(JointAnm animation){
        for (NodeAnimation node : animation.getNodes()) {
            //node.
        }
    }
    
    public void writeICAs() {
        List<JointAnm> animations = nsbca.getJNT0().getAnimations();

        for (JointAnm animation : animations) {

        }
    }

}
