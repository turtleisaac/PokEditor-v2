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
package com.jackhack96.jNdstool.nitro;


import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

import com.jackhack96.jNdstool.io.*;

/**
 * This class represents the File Name Table
 */
class FNT {
    /**
     * Write the FNT section in the ROM
     *
     * @param rom  ROM binary stream
     * @param root Root nitro directory of the ROM
     * @throws IOException
     */
    public static void writeFNT(jBinaryWriter rom, NitroDirectory root) throws IOException {
        if (root.getId() == 0xf000) {
            ByteBuffer fntMainTable = ByteBuffer.allocate((getDirectoryNumber(root) + 1) * 8).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer fntSubTable = ByteBuffer.allocate(getSubTableSize(root)).order(ByteOrder.LITTLE_ENDIAN);

            // Write the first main table entry
            fntMainTable.putInt(fntMainTable.limit()); // Relative offset to the first sub table item
            fntMainTable.putShort((short) getFirstFileID(root)); // The first file ID we'll encounter
            fntMainTable.putShort((short) (getDirectoryNumber(root) + 1)); // Total number of directories (root included)

            writeFNT(root, fntMainTable, fntSubTable);
            rom.writeBytes(fntMainTable.array());
            rom.writeBytes(fntSubTable.array());
        } else
            throw new IOException("This is not the root directory!");
    }

    /**
     * Pre-calculate the size of the FNT section starting from a path
     *
     * @param path Path of the folder to calculate the FNT
     * @return Size in bytes of the FNT section
     */
    public static int calculateFNTSize(File path) {
        return (getDirectoryNumber(path) + 1) * 8 + getSubTableSize(path);
    }

    /**
     * Recursively write FNT sections
     *
     * @param currentDir Current sub-directory
     * @throws IOException
     */
    private static void writeFNT(NitroDirectory currentDir, ByteBuffer fntMainTable, ByteBuffer fntSubTable) throws IOException {
        for (NitroFile f : currentDir.getFileList()) {
            fntSubTable.put((byte) f.getName().length());
            System.out.println(f.getId() + ": " +  f.getName() + " (" + f.getName().length() + ")");
            fntSubTable.put(f.getName().getBytes());
        }
        for (NitroDirectory d : currentDir.getDirectoryList()) {
            fntSubTable.put((byte) (128 + d.getName().length()));
            fntSubTable.put(d.getName().getBytes());
//            System.out.println(d.getId() + ": " +  d.getName().length());
            fntSubTable.putShort((short) d.getId());
        }
        fntSubTable.put((byte) 0);
        for (NitroDirectory d : currentDir.getDirectoryList()) {
            fntMainTable.putInt(fntMainTable.limit() + fntSubTable.position());
            fntMainTable.putShort((short) getFirstFileID(d));
            fntMainTable.putShort((short) d.getParent().getId());
            writeFNT(d, fntMainTable, fntSubTable);
        }
    }

    /**
     * Calculate the number of directories of the directory tree
     *
     * @param d The root directory from where we start counting
     * @return Number of directories
     */
    private static int getDirectoryNumber(NitroDirectory d) {
        int n = d.getDirectoryList().size();
        for (NitroDirectory t : d.getDirectoryList())
            n += getDirectoryNumber(t);
        return n;
    }

    /**
     * Calculate the number of directories of the directory tree
     *
     * @param path The root directory from where we start counting
     * @return Number of directories
     */
    private static int getDirectoryNumber(File path) {
        int n = Objects.requireNonNull(path.listFiles(File::isDirectory)).length;
        for (File t : Objects.requireNonNull(path.listFiles(File::isDirectory)))
            n += getDirectoryNumber(t);
        return n;
    }

    /**
     * Find the ID of the first file in the directory tree
     *
     * @param d The root directory where to start searching
     * @return ID of the first file found
     */
    private static int getFirstFileID(NitroDirectory d) {
        if (d.getFileList().size() > 0)
            return d.getFileList().get(0).getId();
        else
            return getFirstFileID(d.getDirectoryList().get(0));
    }

    /**
     * Calculate the size of the FNT sub table
     *
     * @param current The root directory
     * @return Size of FNT sub table
     */
    private static int getSubTableSize(NitroDirectory current) {
        int a = 0;
        for (NitroDirectory d : current.getDirectoryList())
            a += d.getName().length() + 3;
        for (NitroFile f : current.getFileList()) {
            a += f.getName().length() + 1;
        }
        a += 1;
        for (NitroDirectory d : current.getDirectoryList())
            a += getSubTableSize(d);
        return a;
    }

    /**
     * Calculate the size of the FNT sub table
     *
     * @param path The root directory
     * @return Size of FNT sub table
     */
    private static int getSubTableSize(File path) {
        int a = 0;
        for (File t : Objects.requireNonNull(path.listFiles(File::isDirectory))) {
            a += t.getName().length() + 3;
        }
        for (File t : Objects.requireNonNull(path.listFiles(File::isFile))) {
            a += t.getName().length() + 1;
        }
        a += 1;
        for (File t : Objects.requireNonNull(path.listFiles(File::isDirectory)))
            a += getSubTableSize(t);
        return a;
    }
}
