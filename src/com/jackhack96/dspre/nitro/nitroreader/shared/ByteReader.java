package com.jackhack96.dspre.nitro.nitroreader.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;

public class ByteReader {

    private Deque<Integer> marks = new ArrayDeque<Integer>();
    private int index, init;
    private byte[] bArray;

    private byte[] fileToByteArray(File file) {
        FileInputStream fis = null;
        byte[] bArray = new byte[(int) file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        } catch (IOException ioExp) {
            ioExp.printStackTrace();
        }
        return bArray;
    }

    public void padding(int n) {
        while ((index % n) != 0) {
            index++;
        }
    }

//    public ByteReader(String path) throws FileNotFoundException, IOException {
//        index = 0;
//        switch (FileUtils.getFileExtension(path)){
//            case "nsbmd":
//                bArray = fileToByteArray(new File(path));
//                break;
//            case "bin":
//                bArray = FileUtils.getNSBMDfromNARC(LZ77.Decompress10LZ(new HexInputStream(path)));
//                break;
//            case "narc":
//                bArray = FileUtils.getNSBMDfromNARC(fileToByteArray(new File(path)));
//                break;
//            case "nsbtx":
//                bArray = FileUtils.getTEX0fromNSBTX(fileToByteArray(new File(path)));
//                break;
//        }
//        System.out.println("");
//    }
    public ByteReader(File file) {
        index = 0;
        bArray = fileToByteArray(file);
    }

    public ByteReader(byte[] data) {
        index = 0;
        bArray = data;
    }

    public ByteReader(ByteReader data, int offset) {
        index = offset + init;
        init = offset;
        bArray = data.bArray;
    }

    public ByteReader(ByteReader data) {
        this.bArray = data.bArray;
        this.index = data.index;
        this.init = data.init;
    }

    public byte[] getbArray() {
        return bArray;
    }

    public int getIndex() {
        return index;
    }

    public void mark() {
        marks.add(index);
    }

    public void resetMarkDelete() {
        index = marks.removeLast();
    }
    
    public void deleteMark(){
        marks.removeLast();
    }

    public int getMark() {
        return marks.getLast();
    }

    public int getMarkDelete() {
        return marks.pollLast();
    }

    public void resetMark() {
        index = marks.getLast();
    }

    public void offset(int n) {
        index = init + n;
    }

    public void offsetAbsolute(int n) {
        index = n;
    }

    public void skip(int n) {
        index += n;
    }

    public void skip(long n) {
        if (n > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("NEGATIVE INDEX");
        }
        index += n;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIndex(long index) {
        if (index > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("NEGATIVE INDEX");
        }
        this.index = (int) index;
    }

    public int length() {
        return bArray.length;
    }

    public int getu8() {
        index++;
        return (int) (bArray[index - 1] & 0xff);
    }

    public int getu16() {
        index += 2;
        return (int) ((bArray[index - 2] & 0xff) | ((bArray[index - 1] & 0xff) << 8));
    }

    public short gets16() {
        index += 2;
        return (short) ((bArray[index - 2] & 0xff) | ((bArray[index - 1] & 0xff) << 8));
    }

    public int gets32() {
        index += 4;
        return (int) ((bArray[index - 4] & 0xff) | ((bArray[index - 3] & 0xff) << 8)
                | ((bArray[index - 2] & 0xff) << 16) | ((bArray[index - 1] & 0xff) << 24));
    }

    public long getu32() {
        index += 4;
        long value = ((bArray[index - 4] & 0xff) | ((bArray[index - 3] & 0xff) << 8)
                | ((bArray[index - 2] & 0xff) << 16) | ((bArray[index - 1] & 0xff) << 24));
        if (value < 0) {
            value += 0X100000000L;
        }
        return value;
    }

    public float getfx16() {
        return (float) gets16() / 0x1000;
    }

    public float getfx32() {
        return (float) gets32() / 0x1000;
    }

    public String getStr(int size) {
        byte[] temp = new byte[size];
        for (int k = 0; k < size; k++) {
            temp[k] = bArray[index + k];
        }
        index += size;
        return new String(temp, StandardCharsets.UTF_8).replaceAll("\0", "");
    }

    public String getStrBigEndian(int size) {
        byte[] temp = new byte[size];
        index += size;
        for (int k = 0; k < size; k++) {
            temp[k] = bArray[index - k - 1];
        }

        return new String(temp, StandardCharsets.UTF_8).replaceAll("\0", "");
    }
}
