package com.jackhack96.jNdstool.io;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * This is an utility class for reading/writing binary little endian buffers
 */
public class jBinaryStream
{
    private final ByteBuffer buffer;

    /**
     * Initialize BinaryStream with the specified buffer
     *
     * @param data Initial byte array (assuming unsigned values)
     */
    public jBinaryStream(int[] data) {
        byte[] byts = new byte[data.length];
        for (int i = 0; i < data.length; i++)
             byts[i] = (byte) data[i];
        buffer = ByteBuffer.wrap(byts);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Initialize BinaryStream with the specified buffer
     *
     * @param data Initial byte array
     */
    public jBinaryStream(byte[] data) {
        buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Initialize BinaryStream with an empty buffer
     *
     * @param len Length of the buffer
     */
    public jBinaryStream(int len) {
        buffer = ByteBuffer.allocate(len);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Read a single byte from the current position
     *
     * @return An unsigned number from the read byte
     * @throws BufferOverflowException If we reach the end of the array
     */
    public int readByte() throws BufferOverflowException {
        return buffer.get() & 0xff;
    }

    /**
     * Read two bytes from the current position
     *
     * @return An unsigned number from the read bytes
     * @throws BufferOverflowException If we reach the end of the array
     */
    public int readShort() throws BufferOverflowException {
        return buffer.getShort() & 0xffff;
    }

    /**
     * Read four bytes from the current position
     *
     * @return An unsigned number from the read bytes
     * @throws BufferOverflowException If we reach the end of the array
     */
    public int readInt() throws BufferOverflowException {
        return buffer.getInt();
    }

    /**
     * Read eight bytes from the current position
     *
     * @return An unsigned number from the read bytes
     * @throws BufferOverflowException If we reach the end of the array
     */
    public long readLong() throws BufferOverflowException {
        return buffer.getLong();
    }

    /**
     * Read n bytes from the current position
     *
     * @param len The number of bytes to be read
     * @return An array of bytes
     * @throws BufferOverflowException If we reach the end of the array
     */
    public byte[] readBuffer(int len) throws BufferOverflowException {
        byte[] b = new byte[len];
        return buffer.get(b).array();
    }

    /**
     * Read n bytes from the current position
     *
     * @param len The number of bytes to be read
     * @param c   The charset to use
     * @return A formatted string
     * @throws BufferOverflowException If we reach the end of the array
     */
    public String readString(int len, Charset c) throws BufferOverflowException {
        byte[] b = new byte[len];
        return new String(buffer.get(b).array(), c);
    }

    /**
     * Read the entire file
     *
     * @return A byte array
     */
    public byte[] readAll() {
        return buffer.array();
    }

    /**
     * Write a single byte in the current position
     *
     * @param t The byte to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeByte(byte t) throws BufferOverflowException {
        buffer.put(t);
    }

    /**
     * Write a single byte in the current position
     *
     * @param t The byte to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeByte(int t) throws BufferOverflowException {
        buffer.put((byte) t);
    }

    /**
     * Write two bytes in the current position
     *
     * @param t The bytes to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeShort(int t) throws BufferOverflowException {
        buffer.putShort((short) t);
    }

    /**
     * Write four bytes in the current position
     *
     * @param t The bytes to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeInt(int t) throws BufferOverflowException {
        buffer.putInt(t);
    }

    /**
     * Write eight bytes in the current position
     *
     * @param t The bytes to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeLong(long t) throws BufferOverflowException {
        buffer.putLong(t);
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Bytes to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeBytes(byte[] t) throws BufferOverflowException {
        ByteBuffer buffer = ByteBuffer.wrap(t);
        this.buffer.put(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Byte buffer
     * @param n Bytes to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeBytes(byte[] t, int n) throws BufferOverflowException {
        ByteBuffer buffer = ByteBuffer.wrap(t, 0, n);
        this.buffer.put(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Bytes to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeBytes(int[] t) throws BufferOverflowException {
        byte[] d = new byte[t.length];
        for (int i = 0; i < t.length; i++)
             d[i] = (byte) t[i];
        ByteBuffer buffer = ByteBuffer.wrap(d);
        this.buffer.put(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Byte buffer
     * @param n Bytes to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeBytes(int[] t, int n) throws BufferOverflowException {
        byte[] d = new byte[t.length];
        for (int i = 0; i < n; i++)
             d[i] = (byte) t[i];
        ByteBuffer buffer = ByteBuffer.wrap(d);
        this.buffer.put(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param s String to write
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeString(String s) throws BufferOverflowException {
        ByteBuffer buffer = ByteBuffer.wrap(s.getBytes());
        this.buffer.put(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param s String to write
     * @param l String length
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void writeString(String s, int l) throws BufferOverflowException {
        ByteBuffer buffer = ByteBuffer.allocate(l).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < l; i++) {
            if (i < s.length())
                buffer.put((byte) s.charAt(i));
            else
                buffer.put((byte) 0x0);
        }
        buffer.flip();
        this.buffer.put(buffer);
    }

    /**
     * Go to the specified position (from the beginning of the file)
     *
     * @param pos The absolute offset
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void seek(int pos) throws BufferOverflowException {
        buffer.position(pos);
    }

    /**
     * Go to the specified position (from the current position)
     *
     * @param pos The relative offset
     * @throws BufferOverflowException If we reach the end of the array
     */
    public void skip(int pos) throws BufferOverflowException {
        buffer.position(buffer.position() + pos);
    }
}
