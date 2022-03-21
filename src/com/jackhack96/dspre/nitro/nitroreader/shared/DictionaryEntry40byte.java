/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.shared;

/**
 *
 * @author Usuario
 */
public class DictionaryEntry40byte {

    private final long param1, param2, param3, param4, param5, param6, param7, param8, param9, param10;
    private String name;

    public DictionaryEntry40byte(long param1, long param2, long param3, long param4, long param5, long param6, long param7, long param8, long param9, long param10) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
        this.param7 = param7;
        this.param8 = param8;
        this.param9 = param9;
        this.param10 = param10;
    }

    public long getParam1() {
        return param1;
    }

    public long getParam2() {
        return param2;
    }

    public long getParam3() {
        return param3;
    }

    public long getParam4() {
        return param4;
    }

    public long getParam5() {
        return param5;
    }

    public long getParam6() {
        return param6;
    }

    public long getParam7() {
        return param7;
    }

    public long getParam8() {
        return param8;
    }

    public long getParam9() {
        return param9;
    }

    public long getParam10() {
        return param10;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name.replace("\0", "");
    }

}
