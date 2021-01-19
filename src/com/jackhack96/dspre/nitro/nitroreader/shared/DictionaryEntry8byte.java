package com.jackhack96.dspre.nitro.nitroreader.shared;

public class DictionaryEntry8byte {

    private final long param1, param2;
    private String name;

    public DictionaryEntry8byte(long param1, long param2) {
        this.param1 = param1;
        this.param2 = param2;
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

    
}
