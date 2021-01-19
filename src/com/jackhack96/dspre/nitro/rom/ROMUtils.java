/*
 * This file is part of DS Pokemon ROM Editor.
 *
 * DS Pokemon ROM Editor is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DS Pokemon ROM Editor.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2020 JackHack96 Nomura
 */

package com.jackhack96.dspre.nitro.rom;

import com.jackhack96.dspre.nitro.nitroreader.shared.Utils;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Class that implements some utilities like ROM name and code, ROM icon and MD5 hash
 */
public class ROMUtils
{
    /**
     * Reads the ROM game code and returns it
     *
     * @param romPath The ROM path
     * @return ROM game code
     * @throws IOException If IO errors occur
     */
    public static String getROMCode(String romPath) throws IOException {
        Buffer reader = new Buffer(romPath);
        reader.skipBytes(12);
        String code = reader.readString(4);
        reader.close();
        return code;
    }

    /**
     * Reads the ROM game name and returns it
     *
     * @param romPath The ROM path
     * @return ROM game name
     * @throws IOException If IO errors occur
     */
    public static String getROMName(String romPath) throws IOException {
        Buffer reader = new Buffer(romPath);
        String code = reader.readString(12).trim();
        reader.close();
        return code;
    }

    /**
     * Reads the ROM game icon and returns it
     *
     * @param bannerPath The banner.bin path
     * @return ROM game icon
     * @throws IOException If IO errors occur
     */
    public static BufferedImage getIcon(String bannerPath) throws IOException
    {
        Buffer reader = new Buffer(bannerPath);
        reader.skipBytes(0x20);

        int[] iconBytes = bytesToBit4(reader.readBytesI(0x200));
        Color[] palette = new Color[16];
        for (int i = 0; i < 16; i++)
            palette[i] = Utils.u16ToColor(reader.readShort());
        reader.close();

        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        int index = 0;
        for (int tileX = 0; tileX < 4; tileX++) {
            for (int tileY = 0; tileY < 4; tileY++) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        icon.setRGB(y + tileY * 8, x + tileX * 8, palette[iconBytes[index]].getRGB());
                        index++;
                    }
                }
            }
        }

        return icon;
    }

    /**
     * Compute the MD5 checksum of the given ROM
     *
     * @param romPath The ROM path
     * @return The MD5 hash
     * @throws IOException If IO errors occur
     */
    public static String getROMChecksum(Path romPath) throws IOException {
        // read all the ROM and compute its MD5
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes = Objects.requireNonNull(digest).digest(Files.readAllBytes(romPath));

        // convert bytes to hexadecimal format
        return printHexBinary(bytes);
    }

    /**
     * Splits given 8 bit unsigned byte array to 4 bits per byte (useful for 4bpp bitmaps)
     *
     * @param data Unsigned 8 bit array
     * @return Split 4 bit array
     */
    public static int[] bytesToBit4(int[] data) {
        int[] bit4 = new int[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            bit4[i * 2] = data[i] & 0x0f;
            bit4[i * 2 + 1] = (data[i] & 0xf0) >> 4;
        }
        return bit4;
    }

    /**
     * Just add padding for 4-byte offset alignment
     *
     * @param rom The BinaryWriter ROM stream
     * @throws IOException If any IO errors occur
     */
    public static void writePadding(BinaryWriter rom) throws IOException {
        int p = (int) rom.getPosition();
        if (p % 4 != 0)
            for (int i = 0; i < 4 - (p % 4); i++)
                rom.writeByte(0xff);
    }

    /**
     * Calculate the nearest 4-byte aligned offset
     *
     * @param offset Current offset
     * @return 4-byte aligned offset
     */
    public static int addPadding(int offset) {
        int diff = offset;
        if (diff % 4 != 0)
            diff += 4 - (diff % 4);
        return diff - offset;
    }

    private static String printHexBinary(byte... arr)
    {
        StringBuilder str= new StringBuilder();

        for(byte b : arr)
        {
            str.append(Integer.toHexString(b));
        }

        return str.toString().toLowerCase();
    }
}
