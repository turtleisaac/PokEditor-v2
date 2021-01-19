/*
 * This file is part of jNdstool.
 * 
 * jNdstool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jNdstool. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2020 JackHack96
 */
package com.jackhack96.jNdstool.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;

/**
 * This is an utility class for writing binary little endian files (such as NDS ROMs)
 * By default, Java uses Big Endian, so we have to manually convert everything
 */
public class jBinaryWriter
{
    private final FileOutputStream fileOutput; // The base FileOutputStream where we'll get the FileChannel
    private final FileChannel fileOutputChannel; // The actual FileChannel
    private final FileLock fileOutputChannelLock; // An exclusive lock

    /**
     * Class constructor
     *
     * @param filePath The file path
     * @throws FileNotFoundException If the given path doesn't exist
     */
    public jBinaryWriter(Path filePath) throws IOException {
        this.fileOutput = new FileOutputStream(filePath.toString());
        this.fileOutputChannel = this.fileOutput.getChannel();
        this.fileOutputChannelLock = this.fileOutputChannel.lock();
    }

    /**
     * Write a single byte in the current position
     *
     * @param t The byte to write
     * @throws IOException If there's an I/O error
     */
    public void writeByte(byte t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(t);
        buffer.flip();
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write a single byte in the current position
     *
     * @param t The byte to write
     * @throws IOException If there's an I/O error
     */
    public void writeByte(int t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) t);
        buffer.flip();
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write two bytes in the current position
     *
     * @param t The bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeShort(int t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) t);
        buffer.flip();
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write four bytes in the current position
     *
     * @param t The bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeInt(int t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(t);
        buffer.flip();
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write four bytes in the current position
     *
     * @param t The bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeInt(long t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt((int) t);
        buffer.flip();
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write eight bytes in the current position
     *
     * @param t The bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeLong(long t) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(t);
        buffer.flip();
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeBytes(byte[] t) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(t);
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Byte buffer
     * @param n Bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeBytes(byte[] t, int n) throws IOException {
        if (n <= t.length) {
            ByteBuffer buffer = ByteBuffer.wrap(t, 0, n);
            this.fileOutputChannel.write(buffer);
        } else
            throw new IndexOutOfBoundsException();
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeBytes(int[] t) throws IOException {
        byte[] d = new byte[t.length];
        for (int i = 0; i < t.length; i++)
            d[i] = (byte) t[i];
        ByteBuffer buffer = ByteBuffer.wrap(d);
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param t Byte buffer
     * @param n Bytes to write
     * @throws IOException If there's an I/O error
     */
    public void writeBytes(int[] t, int n) throws IOException {
        if (n <= t.length) {
            byte[] d = new byte[t.length];
            for (int i = 0; i < n; i++)
                d[i] = (byte) t[i];
            ByteBuffer buffer = ByteBuffer.wrap(d);
            this.fileOutputChannel.write(buffer);
        } else
            throw new IndexOutOfBoundsException();
    }

    /**
     * Write n bytes in the current position
     *
     * @param s String to write
     * @throws IOException If there's an I/O error
     */
    public void writeString(String s) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(s.getBytes());
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Write n bytes in the current position
     *
     * @param s String to write
     * @param l String length
     * @throws IOException If there's an I/O error
     */
    public void writeString(String s, int l) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(l).order(ByteOrder.LITTLE_ENDIAN);
        byte st[] = new byte[l];
        for (int i = 0; i < l; i++) {
            if (i < s.length())
                st[i] = (byte) s.charAt(i);
            else
                st[i] = 0x0;
        }
        buffer.put(st);
        buffer.flip();
        this.fileOutputChannel.write(buffer);
    }

    /**
     * Go to the the specified position (from the beginning of the file)
     *
     * @param t The absolute offset
     * @throws IOException If there's an I/O error
     */
    public void seek(long t) throws IOException {
        this.fileOutputChannel.position(t);
    }

    /**
     * Go to the specified position (from the current position)
     *
     * @param pos The relative offset
     * @throws IOException If there's an I/O error
     */
    public void skip(int pos) throws IOException {
        this.fileOutputChannel.position(this.fileOutputChannel.position() + pos);
    }

    /**
     * Get the current position
     * Please note that the returned value is a 4 byte integer
     * as NDS offsets are usually 4 byte long
     *
     * @return The absolute offset as 4-byte integer
     * @throws IOException If there's an I/O error
     */
    public int getPosition() throws IOException {
        return (int) this.fileOutputChannel.position();
    }

    /**
     * Close the streams
     *
     * @throws IOException If there's an I/O error
     */
    public void close() throws IOException {
        this.fileOutputChannelLock.release();
        this.fileOutputChannel.close();
        this.fileOutput.close();
    }

    /**
     * Sync the file cache (force disk write)
     *
     * @throws IOException If there's an I/O error
     */
    public void sync() throws IOException {
        this.fileOutput.flush();
        this.fileOutput.getFD().sync();
    }
}