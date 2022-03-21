/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.shared;

import java.awt.Color;

/**
 *
 * @author Usuario
 */
public class Utils {

    public static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0f);
    public static final float[][] IDENTITY = new float[][]{{1f, 0f, 0f},{0f, 1f, 0f},{0f, 0f, 1f}};

    public static boolean flagComp(int n, int flag) {
        return (n & flag) == flag;
    }

    public static boolean flagComp(int n, long flag) {
        return (n & flag) == flag;
    }

    public static boolean flagComp(long n, int flag) {
        return (n & flag) == flag;
    }

    public static boolean flagComp(long n, long flag) {
        return (n & flag) == flag;
    }

    public static boolean tagComp(int value, ComparableTagEnum tag) {
        long tagValue = tag.getValue();
        if (tagValue < 0){
            tagValue += 0X100000000L;
        }
        return flagComp(value, tagValue);
    }

    public static boolean tagComp(long value, ComparableTagEnum tag) {
        long tagValue = tag.getValue();
        if (tagValue < 0){
            tagValue += 0X100000000L;
        }
        return flagComp(value, tagValue);
    }

    public static float[] crossProduct(float f00, float f01, float f02, float f10, float f11, float f12) {
        return new float[]{
            f01 * f12 - f02 * f11,
            f02 * f10 - f00 * f12,
            f00 * f11 - f01 * f10
        };
    }

    public static float[] getEulerRotation(float[][] matrix) {
        double x = Math.atan2(matrix[2][1], matrix[2][2]);
        double y = Math.atan2(-matrix[2][0], Math.sqrt(matrix[2][1] * matrix[2][1] + matrix[2][2] * matrix[2][2]));
        double z = Math.atan2(matrix[1][0], matrix[0][0]);

        x = Math.toDegrees(x);
        y = Math.toDegrees(y);
        z = Math.toDegrees(z);

        return new float[]{(float) x, (float) y, (float) z};
    }

    public static float[][] transposeMatrix(float[][] m) {
        float[][] temp = new float[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }

    public static float[][] getIdentityMatrix() {
        float[][] identity = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    identity[i][j] = 1;
                } else {
                    identity[i][j] = 0;
                }
            }
        }
        return identity;
    }

    public static Color u16ToColor(int value) {
        int red = value & 0x001f;
        int green = (value & 0x3e0) >> 5;
        int blue = (value & 0x7c00) >> 10;

        return new Color(expand31To255(red), expand31To255(green), expand31To255(blue));
    }

    public static Color u16ToColorAlpha(int value) {
        int red = value & 0x1f;
        int green = (value & 0x3e0) >> 5;
        int blue = (value & 0x7c00) >> 10;
        //int alpha = (value >> 15) * 255;
        boolean hasAlpha = (value >> 15) == 0;

        if (hasAlpha){
            return new Color(expand31To255(red), expand31To255(green), expand31To255(blue), 0);
        } else {
            return new Color(expand31To255(red), expand31To255(green), expand31To255(blue), 255);
        }

        //return new Color(expand31To255(red), expand31To255(green), expand31To255(blue), alpha);
        //return new Color(expand31To255(red), expand31To255(green), expand31To255(blue), hasAlpha);
    }

    public static int expand31To255(int value) {
        if (value == 0) {
            return 0;
        } else {
            return (value + 1) * 8 - 1;
        }
    }

    public static int expand7To255(int value) {
        if (value == 0) {
            return 0;
        } else {
            return (value + 1) * 32 - 1;
        }
    }

    /*public static int getBits(int n, int numBits, int bitPos) {
        long value = n;
        if (value < 0){
            value += 0X100000000L;
        }
        int mask = (int) Math.pow(2, numBits) - 1;
        mask <<= bitPos;
        return (int)((value & mask) >> bitPos);
    }*/

    public static boolean bitPosCheck(long n, int bitPos) {
        int bit = (int)Math.pow(2, bitPos);
        return (n & bit) == bit;
    }

    public static int getBits(long n, int numBits, int bitPos) {
        long value = n;
        if (value < 0){
            value += 0X100000000L;
        }
        int mask = (int) Math.pow(2, numBits) - 1;
        mask <<= bitPos;
        return (int)((value & mask) >> bitPos);
    }

    public static Color avgColor(Color colorA, Color colorB) {
        int rA = colorA.getRed();
        int gA = colorA.getGreen();
        int bA = colorA.getBlue();
        int rB = colorB.getRed();
        int gB = colorB.getGreen();
        int bB = colorB.getBlue();

        return new Color((rA + rB) / 2, (gA + gB) / 2, (bA + bB) / 2);
    }

    public static Color getColor2Mode3(Color colorA, Color colorB) {
        int rA = colorA.getRed();
        int gA = colorA.getGreen();
        int bA = colorA.getBlue();
        int rB = colorB.getRed();
        int gB = colorB.getGreen();
        int bB = colorB.getBlue();

        return new Color((rA * 5 + rB * 3) / 8, (gA * 5 + gB * 3) / 8, (bA * 5 + bB * 3) / 8);
    }

    public static Color getColor3Mode3(Color colorA, Color colorB) {
        int rA = colorA.getRed();
        int gA = colorA.getGreen();
        int bA = colorA.getBlue();
        int rB = colorB.getRed();
        int gB = colorB.getGreen();
        int bB = colorB.getBlue();

        return new Color((rA * 3 + rB * 5) / 8, (gA * 3 + gB * 5) / 8, (bA * 3 + bB * 5) / 8);
    }

    public static int getValueCount(int numFrame, int step){
        return (int)Math.ceil((double)numFrame / step) + (numFrame - 1) % step;
    }

}
