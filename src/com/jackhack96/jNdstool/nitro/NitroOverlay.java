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

/**
 * This class handles the overlays
 */
class NitroOverlay {
    private int id;
    private int ramAddress;
    private int ramSize;
    private int bssSize;
    private int stInitStart;
    private int stInitEnd;
    private int fileID;
    private int reserved;
    private int startOffset;
    private int endOffset;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRamAddress() {
        return this.ramAddress;
    }

    public void setRamAddress(int ramAddress) {
        this.ramAddress = ramAddress;
    }

    public int getRamSize() {
        return this.ramSize;
    }

    public void setRamSize(int ramSize) {
        this.ramSize = ramSize;
    }

    public int getBssSize() {
        return this.bssSize;
    }

    public void setBssSize(int bssSize) {
        this.bssSize = bssSize;
    }

    public int getStInitStart() {
        return this.stInitStart;
    }

    public void setStInitStart(int stInitStart) {
        this.stInitStart = stInitStart;
    }

    public int getStInitEnd() {
        return this.stInitEnd;
    }

    public void setStInitEnd(int stInitEnd) {
        this.stInitEnd = stInitEnd;
    }

    public int getFileID() {
        return this.fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public int getReserved() {
        return this.reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getStartOffset() {
        return this.startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return this.endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }
}
