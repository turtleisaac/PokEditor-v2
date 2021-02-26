/*
 *
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
 * Copyright (c)  2020 JackHack96
 *
 */

package com.jackhack96.jNdstool.main;

import com.jackhack96.jNdstool.nitro.ROM;

import java.io.IOException;
import java.nio.file.Paths;

public class JNdstool {
    public static void main(String... args) throws IOException
    {
        if (args[0].equalsIgnoreCase("-x")) {
            ROM.extractROM(Paths.get(args[1]), Paths.get(args[2]));
        } else {
            ROM.buildROM(Paths.get(args[1]), Paths.get(args[2]));
        }
    }
}
