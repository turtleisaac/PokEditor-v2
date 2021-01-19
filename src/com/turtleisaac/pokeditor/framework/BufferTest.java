package com.turtleisaac.pokeditor.framework;

import java.io.File;
import java.util.Random;

class BufferTest {

    public static void main(String[] args) {

        String name  = System.getProperty("user.dir") + File.separator + "SoulSilver.nds";
        Buffer buffer1 = new Buffer(name);
        Buffer buffer2 = new Buffer(name);

        byte[] arr1 = new byte[1024*1024*10];
        byte[] arr2 = new byte[1024*1024*10];

        for (int i=0; i<arr1.length; i++) {
            arr1[i] = (byte)buffer1.readByte();
        }

        int x = 0;
        Random random = new Random(System.currentTimeMillis());
        while (x < arr2.length) {
            int toRead = Math.min(random.nextInt(99999)+1, arr2.length-x);
            byte[] t = buffer2.readBytes(toRead);
            for (int i=0; i<t.length; i++,x++) {
                arr2[x] = t[i];
                if (arr2[x] != arr1[x]) {
                    System.err.println("error at "+x);
                    System.exit(1);
                }
            }
        }

        for (int i=0; i<arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                throw new RuntimeException("differ at offset "+i);
            }
        }
    }

}