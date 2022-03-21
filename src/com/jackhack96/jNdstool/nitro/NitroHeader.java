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

import com.turtleisaac.pokeditor.framework.CRC16;
import com.jackhack96.jNdstool.io.*;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This class handles the cartdridge header.
 */
public class NitroHeader {
    private String gameTitle;
    private String gameCode;
    private String makerCode;

    private int unitCode;
    private int encryptionSeedSelect;
    private int deviceCapacity;
    private byte[] reserved1;
    private int dsiFlags;
    private int ndsRegion;
    private int romVersion;
    private int autoStart;

    private int arm9RomOffset;
    private int arm9EntryAddress;
    private int arm9RamAddress;
    private int arm9Size;

    private int arm7RomOffset;
    private int arm7EntryAddress;
    private int arm7RamAddress;
    private int arm7Size;

    private int fntOffset;
    private int fntSize;

    private int fatOffset;
    private int fatSize;

    private int arm9OverlayOffset;
    private int arm9OverlaySize;

    private int arm7OverlayOffset;
    private int arm7OverlaySize;

    private int port40001A4hNormalCommand;
    private int port40001A4hKey1Command;

    private int iconOffset;

    private int secureAreaChecksum;
    private int secureAreaDelay;

    private int arm9AutoLoad;

    private int arm7AutoLoad;

    private long secureAreaDisable;

    private int usedRomSize;
    private int headerSize;

    private byte[] reserved2;
    private byte[] reserved3;

    private byte[] logo;
    private int logoChecksum;
    private int headerChecksum;

    private int debugRomOffset;
    private int debugSize;
    private int debugRamAddress;

    private int reserved4;
    private byte[] reserved5;

    /**
     * Read the header
     *
     * @param rom The stream where to read the information
     * @return A header
     * @throws IOException If something goes wrong
     */
    public static NitroHeader readHeader(jBinaryReader rom) throws IOException {
        NitroHeader header = new NitroHeader();
        header.gameTitle = rom.readString(12, StandardCharsets.US_ASCII).trim();
        header.gameCode = rom.readString(4, StandardCharsets.US_ASCII);
        header.makerCode = rom.readString(2, StandardCharsets.US_ASCII);
        header.unitCode = rom.readByte();
        header.encryptionSeedSelect = rom.readByte();
        header.deviceCapacity = rom.readByte();
        header.reserved1 = rom.readBuffer(7);
        header.dsiFlags = rom.readByte();
        header.ndsRegion = rom.readByte();
        header.romVersion = rom.readByte();
        header.autoStart = rom.readByte();

        header.arm9RomOffset = rom.readInt();
        header.arm9EntryAddress = rom.readInt();
        header.arm9RamAddress = rom.readInt();
        header.arm9Size = rom.readInt();

        header.arm7RomOffset = rom.readInt();
        header.arm7EntryAddress = rom.readInt();
        header.arm7RamAddress = rom.readInt();
        header.arm7Size = rom.readInt();

        header.fntOffset = rom.readInt();
        header.fntSize = rom.readInt();
        header.fatOffset = rom.readInt();
        header.fatSize = rom.readInt();

        header.arm9OverlayOffset = rom.readInt();
        header.arm9OverlaySize = rom.readInt();

        header.arm7OverlayOffset = rom.readInt();
        header.arm7OverlaySize = rom.readInt();

        header.port40001A4hNormalCommand = rom.readInt();
        header.port40001A4hKey1Command = rom.readInt();

        header.iconOffset = rom.readInt();
        header.secureAreaChecksum = rom.readShort();
        header.secureAreaDelay = rom.readShort();
        header.arm9AutoLoad = rom.readInt();
        header.arm7AutoLoad = rom.readInt();
        header.secureAreaDisable = rom.readLong();
        header.usedRomSize = rom.readInt();
        header.headerSize = rom.readInt();
        header.reserved2 = rom.readBuffer(0x28);
        header.reserved3 = rom.readBuffer(0x10);

        header.logo = rom.readBuffer(0x9c);
        header.logoChecksum = rom.readShort();
        header.headerChecksum = rom.readShort();
        header.debugRomOffset = rom.readInt();
        header.debugSize = rom.readInt();
        header.debugRamAddress = rom.readInt();
        header.reserved4 = rom.readInt();
        header.reserved5 = rom.readBuffer(0x90);
        return header;
    }

    /**
     * Write the header
     *
     * @param header The header to write
     * @param rom    The stream where to write the information
     */
    public static void writeHeader(NitroHeader header, jBinaryWriter rom) throws IOException {
        rom.writeString(header.gameTitle, 12);
        rom.writeString(header.gameCode, 4);
        rom.writeString(header.makerCode, 2);
        rom.writeByte(header.unitCode);
        rom.writeByte(header.encryptionSeedSelect);
        rom.writeByte(header.deviceCapacity);
        rom.writeBytes(header.reserved1);
        rom.writeByte(header.dsiFlags);
        rom.writeByte(header.ndsRegion);
        rom.writeByte(header.romVersion);
        rom.writeByte(header.autoStart);

        rom.writeInt(header.arm9RomOffset);
        rom.writeInt(header.arm9EntryAddress);
        rom.writeInt(header.arm9RamAddress);
        rom.writeInt(header.arm9Size);

        rom.writeInt(header.arm7RomOffset);
        rom.writeInt(header.arm7EntryAddress);
        rom.writeInt(header.arm7RamAddress);
        rom.writeInt(header.arm7Size);

        rom.writeInt(header.fntOffset);
        rom.writeInt(header.fntSize);
        System.out.println("\nFAT Offset: " + header.fatOffset);
        rom.writeInt(header.fatOffset);
        System.out.println("FAT Size: " + header.fatSize);
        System.out.println("Num Files: " + header.fatSize/8);
        rom.writeInt(header.fatSize);


        rom.writeInt(header.arm9OverlayOffset);
        rom.writeInt(header.arm9OverlaySize);

        rom.writeInt(header.arm7OverlayOffset);
        rom.writeInt(header.arm7OverlaySize);

        rom.writeInt(header.port40001A4hNormalCommand);
        rom.writeInt(header.port40001A4hKey1Command);

        rom.writeInt(header.iconOffset);
        rom.writeShort(header.secureAreaChecksum);
        rom.writeShort(header.secureAreaDelay);
        rom.writeInt(header.arm9AutoLoad);
        rom.writeInt(header.arm7AutoLoad);
        rom.writeLong(header.secureAreaDisable);
        rom.writeInt(header.usedRomSize);
        rom.writeInt(header.headerSize);
        rom.writeBytes(header.reserved2);
        rom.writeBytes(header.reserved3);

        rom.writeBytes(header.logo, 0x9c);
        rom.writeShort(header.logoChecksum);
        rom.writeShort(header.headerChecksum);
        rom.writeInt(header.debugRomOffset);
        rom.writeInt(header.debugSize);
        rom.writeInt(header.debugRamAddress);
        rom.writeInt(header.reserved4);
        rom.writeBytes(header.reserved5);
    }

    public static void updateHeaderChecksum(NitroHeader header) {
        ByteBuffer tmpHeader = ByteBuffer.allocate(0x8000).order(ByteOrder.LITTLE_ENDIAN);
        tmpHeader.put(header.gameCode.getBytes());
        tmpHeader.put(header.gameTitle.getBytes());
        tmpHeader.put(header.makerCode.getBytes());
        tmpHeader.put((byte) header.unitCode);
        tmpHeader.put((byte) header.encryptionSeedSelect);
        tmpHeader.put((byte) header.deviceCapacity);
        tmpHeader.put(header.reserved1);
        tmpHeader.put((byte) header.dsiFlags);
        tmpHeader.put((byte) header.ndsRegion);
        tmpHeader.put((byte) header.romVersion);
        tmpHeader.put((byte) header.autoStart);
        tmpHeader.putInt(header.arm9RomOffset);
        tmpHeader.putInt(header.arm9EntryAddress);
        tmpHeader.putInt(header.arm9RamAddress);
        tmpHeader.putInt(header.arm9Size);
        tmpHeader.putInt(header.arm7RomOffset);
        tmpHeader.putInt(header.arm7EntryAddress);
        tmpHeader.putInt(header.arm7RamAddress);
        tmpHeader.putInt(header.arm7Size);
        tmpHeader.putInt(header.fntOffset);
        tmpHeader.putInt(header.fntSize);
        tmpHeader.putInt(header.fatOffset);
        tmpHeader.putInt(header.fatSize);
        tmpHeader.putInt(header.arm9OverlayOffset);
        tmpHeader.putInt(header.arm9OverlaySize);
        tmpHeader.putInt(header.arm7OverlayOffset);
        tmpHeader.putInt(header.arm7OverlaySize);
        tmpHeader.putInt(header.port40001A4hNormalCommand);
        tmpHeader.putInt(header.port40001A4hKey1Command);
        tmpHeader.putInt(header.iconOffset);
        tmpHeader.putShort((short) header.secureAreaChecksum);
        tmpHeader.putShort((short) header.secureAreaDelay);
        tmpHeader.putInt(header.arm9AutoLoad);
        tmpHeader.putInt(header.arm7AutoLoad);
        tmpHeader.putLong(header.secureAreaDisable);
        tmpHeader.putInt(header.usedRomSize);
        tmpHeader.putInt(header.headerSize);
        tmpHeader.put(header.reserved2);
        tmpHeader.put(header.reserved3);
        tmpHeader.put(header.logo);
        tmpHeader.putShort((short) header.logoChecksum);
        tmpHeader.putShort((short) header.headerChecksum);
        tmpHeader.putInt(header.debugRomOffset);
        tmpHeader.putInt(header.debugSize);
        tmpHeader.putInt(header.debugRamAddress);
        tmpHeader.putInt(header.reserved4);
        tmpHeader.put(header.reserved5);

        tmpHeader.flip();
        byte[] headerBytes = tmpHeader.array();

        header.headerChecksum = CRC16.calculateCrc(Arrays.copyOfRange(headerBytes,0,0x15e));
        header.logoChecksum = CRC16.calculateCrc(Arrays.copyOfRange(headerBytes,0xc0,0x15c));
        header.secureAreaChecksum = CRC16.calculateCrc(Arrays.copyOfRange(headerBytes, header.getArm9RomOffset(), 0x8000 - header.getArm9RomOffset()));

        tmpHeader.clear();
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getMakerCode() {
        return makerCode;
    }

    public void setMakerCode(String makerCode) {
        this.makerCode = makerCode;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(int unitCode) {
        this.unitCode = unitCode;
    }

    public int getEncryptionSeedSelect() {
        return encryptionSeedSelect;
    }

    public void setEncryptionSeedSelect(int encryptionSeedSelect) {
        this.encryptionSeedSelect = encryptionSeedSelect;
    }

    public int getDeviceCapacity() {
        return deviceCapacity;
    }

    public void setDeviceCapacity(int deviceCapacity) {
        this.deviceCapacity = deviceCapacity;
    }

    public int getNdsRegion() {
        return ndsRegion;
    }

    public void setNdsRegion(int ndsRegion) {
        this.ndsRegion = ndsRegion;
    }

    public int getRomVersion() {
        return romVersion;
    }

    public void setRomVersion(int romVersion) {
        this.romVersion = romVersion;
    }

    public int getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(int autoStart) {
        this.autoStart = autoStart;
    }

    public int getArm9RomOffset() {
        return arm9RomOffset;
    }

    public void setArm9RomOffset(int arm9RomOffset) {
        this.arm9RomOffset = arm9RomOffset;
    }

    public int getArm9EntryAddress() {
        return arm9EntryAddress;
    }

    public void setArm9EntryAddress(int arm9EntryAddress) {
        this.arm9EntryAddress = arm9EntryAddress;
    }

    public int getArm9RamAddress() {
        return arm9RamAddress;
    }

    public void setArm9RamAddress(int arm9RamAddress) {
        this.arm9RamAddress = arm9RamAddress;
    }

    public int getArm9Size() {
        return arm9Size;
    }

    public void setArm9Size(int arm9Size) {
        this.arm9Size = arm9Size;
    }

    public int getArm7RomOffset() {
        return arm7RomOffset;
    }

    public void setArm7RomOffset(int arm7RomOffset) {
        this.arm7RomOffset = arm7RomOffset;
    }

    public int getArm7EntryAddress() {
        return arm7EntryAddress;
    }

    public void setArm7EntryAddress(int arm7EntryAddress) {
        this.arm7EntryAddress = arm7EntryAddress;
    }

    public int getArm7RamAddress() {
        return arm7RamAddress;
    }

    public void setArm7RamAddress(int arm7RamAddress) {
        this.arm7RamAddress = arm7RamAddress;
    }

    public int getArm7Size() {
        return arm7Size;
    }

    public void setArm7Size(int arm7Size) {
        this.arm7Size = arm7Size;
    }

    public int getFntOffset() {
        return fntOffset;
    }

    public void setFntOffset(int fntOffset) {
        this.fntOffset = fntOffset;
    }

    public int getFntSize() {
        return fntSize;
    }

    public void setFntSize(int fntSize) {
        this.fntSize = fntSize;
    }

    public int getFatOffset() {
        return fatOffset;
    }

    public void setFatOffset(int fatOffset) {
        this.fatOffset = fatOffset;
    }

    public int getFatSize() {
        return fatSize;
    }

    public void setFatSize(int fatSize) {
        this.fatSize = fatSize;
    }

    public int getArm9OverlayOffset() {
        return arm9OverlayOffset;
    }

    public void setArm9OverlayOffset(int arm9OverlayOffset) {
        this.arm9OverlayOffset = arm9OverlayOffset;
    }

    public int getArm9OverlaySize() {
        return arm9OverlaySize;
    }

    public void setArm9OverlaySize(int arm9OverlaySize) {
        this.arm9OverlaySize = arm9OverlaySize;
    }

    public int getArm7OverlayOffset() {
        return arm7OverlayOffset;
    }

    public void setArm7OverlayOffset(int arm7OverlayOffset) {
        this.arm7OverlayOffset = arm7OverlayOffset;
    }

    public int getArm7OverlaySize() {
        return arm7OverlaySize;
    }

    public void setArm7OverlaySize(int arm7OverlaySize) {
        this.arm7OverlaySize = arm7OverlaySize;
    }

    public int getPort40001A4hNormalCommand() {
        return port40001A4hNormalCommand;
    }

    public void setPort40001A4hNormalCommand(int port40001A4hNormalCommand) {
        this.port40001A4hNormalCommand = port40001A4hNormalCommand;
    }

    public int getPort40001A4hKey1Command() {
        return port40001A4hKey1Command;
    }

    public void setPort40001A4hKey1Command(int port40001A4hKey1Command) {
        this.port40001A4hKey1Command = port40001A4hKey1Command;
    }

    public int getIconOffset() {
        return iconOffset;
    }

    public void setIconOffset(int iconOffset) {
        this.iconOffset = iconOffset;
    }

    public int getSecureAreaChecksum() {
        return secureAreaChecksum;
    }

    public void setSecureAreaChecksum(int secureAreaChecksum) {
        this.secureAreaChecksum = secureAreaChecksum;
    }

    public int getSecureAreaDelay() {
        return secureAreaDelay;
    }

    public void setSecureAreaDelay(int secureAreaDelay) {
        this.secureAreaDelay = secureAreaDelay;
    }

    public int getArm9AutoLoad() {
        return arm9AutoLoad;
    }

    public void setArm9AutoLoad(int arm9AutoLoad) {
        this.arm9AutoLoad = arm9AutoLoad;
    }

    public int getArm7AutoLoad() {
        return arm7AutoLoad;
    }

    public void setArm7AutoLoad(int arm7AutoLoad) {
        this.arm7AutoLoad = arm7AutoLoad;
    }

    public long getSecureAreaDisable() {
        return secureAreaDisable;
    }

    public void setSecureAreaDisable(long secureAreaDisable) {
        this.secureAreaDisable = secureAreaDisable;
    }

    public int getUsedRomSize() {
        return usedRomSize;
    }

    public void setUsedRomSize(int usedRomSize) {
        this.usedRomSize = usedRomSize;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public int getLogoChecksum() {
        return logoChecksum;
    }

    public void setLogoChecksum(int logoChecksum) {
        this.logoChecksum = logoChecksum;
    }

    public int getHeaderChecksum() {
        return headerChecksum;
    }

    public void setHeaderChecksum(int headerChecksum) {
        this.headerChecksum = headerChecksum;
    }

    public int getDebugRomOffset() {
        return debugRomOffset;
    }

    public void setDebugRomOffset(int debugRomOffset) {
        this.debugRomOffset = debugRomOffset;
    }

    public int getDebugSize() {
        return debugSize;
    }

    public void setDebugSize(int debugSize) {
        this.debugSize = debugSize;
    }

    public int getDebugRamAddress() {
        return debugRamAddress;
    }

    public void setDebugRamAddress(int debugRamAddress) {
        this.debugRamAddress = debugRamAddress;
    }
}
