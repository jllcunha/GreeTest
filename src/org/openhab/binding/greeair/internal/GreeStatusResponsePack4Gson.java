/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openhab.binding.greeair.internal;

/**
 *
 * @author John
 */
public class GreeStatusResponsePack4Gson {

    public GreeStatusResponsePack4Gson(GreeStatusResponsePack4Gson other) {
        cols = new String[other.cols.length];
        dat = new Integer[other.dat.length];
        System.arraycopy(other.cols, 0, cols, 0, other.cols.length);
        System.arraycopy(other.dat, 0, dat, 0, other.dat.length);
    }

    public String t = null;
    public String mac = null;
    public int r = 0;
    public String[] cols = null;
    public Integer[] dat = null;
}
