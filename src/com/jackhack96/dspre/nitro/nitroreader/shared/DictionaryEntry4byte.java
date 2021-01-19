package com.jackhack96.dspre.nitro.nitroreader.shared;

public class DictionaryEntry4byte {

    private long offset;
    private String name;

    public DictionaryEntry4byte(long offset) {
        this.offset = offset;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name.replace("\0", "");
    }

    public long getOffset() {
        return offset;
    }

}
