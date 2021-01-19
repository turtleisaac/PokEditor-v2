/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader;

import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dreader;
import com.jackhack96.dspre.nitro.nitroreader.shared.G3Dfile;
import com.jackhack96.dspre.nitro.nitroreader.shared.ByteReader;
import com.jackhack96.dspre.nitro.nitroreader.nsbca.NSBCAreader;
import com.jackhack96.dspre.nitro.nitroreader.nsbma.NSBMAreader;
import com.jackhack96.dspre.nitro.nitroreader.nsbta.NSBTAreader;
import com.jackhack96.dspre.nitro.nitroreader.nsbtx.NSBTXreader;
import com.jackhack96.dspre.nitro.nitroreader.nsbva.NSBVAreader;
import java.io.File;

public class NSReader {

    private final ByteReader data;

//    public NSReader(ByteReader data) {
//        this.data = data;
//    }
    public NSReader(File file) {
        data = new ByteReader(file);
    }

    public G3Dfile readFile() {
        G3Dreader reader = null;
        String kind = data.getStr(4);
        data.skip(-4); // go back
        switch (kind) {
            case "BCA0":
                reader = new NSBCAreader(data);
                break;
            case "BMA0":
                reader = new NSBMAreader(data);
                break;
            case "BVA0":
                reader = new NSBVAreader(data);
                break;
            case "BTA0":
                reader = new NSBTAreader(data);
                break;
            case "BTX0":
                reader = new NSBTXreader(data);
                break;
            default:
                throw new UnsupportedOperationException("cannot read file");
        }
        return reader.readFile();
    }

}
