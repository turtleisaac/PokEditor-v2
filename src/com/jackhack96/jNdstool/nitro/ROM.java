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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import com.jackhack96.jNdstool.io.*;

/**
 * This is the main class, which handles the entire ROM
 */
public class ROM {
    /**
     * Extract the entire ROM in the host file system
     *
     * @param romPath The path of the .nds file
     * @param dirPath The path where to extract files
     * @throws IOException If something goes wrong
     */
    public static void extractROM(Path romPath, Path dirPath) throws IOException {
        if (Files.notExists(dirPath))
            Files.createDirectories(dirPath);
        if (!Files.isWritable(dirPath)) // If we can't read or write, we don't own the directory
        {
            System.out.println(dirPath.toFile().toString());
            throw new IOException("Can't write in the directory! Check permissions!");
        }


        jBinaryReader rom = new jBinaryReader(romPath);
        NitroDirectory root = new NitroDirectory("data", 0xf000, null);
        NitroHeader header = NitroHeader.readHeader(rom);
        Map<Integer, Integer> startOffset = new HashMap<>(); // The ROM's files start offset
        Map<Integer, Integer> endOffset = new HashMap<>(); // The ROM's files end offsets

        rom.seek(header.getFatOffset());

        for (int i = 0; i < header.getFatSize() / 8; i++) {
            startOffset.put(i, rom.readInt());
            endOffset.put(i, rom.readInt());
        }

        // Load the directory structure
        rom.seek(header.getFntOffset());
        NitroDirectory.loadDir(root, rom, rom.getPosition(), startOffset, endOffset);

        // Let's create the directory tree
        if (Files.notExists(dirPath.resolve("data")))
            Files.createDirectory(dirPath.resolve("data"));
        NitroDirectory.unpackFileTree(rom, dirPath.resolve("data"), root);

        // We also have to extract the header, the ARM binary files and the overlays
        jBinaryWriter w; // The writer for the various files

        // The overlays
        if (Files.notExists(dirPath.resolve("overlay")))
            Files.createDirectory(dirPath.resolve("overlay"));
        for (int i = 0; i < header.getArm9OverlaySize() / 0x20; i++) {
            if (Files.notExists(dirPath.resolve("overlay").resolve(String.format("overlay_%04d.bin", i)))) {
                w = new jBinaryWriter(dirPath.resolve("overlay").resolve(String.format("overlay_%04d.bin", i)));
                rom.seek(startOffset.get(i));
                w.writeBytes(rom.readBuffer(endOffset.get(i) - startOffset.get(i)));
                w.close();
            }
        }
        int arm7OvSize = (header.getArm7OverlaySize() / 0x20);
        for (int i = 0; i < arm7OvSize; i++) {
            if (Files.notExists(dirPath.resolve("overlay").resolve(String.format("overlay_%04d.bin", i + arm7OvSize)))) {
                w = new jBinaryWriter(dirPath.resolve("overlay").resolve(String.format("overlay_%04d.bin", i + arm7OvSize)));
                rom.seek(startOffset.get(i + arm7OvSize));
                w.writeBytes(rom.readBuffer(endOffset.get(i + arm7OvSize) - startOffset.get(i + arm7OvSize)));
                w.close();
            }
        }

        // The header and the two arms
        if (Files.notExists(dirPath.resolve("header.bin"))) {
            rom.seek(0);
            w = new jBinaryWriter(dirPath.resolve("header.bin"));
            w.writeBytes(rom.readBuffer(0x200));
            w.close();
        }

        if (Files.notExists(dirPath.resolve("arm9.bin"))) {
            rom.seek(header.getArm9RomOffset());
            w = new jBinaryWriter(dirPath.resolve("arm9.bin"));
            w.writeBytes(rom.readBuffer(header.getArm9Size()));
            w.close();
        }

        if (Files.notExists(dirPath.resolve("arm9ovltable.bin"))) {
            rom.seek(header.getArm9OverlayOffset());
            w = new jBinaryWriter(dirPath.resolve("arm9ovltable.bin"));
            w.writeBytes(rom.readBuffer(header.getArm9OverlaySize()));
            w.close();
        }

        if (Files.notExists(dirPath.resolve("arm7.bin"))) {
            rom.seek(header.getArm7RomOffset());
            w = new jBinaryWriter(dirPath.resolve("arm7.bin"));
            w.writeBytes(rom.readBuffer(header.getArm7Size()));
            w.close();
        }

        if (Files.notExists(dirPath.resolve("arm7ovltable.bin"))) {
            rom.seek(header.getArm7OverlayOffset());
            w = new jBinaryWriter(dirPath.resolve("arm7ovltable.bin"));
            w.writeBytes(rom.readBuffer(header.getArm7OverlaySize()));
            w.close();
        }

        if (Files.notExists(dirPath.resolve("banner.bin"))) {
            rom.seek(header.getIconOffset());
            w = new jBinaryWriter(dirPath.resolve("banner.bin"));
            w.writeBytes(rom.readBuffer(0x840));
            w.close();
        }

        rom.close();
    }

    /**
     * Build the entire ROM from the given directory
     *
     * @param dirPath The path of the directory containing the files
     * @param romPath The path of the .nds file
     * @throws IOException If something goes wrong
     */
    public static void buildROM(Path dirPath, Path romPath) throws IOException {
        // General check of the files
        if (Files.notExists(dirPath.resolve("data")))
            throw new IOException("data subfolder not found! Please check the given directory!");
        if (Files.notExists(dirPath.resolve("overlay")))
            throw new IOException("overlay subfolder not found! Please check the given directory!");
        if (Files.notExists(dirPath.resolve("arm9.bin")))
            throw new IOException("arm9 file not found! Please check the given directory!");
        if (Files.notExists(dirPath.resolve("arm9ovltable.bin")))
            throw new IOException("arm9 overlay table file not found! Please check the given directory!");
        if (Files.notExists(dirPath.resolve("arm7.bin")))
            throw new IOException("arm7 file not found! Please check the given directory!");
        if (Files.notExists(dirPath.resolve("arm7ovltable.bin")))
            throw new IOException("arm7 overlay table not found! Please check the given directory!");
        if (Files.notExists(dirPath.resolve("header.bin")))
            throw new IOException("header file not found! Please check the given directory!");
        if (Files.notExists(dirPath.resolve("banner.bin")))
            throw new IOException("banner file not found! Please check the given directory!");

        jBinaryWriter rom = new jBinaryWriter(romPath); // The stream for the .nds file
        jBinaryReader reader; // The stream for reading files

        // Loading the actual data and the overlay and pre-calculate offsets
        File[] overlays = dirPath.resolve("overlay").toFile().listFiles();
        assert overlays != null;
        Arrays.sort(overlays);

        NitroDirectory root = new NitroDirectory("data", 0xf000, null);
        int fimgOffset = 0;
        fimgOffset += 0x4000;                                                   // header size
        fimgOffset += (int) Files.size(dirPath.resolve("arm9.bin"));            // arm9 padded size
        fimgOffset += addPadding(fimgOffset);
        fimgOffset += (int) Files.size(dirPath.resolve("arm9ovltable.bin"));    // arm9 overlay table padded size
        fimgOffset += addPadding(fimgOffset);
        for (File overlay : overlays) {                                         // arm9 padded overlays
            fimgOffset += (int) overlay.length();
            fimgOffset += addPadding(fimgOffset);
        }
        fimgOffset += (int) Files.size(dirPath.resolve("arm7.bin"));            // arm7 padded size
        fimgOffset += addPadding(fimgOffset);
        fimgOffset += (int) Files.size(dirPath.resolve("arm7ovltable.bin"));    // arm7 overlay table padded size
        fimgOffset += addPadding(fimgOffset);
        fimgOffset += FNT.calculateFNTSize(dirPath.resolve("data").toFile());   // File Name Table padded size
        fimgOffset += addPadding(fimgOffset);
        fimgOffset += FAT.calculateFATSize(dirPath.resolve("data").toFile());   // File Allocation Table padded size
        fimgOffset += (overlays.length * 8);
        fimgOffset += addPadding(fimgOffset);
        fimgOffset += 0x840;                                                    // banner size

        // Recursively load directories and files, getting the root nitro directory
        NitroDirectory.loadDir(dirPath.resolve("data").toFile(),
                root,
                0xf000,
                Objects.requireNonNull(dirPath.resolve("overlay").toFile().listFiles()).length,
                fimgOffset
        );

        // Reading the header template, but skipping the section for now as we have to adjust some values
        reader = new jBinaryReader(dirPath.resolve("header.bin"));
        NitroHeader header = NitroHeader.readHeader(reader);
        ByteBuffer h = ByteBuffer.allocate(0x4000);
        rom.writeBytes(h.array());

        // The ARM9
        reader = new jBinaryReader(dirPath.resolve("arm9.bin"));
        header.setArm9RomOffset(rom.getPosition());
        header.setArm9Size(reader.getSize());
        rom.writeBytes(reader.readAll());
        writePadding(rom);

        // The ARM9 overlay table
        reader = new jBinaryReader(dirPath.resolve("arm9ovltable.bin"));
        header.setArm9OverlayOffset(rom.getPosition());
        header.setArm9OverlaySize(reader.getSize());
        rom.writeBytes(reader.readAll());
        writePadding(rom);

        // This will be needed for the FAT
        List<Integer> overlayStartOffsets = new ArrayList<>();
        List<Integer> overlayEndOffsets = new ArrayList<>();

        // The ARM9 overlays
        for (int i = 0; i < header.getArm9OverlaySize() / 0x20; i++) {
            reader = new jBinaryReader(overlays[i].toPath());
            overlayStartOffsets.add(rom.getPosition());
            overlayEndOffsets.add(rom.getPosition() + reader.getSize());
            rom.writeBytes(reader.readAll());
            writePadding(rom);
        }

        // The ARM7
        reader = new jBinaryReader(dirPath.resolve("arm7.bin"));
        header.setArm7RomOffset(rom.getPosition());
        header.setArm7Size(reader.getSize());
        rom.writeBytes(reader.readAll());
        writePadding(rom);

        // The ARM7 overlay table
        reader = new jBinaryReader(dirPath.resolve("arm7ovltable.bin"));
        header.setArm7OverlayOffset(rom.getPosition());
        header.setArm7OverlaySize(reader.getSize());
        rom.writeBytes(reader.readAll());
        writePadding(rom);

        // The ARM7 overlays
        for (int i = header.getArm9OverlaySize() / 0x20; i < header.getArm9OverlaySize() / 0x20 + header.getArm7OverlaySize() / 0x20; i++) {
            reader = new jBinaryReader(overlays[i].toPath());
            overlayStartOffsets.add(rom.getPosition());
            overlayEndOffsets.add(rom.getPosition() + reader.getSize());
            rom.writeBytes(reader.readAll());
            writePadding(rom);
        }

        // The File Name Table
        header.setFntOffset(rom.getPosition());
        FNT.writeFNT(rom, root);
        header.setFntSize(rom.getPosition() - header.getFntOffset());
        writePadding(rom);

        // The File Allocation Table
        header.setFatOffset(rom.getPosition());
        FAT.writeFAT(rom, root, overlayStartOffsets, overlayEndOffsets);
        header.setFatSize(rom.getPosition() - header.getFatOffset());
        System.out.println("Current Position: " + rom.getPosition());
        writePadding(rom);

        // The banner
        header.setIconOffset(rom.getPosition());
        reader = new jBinaryReader(dirPath.resolve("banner.bin"));
        rom.writeBytes(reader.readAll());
        writePadding(rom);

        // The actual files
        NitroDirectory.repackFileTree(rom, dirPath.resolve("data"), root);

        // Write updated header
        rom.seek(0);
        NitroHeader.updateHeaderChecksum(header);
        NitroHeader.writeHeader(header, rom);

        rom.close();
        reader.close();
    }

    /**
     * Just add padding for 4-byte offset alignment
     *
     * @param rom The BinaryWriter ROM stream
     * @throws IOException
     */
    static void writePadding(jBinaryWriter rom) throws IOException {
        int p = rom.getPosition();
        if (p % 4 != 0)
            for (int i = 0; i < 4 - (p % 4); i++) {
                rom.writeByte(0xff);
            }
    }

    /**
     * Calculate the nearest 4-byte aligned offset
     *
     * @param offset Current offset
     * @return 4-byte aligned offset
     */
    private static int addPadding(int offset) {
        int diff = offset;
        if (diff % 4 != 0)
            diff += 4 - (diff % 4);
        return diff - offset;
    }
}