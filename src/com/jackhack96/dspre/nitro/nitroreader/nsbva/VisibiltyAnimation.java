/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jackhack96.dspre.nitro.nitroreader.nsbva;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class VisibiltyAnimation {
    
    private List<Boolean> animation;

    public VisibiltyAnimation(int numFrame) {
        animation = new ArrayList<>(numFrame);
    }
    
    public void addFrame(boolean value){
        animation.add(value);
    }
    
    public void setFrame(int pos, boolean value){
        animation.set(pos, value);
    }
    
    
}
