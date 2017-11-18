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
public class GreeBindResponse4Gson {
    
    public String t = null;
    public int i = 0;
    public int uid = 0;
    public String cid = null;
    public String tcid = null;
    public String pack = null;
    
    public transient String decryptedPack = null;
    public transient GreeBindResponsePack4Gson packJson = null;  
}
