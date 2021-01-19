/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class JointAnm {

    private List<NodeAnimation> nodes;
    private int numFrames;

    public JointAnm(ByteReader data) {

        int JAC_offset = data.getIndex();

        String anmHeader = data.getStr(4);
        int numFrame = data.getu16();
        int numNode = data.getu16();
        long annFlag = data.getu32();
        long ofsRot3 = data.getu32();
        long ofsRot5 = data.getu32();
        List<Integer> ofsTag = new ArrayList<>(numNode);
        for (int i = 0; i < numNode; i++) {
            ofsTag.add(data.getu16());
        }

        data.padding(4);

        nodes = new ArrayList<>(numNode);
        for (Integer node_offset : ofsTag) {
            data.setIndex(JAC_offset + node_offset);
            nodes.add(new NodeAnimation(data, new TagData(data), JAC_offset, ofsRot3, ofsRot5, numFrame));
        }

        this.numFrames = numFrame;
    }

    public List<NodeAnimation> getNodes() {
        return nodes;
    }

    public int getNumFrames() {
        return numFrames;
    }

    

}
