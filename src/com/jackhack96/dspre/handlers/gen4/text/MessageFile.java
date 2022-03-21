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

package com.jackhack96.dspre.handlers.gen4.text;

import com.jackhack96.jNdstool.io.jBinaryStream;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles Gen 4 text encoding/decoding
 */
public class MessageFile
{
    private static List<String> texts;
    private static int          seed;

    public static List<String> getTexts() {
        return texts;
    }

    public static void setTexts(List<String> texts) {
        MessageFile.texts = texts;
    }

    public static int getSeed() {
        return seed;
    }

    public static void setSeed(int seed) {
        MessageFile.seed = seed;
    }

    /**
     * Decode the given stream (assuming it's a text bank)
     *
     * @param reader BinaryStream of text bank
     * @throws IOException If any IO errors occurs
     */
    public static void decodeText(jBinaryStream reader) throws IOException {
        int num = reader.readShort();
        seed = reader.readShort();

        int[] offsets = new int[num];
        int[] sizes   = new int[num];

        int[][] binaryStrings = new int[num][];
        texts = new ArrayList<>();

        int key;
        // generate offsets and sizes
        for (int i = 0; i < num; i++) {
            key        = ((seed * (i + 1) * 0x2fd) & 0xffff) | ((seed * (i + 1) * 0x2fd0000) & 0xffff0000);
            offsets[i] = reader.readInt() ^ key;
            sizes[i]   = reader.readInt() ^ key;
        }

        InputStream inputStream = MessageFile.class.getResourceAsStream("/data/characters.json");
        JSONTokener tokener     = new JSONTokener(inputStream);
        JSONObject  characters  = new JSONObject(tokener);

        for (int i = 0; i < num; i++) {
            key              = (0x91bd3 * (i + 1)) & 0xffff;
            binaryStrings[i] = new int[sizes[i]];
            reader.seek(offsets[i]);

            // decrypt strings
            for (int j = 0; j < sizes[i]; j++) {
                binaryStrings[i][j] = reader.readShort() ^ key;
                key                 = (key + 0x493d) & 0xffff;
            }

            if (binaryStrings[i][0] == 0xf100) {
                binaryStrings[i] = MessageFile.decompress(binaryStrings[i]);
                sizes[i]         = binaryStrings[i].length;
            }

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < binaryStrings[i].length; j++) {
                int c = binaryStrings[i][j];
                if (c == 0xffff) {
                    // if the character is 0xffff, the string finishes here
                    break;
                } else if (c == 0xfffe) {
                    // if the character is 0xfffe, then it's a special VAR vase
                    text.append("VAR(");
                    StringBuilder args = new StringBuilder();
                    args.append(binaryStrings[i][++j]);
                    int argNum = binaryStrings[i][++j];
                    for (int k = 0; k < argNum; k++) {
                        args.append(", ");
                        args.append(binaryStrings[i][++j]);
                    }
                    args.append(")");
                    text.append(args);
                } else {
                    try {
                        text.append(characters.getJSONObject("getChar").get(String.valueOf(c)));
                    } catch (Exception e) {
                        text.append(String.format("\\?%04x", c));
                    }
                }
            }
            texts.add(text.toString());
        }
        inputStream.close();
    }

    /**
     * Encode the given stream (assuming it's a text bank)
     *
     * @param writer BinaryStream of text bank
     * @throws IOException If any IO errors occurs
     */
    public static void encodeText(jBinaryStream writer) throws IOException {
        ArrayList<Integer>            tmpBinaryString;
        ArrayList<ArrayList<Integer>> binaryStrings = new ArrayList<>();

        InputStream inputStream = MessageFile.class.getResourceAsStream("/data/characters.json");
        JSONTokener tokener     = new JSONTokener(inputStream);
        JSONObject  characters  = new JSONObject(tokener);

        // encode text and write it
        for (String text : texts) {
            tmpBinaryString = new ArrayList<>();

            // encode the binary strings
            for (int j = 0; j < text.length(); j++) {
                if (text.charAt(j) == '\\') {
                    switch (text.charAt(j + 1)) {
                        case 'r':
                            tmpBinaryString.add(0x25bc);
                            break;
                        case 'n':
                            tmpBinaryString.add(0xe000);
                            break;
                        case 'f':
                            tmpBinaryString.add(0x25bd);
                            break;
                        default:
                            tmpBinaryString.add(Integer.parseInt(text.substring(j + 2, j + 6), 16));
                            j += 4;
                            break;
                    }
                    j++;
                } else if (text.substring(j, Math.min(j + 4, text.length())).equals("VAR(")) {
                    int endOfVar = text.indexOf(')', j);
                    if (endOfVar == -1)
                        throw new RuntimeException("Could not find end of VAR()");
                    String[] args = text.substring(j + 4, endOfVar).split(",");
                    tmpBinaryString.add(0xfffe);
                    tmpBinaryString.add(Integer.parseInt(args[0].trim()));
                    tmpBinaryString.add(args.length - 1);
                    for (int x = 1; x < args.length; x++)
                         tmpBinaryString.add(Integer.parseInt(args[x].trim()));
                    j = endOfVar;
                } else {
                    tmpBinaryString.add(Integer.parseInt(characters.getJSONObject("getInt").get(String.valueOf(text.charAt(j))).toString()));
                }
            }
            binaryStrings.add(tmpBinaryString);
        }

        // generate offsets and sizes and write them
        writer.writeShort(texts.size());
        writer.writeShort(seed);

        int currentOffset = 4 + (texts.size() * 8);
        int key, currentSize;
        for (int i = 0; i < texts.size(); i++) {
            key         = ((seed * (i + 1) * 0x2fd) & 0xffff) | ((seed * (i + 1) * 0x2fd0000) & 0xffff0000);
            currentSize = binaryStrings.get(i).size() + 1;

            writer.writeInt(currentOffset ^ key); // write encrypted offset
            writer.writeInt(currentSize ^ key); // write encrypted string size

            currentOffset += currentSize * 2;
        }

        // actually write the encoded strings
        for (int i = 0; i < binaryStrings.size(); i++) {
            key = (0x91bd3 * (i + 1)) & 0xffff;
            for (int j = 0; j < binaryStrings.get(i).size(); j++) {
                writer.writeShort(binaryStrings.get(i).get(j) ^ key);
                key = (key + 0x493d) & 0xffff;
            }
            writer.writeShort(0xffff ^ key);
        }
        inputStream.close();
    }

    /**
     * Decompress the given 9-bit encoded string to normal 16-bit string
     *
     * @param compressedString Compressed 9-bit encoded string
     * @return Uncompressed 16-bit encoded string
     */
    public static int[] decompress(int[] compressedString) {
        // decompress string: characters are stored in 9 bits instead of 16
        int   container       = 0;
        int   bitshift        = 0;
        int   index           = 0;
        int[] newBinaryString = new int[compressedString.length * 2];

        for (int i = 1; i < compressedString.length; i++) {
            container |= compressedString[i] << bitshift;
            bitshift += 0xf;
            while (bitshift >= 0x9) {
                bitshift -= 0x9;
                if ((container & 0x1ff) == 0x1ff)
                    newBinaryString[index++] = 0xffff;
                else
                    newBinaryString[index++] = container & 0x1ff;
                container >>= 9;
            }
        }

        return newBinaryString;
    }

    /**
     * Compress the given 16-bit encoded string to 9-bit encoding
     *
     * @param uncompressedString Uncompressed 16-bit encoded string
     * @return Compressed 9-bit encoded string
     */
    public static int[] compress(int[] uncompressedString) throws RuntimeException {
        int   container       = 0;
        int   bitshift        = 0;
        int   index           = 0;
        int[] newBinaryString = new int[uncompressedString.length / 2];

        newBinaryString[0] = 0xf100;
        for (int i = 1; i < newBinaryString.length; i++) {
            int c = uncompressedString[index];
            if ((c >> 9) == 1)
                throw new RuntimeException(String.format("%04x cannot be compressed", c));
            container |= c << bitshift;
            bitshift += 9;
            while (bitshift >= 0xf) {
                bitshift -= 0xf;
                newBinaryString[index++] = container & 0xffff;
                container >>= 0xf;
            }
        }
        container |= 0xffff << bitshift;
        newBinaryString[index] = container & 0xffff;
        return newBinaryString;
    }

    /**
     * Calculate an (over-)estimation of the real encoded string size
     *
     * @param p String to encode
     * @return Encoded string size
     */
    public static int getSize(String p) {
        int size = 0;
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) == '\\') {
                switch (p.charAt(i + 1)) {
                    case 'r':
                    case 'n':
                    case 'f':
                        size++;
                        i++;
                        break;
                    case 'x':
                        size++;
                        i += 5;
                        break;
                }
            } else if (p.substring(i, Math.min(i + 4, p.length())).equals("VAR(")) {
                int endOfVar = p.indexOf(')', i + 4);
                if (endOfVar == -1)
                    throw new RuntimeException("Could not find end of VAR()");
                size += 3 + p.substring(i + 4, endOfVar).split(",").length - 1;
                i = endOfVar;
            } else
                size++;
        }
        size++;
        return size;
    }

    /**
     * Calculate an (over-)estimation of the total encoded strings size
     *
     * @param list List of strings to be encoded
     * @return The size in bytes
     */
    public static int getSize(List<String> list) {
        int size = 4 + (8 * list.size());
        for (String s : list) {
            size += getSize(s) * 2;
        }
        return size;
    }
}