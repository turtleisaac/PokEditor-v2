package com.turtleisaac.pokeditor.framework;

import java.io.*;
import java.util.Arrays;

public class BinaryWriter {

    private final RandomAccessFile raf;
    private final byte[] buf = new byte[8];

    public static void writeFile(File file, int... bytes) throws IOException {
        BinaryWriter writer = new BinaryWriter(file);
        writer.writeBytes(bytes);
        writer.close();
    }

    public BinaryWriter(File file) throws IOException {
        raf = new RandomAccessFile(file, "rw");
        raf.setLength(0);
    }

    public BinaryWriter(String fileName) throws IOException
    {
        this(new File(fileName.replaceAll("/",File.separator)));
    }
    
    public void setPosition(long pos) throws IOException {
        raf.seek(pos);
    }
    
    public long getPosition() throws IOException {
        return raf.getFilePointer();
    }
    
    public void skipBytes(int bytes) throws IOException {
        raf.skipBytes(bytes);
    }

    public void writeInt(int i) throws IOException {
        buf[0] = (byte) (i & 0xff);
        buf[1] = (byte) ((i >> 8) & 0xff);
        buf[2] = (byte) ((i >> 16) & 0xff);
        buf[3] = (byte) ((i >> 24) & 0xff);
        raf.write(buf, 0, 4);
    }

    public void writeInts(int... i) throws IOException {
        for(int in : i) {
            writeInt(in);
        }
    }

    public void writeLong(long i) throws IOException {
        buf[0] = (byte) (i & 0xff);
        buf[1] = (byte) ((i >> 8) & 0xff);
        buf[2] = (byte) ((i >> 16) & 0xff);
        buf[3] = (byte) ((i >> 24) & 0xff);
        buf[4] = (byte) ((i >> 32) & 0xff);
        buf[5] = (byte) ((i >> 40) & 0xff);
        buf[6] = (byte) ((i >> 48) & 0xff);
        buf[7] = (byte) ((i >> 56) & 0xff);
        raf.write(buf, 0, 8);
    }

    public void writeShort(short s) throws IOException {
        buf[0] = (byte) (s & 0xff);
        buf[1] = (byte) ((s >> 8) & 0xff);
        raf.write(buf, 0, 2);
    }

    public void writeByte(byte b) throws IOException {
        raf.write(b);
    }

    public void writeByte(int b)
    {
        writeByte(b);
    }

    public void writeBytes(int... bytes) throws IOException {
        for (int b : bytes) {
            raf.write(b);
        }
    }

    public void writeShorts(int... shorts) throws IOException {
        for (int s : shorts) {
            raf.writeShort(s);
        }
    }

    public void writeShorts(short... shorts) throws IOException {
        for (short s : shorts) {
            raf.writeShort(s);
        }
    }

    public void write(byte[] bytes) throws IOException {
        raf.write(bytes);
    }

    public void write(byte[] bytes, int offset, int length) throws IOException {
        raf.write(bytes, offset, length);
    }

    public void writeByteNumTimes(byte b, int num) throws IOException {
        byte[] bytes= new byte[num];
        Arrays.fill(bytes,b);
        write(bytes);
    }

    public void writeByteNumTimes(int b, int num) throws IOException {
        byte[] bytes= new byte[num];
        Arrays.fill(bytes,(byte) b);
        write(bytes);
    }

    public void writePadding(int num) throws IOException {
        writeByteNumTimes((byte) 0x00,num);
    }

    public void close() throws IOException {
        raf.close();
    }

    public void write(int i) {
    }
}
