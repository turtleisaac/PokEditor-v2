package com.jackhack96.dspre.nitro.nitroreader.shared;

public class DictionaryEntry20byte {

    private final long param1, param2, param3, param4, param5;
    private String name;

    public DictionaryEntry20byte(long param1, long param2, long param3, long param4, long param5) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name.replace("\0", "");
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

    
}
