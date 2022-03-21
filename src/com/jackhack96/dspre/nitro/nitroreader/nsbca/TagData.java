/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbca;

import com.jackhack96.dspre.nitro.nitroreader.shared.ComparableTagEnum;
import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import java.util.HashMap;
import java.util.Map;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmRot;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmRotConst;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmRotVariable;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmScale;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmScaleConst;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmScaleVariable;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTrans;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransConst;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.animtag.JointAnmTransVariable;

import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.BASE_R;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.BASE_S;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.BASE_T;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.CONST_R;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.CONST_SX;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.CONST_SY;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.CONST_SZ;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.CONST_TX;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.CONST_TY;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.CONST_TZ;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.IDENTITY;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.IDENTITY_R;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.IDENTITY_S;
import static com.jackhack96.dspre.nitro.nitroreader.nsbca.TagData.NNS_G3D_JNTANM_SRTINFO.IDENTITY_T;
import static com.jackhack96.dspre.nitro.nitroreader.shared.Utils.tagComp;

/**
 *
 * @author Usuario
 */
public class TagData {

    public enum NNS_G3D_JNTANM_SRTINFO implements ComparableTagEnum{

        IDENTITY(0x00000001),
        IDENTITY_T(0x00000002),
        BASE_T(0x00000004),
        CONST_TX(0x00000008),
        CONST_TY(0x00000010),
        CONST_TZ(0x00000020),
        IDENTITY_R(0x00000040),
        BASE_R(0x00000080),
        CONST_R(0x00000100),
        IDENTITY_S(0x00000200),
        BASE_S(0x00000400),
        CONST_SX(0x00000800),
        CONST_SY(0x00001000),
        CONST_SZ(0x00002000),
        NODE_MASK(0xFF000000);

        private final int value;

        private static Map<Integer, NNS_G3D_JNTANM_SRTINFO> map = new HashMap<>();

        static {
            for (NNS_G3D_JNTANM_SRTINFO cmd : NNS_G3D_JNTANM_SRTINFO.values()) {
                map.put(cmd.value, cmd);
            }
        }

        private NNS_G3D_JNTANM_SRTINFO(int opcode) {
            this.value = opcode;
        }

        public static NNS_G3D_JNTANM_SRTINFO valueOf(int opcode) {
            return map.get(opcode);
        }

        @Override
        public int getValue() {
            return value;
        }

    }

    private JointAnmScale getJointAnmScale(ByteReader data, boolean isConst) {
        if (isConst) {
            return new JointAnmScaleConst(data);
        }
        return new JointAnmScaleVariable(data);
    }

    private JointAnmRot getJointAnmRot(ByteReader data, boolean isConst) {
        if (isConst) {
            return new JointAnmRotConst(data);
        }
        return new JointAnmRotVariable(data);
    }

    private JointAnmTrans getJointAnmTrans(ByteReader data, boolean isConst) {
        if (isConst) {
            return new JointAnmTransConst(data);
        }
        return new JointAnmTransVariable(data);
    }

    private JointAnmTrans tx, ty, tz;
    private JointAnmRot r;
    private JointAnmScale sx, sy, sz;

    public TagData(ByteReader data) {

        tx = new JointAnmTransConst();
        ty = new JointAnmTransConst();
        tz = new JointAnmTransConst();
        r = new JointAnmRotConst();
        sx = new JointAnmScaleConst();
        sy = new JointAnmScaleConst();
        sz = new JointAnmScaleConst();

        long flag = data.getu32();
        if (!(tagComp(flag, IDENTITY))) {
            if (!(tagComp(flag, IDENTITY_T)) && !(tagComp(flag, BASE_T))) {
                tx = getJointAnmTrans(data, tagComp(flag, CONST_TX));
                ty = getJointAnmTrans(data, tagComp(flag, CONST_TY));
                tz = getJointAnmTrans(data, tagComp(flag, CONST_TZ));
            }
            if (!(tagComp(flag, IDENTITY_R)) && !(tagComp(flag, BASE_R))) {
                r = getJointAnmRot(data, tagComp(flag, CONST_R));
            }
            if (!(tagComp(flag, IDENTITY_S)) && !(tagComp(flag, BASE_S))) {
                sx = getJointAnmScale(data, tagComp(flag, CONST_SX));
                sy = getJointAnmScale(data, tagComp(flag, CONST_SY));
                sz = getJointAnmScale(data, tagComp(flag, CONST_SZ));
            }
        }

    }

    public JointAnmTrans getTx() {
        return tx;
    }

    public JointAnmTrans getTy() {
        return ty;
    }

    public JointAnmTrans getTz() {
        return tz;
    }

    public JointAnmRot getR() {
        return r;
    }

    public JointAnmScale getSx() {
        return sx;
    }

    public JointAnmScale getSy() {
        return sy;
    }

    public JointAnmScale getSz() {
        return sz;
    }

}
