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
 * This class represents a generic file in the Nitro file system
 */
class NitroFile implements Comparable<NitroFile> {
    private int id; // The file ID inside the ROM
    private int offset; // Absolute offset of the file
    private int size; // Size of the file
    private String name; // Name of the file
    private NitroDirectory parent; // Parent directory

    public NitroFile(String name, int id, int offset, int size, NitroDirectory parent) {
        this.name = name;
        this.id = id;
        this.offset = offset;
        this.size = size;
        this.parent = parent;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public NitroDirectory getParent() {
        return this.parent;
    }

    public void setParent(NitroDirectory parent) {
        this.parent = parent;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(NitroFile nitroFile) {
        return name.compareToIgnoreCase(nitroFile.name);
    }

    @Override
    public String toString() {
        return "NitroFile{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
