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

import com.jackhack96.jNdstool.io.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * This class represents a folder of a Nitro file system
 */
class NitroDirectory implements Comparable<NitroDirectory> {
    private final String name; // Directory name
    private final int id; // Directory ID
    private final NitroDirectory parent; // Parent directory
    private final List<NitroFile> fileList; // The list of directory's files
    private final List<NitroDirectory> directoryList; // The list of subdirectories

    //The following variables are used internally by loadDir method
    private static int currentDirID;
    private static int fileID;
    private static int currentOffset;

    public NitroDirectory(String name, int id, NitroDirectory parent) {
        this.name = name;
        this.id = id;
        this.parent = parent;
        this.fileList = new ArrayList<>();
        this.directoryList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public NitroDirectory getParent() {
        return parent;
    }

    public List<NitroFile> getFileList() {
        Collections.sort(fileList);
        return fileList;
    }

    public List<NitroDirectory> getDirectoryList() {
        Collections.sort(directoryList);
        return directoryList;
    }

    @Override
    public String toString() {
        return "NitroDirectory{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(NitroDirectory nitroDirectory) {
        return (name.compareToIgnoreCase(nitroDirectory.name));
    }

    /**
     * Recursively load the FNT structure
     *
     * @param parent The current parent directory
     * @param stream FNT source stream
     * @param origin Initial stream offset
     * @throws IOException If a file is corrupted or something is wrong
     */
    public static void loadDir(NitroDirectory parent, jBinaryReader stream, long origin, Map<Integer, Integer> startOffset, Map<Integer, Integer> endOffset) throws IOException {
        long position = stream.getPosition();
        stream.seek(origin + (8 * (parent.id & 0xfff))); // Go the the FNT main table entry

        int subTableOffset = stream.readInt();
        int firstFileID = stream.readShort();

        stream.seek(origin + subTableOffset); // Go to the FNT sub table entry

        int header; // The header tells us if the entry is a file or a parent, and then it tells us the name size

        while (((header = stream.readByte()) & 0x7f) != 0) { // Until we found a 0x00 or a 0xff header, we can go further
            String name = stream.readString(header & 0x7f, StandardCharsets.US_ASCII); // This could be a parent name or a file name
            if (header > 0x7f) { // This is a directory
                int newID = stream.readShort(); // This will be the next parent ID
                NitroDirectory newDirectory = new NitroDirectory(name, newID, parent);
                parent.directoryList.add(newDirectory);
                loadDir(newDirectory, stream, origin, startOffset, endOffset);
            } else { // This is a file
                parent.fileList.add(new NitroFile(name, firstFileID, startOffset.get(firstFileID), endOffset.get(firstFileID) - startOffset.get(firstFileID), parent));
                firstFileID++;
            }
        }
        stream.seek(position);
    }

    /**
     * Recursively construct the NitroDirectory structure
     *
     * @param currentPath   The current host file system path
     * @param parent        The current parent directory
     * @param firstFileID   First file ID of the current folder
     * @param currentOffset Current offset from the ROM origin
     */
    public static void loadDir(File currentPath, NitroDirectory parent, int currentDirID, int firstFileID, int currentOffset) {
        // i need this variables to be shared among ALL recursive calls
        NitroDirectory.currentDirID = currentDirID;
        NitroDirectory.fileID = firstFileID;
        // it's better to get 4-byte alignment of the offsets
        if (currentOffset % 4 != 0)
            currentOffset += 4 - (currentOffset % 4);
        NitroDirectory.currentOffset = currentOffset;

        // now the actual recursive loading process
        loadDir(currentPath, parent);
    }

    /**
     * Recursively construct the NitroDirectory structure
     *
     * @param currentPath The current host file system path
     * @param parent      The current parent directory
     */
    private static void loadDir(File currentPath, NitroDirectory parent) {
        File[] dirList = currentPath.listFiles(File::isDirectory); // load the current directory list
        File[] fileList = currentPath.listFiles(File::isFile); // load the current file list

        // it's important to sort the file lists alphabetically
        if (dirList != null) {
            Arrays.sort(dirList, Comparator.comparing(a -> a.getName().toLowerCase()));
            for (File dir : dirList) { // for every directory I create the correspondent NitroDirectory
                currentDirID++;
                NitroDirectory newDirectory = new NitroDirectory(dir.getName(), currentDirID, parent);
                parent.directoryList.add(newDirectory);
                if (Objects.requireNonNull(dir.listFiles()).length > 0)
                    // here is the recursive call, whenever we encounter a new directory, we will first explore that path, in a depth-first-search fashion
                    loadDir(dir, newDirectory);
            }
        }
        // remember that this part will be executed for every directory
        // the first execution will be the first path that will lead to files (like a/0/0/0 for Pokemon HG)
        if (fileList != null) {
            Arrays.sort(fileList, Comparator.comparing(a -> a.getName().toLowerCase()));
            for (File file : fileList) { // for every file I create the correspondent NitroFile
                parent.fileList.add(new NitroFile(file.getName(), fileID, currentOffset, (int) file.length(), parent));
                fileID++;
                currentOffset += (int) file.length();
                // it's better to get 4-byte alignment of the offsets
                if (currentOffset % 4 != 0)
                    currentOffset += (4 - (currentOffset % 4));
            }
        }
    }

    /**
     * Recursively unpack the files of the ROM
     *
     * @param currentDir The path to use for creating the tree
     * @param rootDir    The current root directory
     * @throws IOException If a file is corrupted or something is wrong
     */
    public static void unpackFileTree(jBinaryReader rom, Path currentDir, NitroDirectory rootDir) throws IOException {
        // we scan for directories first, thus exploring a path in depth as in DFS algorithm
        for (NitroDirectory d : rootDir.directoryList) {
            if (Files.notExists(currentDir.resolve(d.name)))
                Files.createDirectory(currentDir.resolve(d.name));
            unpackFileTree(rom, currentDir.resolve(d.name), d);
        }
        // then whenever we reach the end of a path we unpack the files
        for (NitroFile f : rootDir.fileList) {
            if (Files.notExists(currentDir.resolve(f.getName()))) {
                jBinaryWriter tmp = new jBinaryWriter(currentDir.resolve(f.getName()));
                rom.seek(f.getOffset());
                tmp.writeBytes(rom.readBuffer(f.getSize()));
                tmp.close();
            }
        }
    }

    /**
     * Recursively repack the files in the ROM
     *
     * @param rom        BinaryWriter stream of the .nds ROM
     * @param currentDir Current path
     * @param rootDir    The current root directory
     * @throws IOException If a file is corrupted or something is wrong
     */
    public static void repackFileTree(jBinaryWriter rom, Path currentDir, NitroDirectory rootDir) throws IOException {
        // we scan for directories first, thus exploring a path in depth as in DFS algorithm
        for (NitroDirectory d : rootDir.directoryList)
            repackFileTree(rom, currentDir.resolve(d.getName()), d);
        // then whenever we reach the end of a path we unpack the files
        for (NitroFile f : rootDir.fileList) {
            if (Files.exists(currentDir.resolve(f.getName()))) {
                jBinaryReader tmp = new jBinaryReader(currentDir.resolve(f.getName()));
                if (f.getOffset() != rom.getPosition()) {
                    System.out.println("WARNING! " + f + " real offset differs from assumed one! Assumed: "
                            + f.getOffset() + " Real: " + rom.getPosition());
                    f.setOffset(rom.getPosition());
                }
                rom.writeBytes(tmp.readAll());
                // padding with 0xff for 4-byte alignment
                ROM.writePadding(rom);
                tmp.close();
            } else
                throw new IOException(f.getName() + " file does not exist");
        }
    }
}
