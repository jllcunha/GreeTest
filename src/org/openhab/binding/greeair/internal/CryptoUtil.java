/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openhab.binding.greeair.internal;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;


public class CryptoUtil {
    static String AES_General_Key = "a3K8Bx%2r8Y7#xDh";
    
    public static String GetAESGeneralKey()
    {
        return AES_General_Key;
    }
    
    public static byte[] GetAESGeneralKeyByteArray()
    {
        return AES_General_Key.getBytes();
    }
    
    public static String decryptPack(byte[] keyarray, String message) throws Exception 
    {
        String descrytpedMessage = null;
        try {
            Key key = new SecretKeySpec(keyarray, "AES");
            //BASE64Decoder decoder = new BASE64Decoder();
            Base64.Decoder decoder = Base64.getDecoder();
            //Decoder decoder = new Decoder();
            byte[] imageByte = decoder.decode(message);
            //byte[] imageByte = decoder.decodeBuffer(message);

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytePlainText = aesCipher.doFinal(imageByte);

            descrytpedMessage = new String(bytePlainText);
        } 
        catch (Exception ex) {
            System.out.println(ex);
        }
        return descrytpedMessage;
    }
    
    
    public static String encryptPack(byte[] keyarray, String message) throws Exception 
    {
        String encrytpedMessage = null;
        String oldencrytpedMessage = null;
        try {
            Key key = new SecretKeySpec(keyarray, "AES");
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytePlainText = aesCipher.doFinal(message.getBytes());
            String newString = new String(bytePlainText);

            Base64.Encoder newencoder = Base64.getEncoder();
            //BASE64Encoder oldencoder = new BASE64Encoder();
            //oldencrytpedMessage = oldencoder.encodeBuffer(bytePlainText);
            //oldencrytpedMessage = oldencrytpedMessage.substring(0, oldencrytpedMessage.length()-2);
            encrytpedMessage = new String(newencoder.encode(bytePlainText));
            encrytpedMessage = encrytpedMessage.substring(0, encrytpedMessage.length());
        } 
        catch (Exception ex) {
            System.out.println(ex);
        }
        return encrytpedMessage;
    }
}
