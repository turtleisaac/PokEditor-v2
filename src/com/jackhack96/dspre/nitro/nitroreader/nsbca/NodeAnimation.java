/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca;

import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;
import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmRot;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmScale;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTrans;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.transposeMatrix;

public class NodeAnimation {

    private float[] tx, ty, tz, sx, sy, sz;
    private float[][][] r;
    private int txStep, tyStep, tzStep, sxStep, syStep, szStep, rStep;

    public NodeAnimation(ByteReader data, TagData tags, long JAC_offset, long ofsRot3, long ofsRot5, int numFrame) {
        JointAnmTrans tx, ty, tz;
        JointAnmRot r;
        JointAnmScale sx, sy, sz;
        
        tx = tags.getTx();
        ty = tags.getTy();
        tz = tags.getTz();
        r = tags.getR();
        sx = tags.getSx();
        sy = tags.getSy();
        sz = tags.getSz();
        
        this.tx = tx.getData(data, JAC_offset, numFrame);
        this.ty = ty.getData(data, JAC_offset, numFrame);
        this.tz = tz.getData(data, JAC_offset, numFrame);
        
        this.r = r.getData(data, JAC_offset, ofsRot3, ofsRot5, numFrame);
        
        this.sx = sx.getData(data, JAC_offset, numFrame);
        this.sy = sy.getData(data, JAC_offset, numFrame);
        this.sz = sz.getData(data, JAC_offset, numFrame);
        
        
        txStep = tx.getFrameStep();
        tyStep = ty.getFrameStep();
        tzStep = tz.getFrameStep();
        rStep = r.getFrameStep();
        sxStep = sx.getFrameStep();
        syStep = sy.getFrameStep();
        szStep = sz.getFrameStep();
        
    }

    public float[] getTx() {
        return tx;
    }

    public float[] getTy() {
        return ty;
    }

    public float[] getTz() {
        return tz;
    }

    public float[] getSx() {
        return sx;
    }

    public float[] getSy() {
        return sy;
    }

    public float[] getSz() {
        return sz;
    }

    public float[][][] getR() {
        return r;
    }

    public int getTxStep() {
        return txStep;
    }

    public int getTyStep() {
        return tyStep;
    }

    public int getTzStep() {
        return tzStep;
    }

    public int getSxStep() {
        return sxStep;
    }

    public int getSyStep() {
        return syStep;
    }

    public int getSzStep() {
        return szStep;
    }

    public int getrStep() {
        return rStep;
    }

    public void showData(){
        
        System.out.println("Scale");
        
        System.out.println("\tScale X");
        System.out.print("\t");
        for (int i = 0; i < sx.length; i++) {
            System.out.print(sx[i]+" ");
        }
        System.out.println("");
        
        System.out.println("\tScale Y");
        System.out.print("\t");
        for (int i = 0; i < sy.length; i++) {
            System.out.print(sy[i]+" ");
        }
        System.out.println("");
        
        System.out.println("\tScale Z");
        System.out.print("\t");
        for (int i = 0; i < sz.length; i++) {
            System.out.print(sz[i]+" ");
        }
        System.out.println("");
        System.out.println("");
        
        
        System.out.println("Translation");
        
        System.out.println("\tTranslation X");
        System.out.print("\t");
        for (int i = 0; i < tx.length; i++) {
            System.out.print(tx[i]+" ");
        }
        System.out.println("");
        
        System.out.println("\tTranslation Y");
        System.out.print("\t");
        for (int i = 0; i < ty.length; i++) {
            System.out.print(ty[i]+" ");
        }
        System.out.println("");
        
        System.out.println("\tTranslation Z");
        System.out.print("\t");
        for (int i = 0; i < tz.length; i++) {
            System.out.print(tz[i]+" ");
        }
        System.out.println("");
        System.out.println("");
        
        
        System.out.println("Rotation");
        float[] rotation;
        for (int i = 0; i < r.length; i++) {
            rotation = Utils.getEulerRotation(transposeMatrix(r[i]));
            //rotation = Utils.getEulerRotation(r[i]);
            System.out.println("("+rotation[0]+" "+rotation[1]+" "+rotation[2]+") ");
        }
        System.out.println("");
        System.out.println("");
        
    }
    
    

}
