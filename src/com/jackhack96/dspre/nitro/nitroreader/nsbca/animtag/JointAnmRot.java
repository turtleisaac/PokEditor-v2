/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag;

import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.crossProduct;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.flagComp;

/**
 *
 * @author Usuario
 */
public abstract class JointAnmRot {

    public abstract float[][][] getData(ByteReader data, long JAC_offset, long ofsRot3, long ofsRot5, int numframes);

    public abstract int getFrameStep();
    
    public float[][] readMatrix(ByteReader data, long JAC_offset, long ofsRot3, long ofsRot5, long matrix_index) {
        boolean isRot3 = Utils.flagComp(matrix_index, 0x8000);
        matrix_index &= 0x7fff;

        data.mark(); //save rotIdx array position

        data.setIndex(JAC_offset);

        if (isRot3) {
            data.skip(ofsRot3 + 6 * matrix_index);
            return readRot3Matrix(data);
        } else {
            data.skip(ofsRot5 + 10 * matrix_index);
            return readRot5Matrix(data);
        }
    }

    private float[][] readRot3Matrix(ByteReader data) {
        boolean SD, SC, M;
        int idxPivot;

        int flags = data.getu16();
        float A = data.getfx16();
        float B = data.getfx16();
        float C = B;
        float D = A;
        float pivot = 1;

        SD = flagComp(flags, 0x40);
        SC = flagComp(flags, 0x20);
        M = flagComp(flags, 0x10);
        idxPivot = flags % 0x10;

        if (SD) {
            D = -A;
        }
        if (SC) {
            C = -B;
        }
        if (M) {
            pivot = -1;
        }

        int unused_row, unused_column;
        unused_row = idxPivot / 3;
        unused_column = idxPivot % 3;

        float[] matrix_elements = new float[]{A, B, C, D};
        int element_index = 0;

        float[][] rotation_matrix = new float[3][3];

        int i = 0, j = 0;
        for (i = 0; i < 3; i++) {
            //if (i != unused_column) {
            for (j = 0; j < 3; j++) {
                if (j != unused_column && i != unused_row) {
                    rotation_matrix[i][j] = matrix_elements[element_index];
                    element_index++;
                } else {
                    rotation_matrix[i][j] = 0;
                }
            }
            //}
        }

        rotation_matrix[unused_row][unused_column] = pivot;

        data.resetMarkDelete(); //restore index value to rotIdx array position

        return rotation_matrix;
    }

    private float[][] readRot5Matrix(ByteReader data) {
        int _12_int;
        short _00, _01, _02, _10, _11, _12;

        _00 = data.gets16();
        _01 = data.gets16();
        _02 = data.gets16();
        _10 = data.gets16();
        _11 = data.gets16();
        _12_int = 0;
        _12_int |= _10 & 0x7;
        _12_int |= (_02 & 0x7) << 3;
        _12_int |= (_01 & 0x7) << 6;
        _12_int |= (_00 & 0x7) << 9;
        //_12_int |= (_11 & 0x1) << 12;
        
        if (flagComp(_11, 0x1)) {
            _12_int |= 0xF << 12;
        }
        _12 = (short) _12_int;
        
        float f00, f01, f02, f10, f11, f12;

        f00 = (float) ((short)(_00 & 0xFFF8) >> 3) / 0x1000;
        f01 = (float) ((short)(_01 & 0xFFF8) >> 3) / 0x1000;
        f02 = (float) ((short)(_02 & 0xFFF8) >> 3) / 0x1000;
        f10 = (float) ((short)(_10 & 0xFFF8) >> 3) / 0x1000;
        f11 = (float) ((short)(_11 & 0xFFF8) >> 3) / 0x1000;
        f12 = (float) (_12) / 0x1000;
        //f12 = (float) ((_12 & 0xFFF8) >> 3) / 0x1000;

//        f00 = (float) (_00 & 0xFFF8) / 0x1000;
//        f01 = (float) (_01 & 0xFFF8) / 0x1000;
//        f02 = (float) (_02 & 0xFFF8) / 0x1000;
//        f10 = (float) (_10 & 0xFFF8) / 0x1000;
//        f11 = (float) (_11 & 0xFFF8) / 0x1000;
//        f12 = (float) (_12 & 0xFFF8) / 0x1000;

//        f00 = (float) ((_00 & 0xFFF8) << 3) / 0x1000 - 1;
//        f01 = (float) ((_01 & 0xFFF8) << 3) / 0x1000 - 1;
//        f02 = (float) ((_02 & 0xFFF8) << 3) / 0x1000 - 1;
//        f10 = (float) ((_10 & 0xFFF8) << 3) / 0x1000 - 1;
//        f11 = (float) ((_11 & 0xFFF8) << 3) / 0x1000 - 1;
//        f12 = (float) _12 / 0x1000 - 1;

//        f00 = Utils.fixValue(f00);
//        f01 = Utils.fixValue(f01);
//        f02 = Utils.fixValue(f02);
//        f10 = Utils.fixValue(f10);
//        f11 = Utils.fixValue(f11);
//        f12 = Utils.fixValue(f12);

        /*f00 = Math.abs(f00);
        f01 = Math.abs(f01);
        f02 = Math.abs(f02);
        f10 = Math.abs(f10);
        f11 = Math.abs(f11);
        f12 = Math.abs(f12);*/

        data.resetMarkDelete(); //restore index value to rotIdx array position

        //return new float[][]{{f00, f01, f02}, {f10, f11, f12}, {f20, f21, f22}};
        return new float[][]{{f00, f01, f02}, {f10, f11, f12}, crossProduct(f00, f01, f02, f10, f11, f12)};

        //return null;
    }

}
