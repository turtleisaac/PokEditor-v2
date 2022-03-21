package com.jackhack96.jNdstool.nitro;

import com.jackhack96.jNdstool.io.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the File Name Table
 */
public class FAT {
    /**
     * Write the FNT section in the ROM
     *
     * @param rom  ROM binary stream
     * @param root Root nitro directory of the ROM
     * @throws IOException
     */
    public static void writeFAT(jBinaryWriter rom, NitroDirectory root, List<Integer> overlayStartOffsets, List<Integer> overlayEndOffsets) throws IOException {
        if (root.getId() == 0xf000) {
            for (int i = 0; i < overlayStartOffsets.size(); i++) {
                rom.writeInt(overlayStartOffsets.get(i));
                rom.writeInt(overlayEndOffsets.get(i));
            }
            writeFAT(rom, root);
        } else
            throw new IOException("This is not the root directory!");
    }

    /**
     * Recursively write the FAT section of the ROM
     *
     * @param rom  ROM binary stream
     * @param root Root nitro directory of the ROM
     * @throws IOException
     */
    private static void writeFAT(jBinaryWriter rom, NitroDirectory root) throws IOException {
        for (NitroFile f : root.getFileList()) {
            rom.writeInt(f.getOffset());
            rom.writeInt(f.getOffset() + f.getSize());
        }
        for (NitroDirectory d : root.getDirectoryList())
            writeFAT(rom, d);
    }

    /**
     * Pre-calculate the size of the FAT section starting from a path (overlays are excluded from calculation)
     * Please note that the overlays aren't counted
     *
     * @param path Path of the folder to calculate the FAT
     * @return Size in bytes of the FAT section
     */
    public static int calculateFATSize(File path) {
        int n = Objects.requireNonNull(path.listFiles(File::isFile)).length * 8;
        for (File t : Objects.requireNonNull(path.listFiles(File::isDirectory)))
            n += calculateFATSize(t);
        return n;
    }
}