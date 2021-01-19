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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * This is an utility class for reading binary little endian files (such as NDS
 * ROMs) By default, Java uses Big Endian, so we have to manually convert
 * everything
 */
public class jBinaryReader
{
    private final FileInputStream fileInput; // The base FileInputStream where we'll get the FileChannel
    private final FileChannel fileInputChannel; // The actual FileChannel

    /**
     * Class constructor
     *
     * @param filePath The file path
     * @throws FileNotFoundException If the given path doesn't exist
     */
    public jBinaryReader(Path filePath) throws IOException {
        this.fileInput = new FileInputStream(filePath.toString());
        this.fileInputChannel = this.fileInput.getChannel();
    }

    /**
     * Read a single byte from the current position
     *
     * @return An unsigned number from the read byte
     * @throws IOException If there's an I/O error
     */
    public int readByte() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        return (buffer.get(0) & 0xff);
    }

    /**
     * Read two bytes from the current position
     *
     * @return An unsigned number from the read bytes
     * @throws IOException If there's an I/O error
     */
    public int readShort() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        buffer.flip();
        return buffer.getShort() & 0xffff;
    }

    /**
     * Read four bytes from the current position
     *
     * @return An unsigned number from the read bytes
     * @throws IOException If there's an I/O error
     */
    public int readInt() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * Read eight bytes from the current position
     *
     * @return An unsigned number from the read bytes
     * @throws IOException If there's an I/O error
     */
    public long readLong() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        buffer.flip();
        return buffer.getLong();
    }

    /**
     * Read n bytes from the current position
     *
     * @param len The number of bytes to be read
     * @return An array of bytes
     * @throws IOException If there's an I/O error
     */
    public byte[] readBuffer(int len) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        buffer.flip();
        return buffer.array();
    }

    /**
     * Read n bytes from the current position
     *
     * @param len The number of bytes to be read
     * @return An array of unsigned int
     * @throws IOException If there's an I/O error
     */
    public int[] readUnsignedBuffer(int len) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        buffer.flip();
        int[] b2 = new int[len];
        for (int i = 0; i < len; i++)
            b2[i] = buffer.array()[i] & 0xff;
        return b2;
    }

    /**
     * Read n bytes from the current position
     *
     * @param len The number of bytes to be read
     * @param c   The charset to use
     * @return A formatted string
     * @throws IOException If there's an I/O error
     */
    public String readString(int len, Charset c) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        buffer.flip();
        return new String(buffer.array(), c);
    }

    /**
     * Read the entire file
     *
     * @return A byte array
     * @throws IOException If there's an I/O error
     */
    public byte[] readAll() throws IOException {
        this.fileInputChannel.position(0);
        ByteBuffer buffer = ByteBuffer.allocate((int) this.fileInputChannel.size()).order(ByteOrder.LITTLE_ENDIAN);
        this.fileInputChannel.read(buffer);
        buffer.flip();
        return buffer.array();
    }

    /**
     * Go to the specified position (from the beginning of the file)
     *
     * @param pos The absolute offset
     * @throws IOException If there's an I/O error
     */
    public void seek(long pos) throws IOException {
        this.fileInputChannel.position(pos);
    }

    /**
     * Go to the specified position (from the current position)
     *
     * @param pos The relative offset
     * @throws IOException If there's an I/O error
     */
    public void skip(int pos) throws IOException {
        this.fileInputChannel.position(this.fileInputChannel.position() + pos);
    }

    /**
     * Get the current position
     *
     * @return The absolute offset
     * @throws IOException If there's an I/O error
     */
    public int getPosition() throws IOException {
        return (int) this.fileInputChannel.position();
    }

    /**
     * Get the file size (in bytes)
     *
     * @return The file size
     * @throws IOException If there's an I/O error
     */
    public int getSize() throws IOException {
        return (int) this.fileInputChannel.size();
    }

    /**
     * Close the streams
     *
     * @throws IOException If there's an I/O error
     */
    public void close() throws IOException {
        this.fileInputChannel.close();
        this.fileInput.close();
    }
}