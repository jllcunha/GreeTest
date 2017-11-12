/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greetest;

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;
import java.nio.charset.Charset;
import java.util.HashMap;


/**
 *
 * @author John
 */
public class GreeTest {

    //private final static Charset UTF8_CHARSET = Charset.forName("UTF-8");
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
//        byte[] sendData = new byte[1024];
//        byte[] receiveData = new byte[347];

//        CryptoUtil cryptoUtil = new CryptoUtil();
//        byte[] generalKey = cryptoUtil.GetAESGeneralKeyByteArray();
        
        // Send the Scan message
        GreeDeviceFinder deviceFinder = new GreeDeviceFinder();
        deviceFinder.Scan(clientSocket);
        GreeDevice airconDevice = deviceFinder.GetDeviceByIPAddress("192.168.1.96");
        airconDevice.BindWithDevice(clientSocket);
        
        airconDevice.SetPoweredOn(clientSocket, Boolean.TRUE);
        
        Thread.sleep(20000);
        
        airconDevice.SetTemperature(clientSocket, 20);
        
        Thread.sleep(5000);
        
        airconDevice.SetPoweredOn(clientSocket, Boolean.FALSE);
        
        /*GreeProtocolUtils protocolUtils = new GreeProtocolUtils();
        sendData = protocolUtils.CreateScanRequest();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 7000);
        clientSocket.send(sendPacket);

        // Recieve a response
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        InetAddress remoteAddress = receivePacket.getAddress();
        String address = remoteAddress.getHostAddress();
        int remotePort = receivePacket.getPort();
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        //byte[] modifiedSentenceArray = receivePacket.getData();

        // Read the response
        JSONObject  readScanDataPacketJson=new JSONObject(modifiedSentence);
        String pack = readScanDataPacketJson.getString("pack");
        String id = readScanDataPacketJson.getString("cid");
        String decryptedMsg = cryptoUtil.decryptPack(generalKey, pack);
        System.out.println("Result: " + decryptedMsg);
  
        JSONObject  readPackDataPacketJson=new JSONObject(decryptedMsg);
        String mac = readPackDataPacketJson.getString("mac");
        String mid = readPackDataPacketJson.getString("mid");
        String model = readPackDataPacketJson.getString("model");
        String name = readPackDataPacketJson.getString("name");
        String ver = readPackDataPacketJson.getString("ver");
        */
//        GreeDevice firstDevice = deviceFinder.GetFirstDevice();
        
        // Now create a Binding Request
/*        JSONObject sendBindRequestPacket = new JSONObject();
        sendBindRequestPacket.put("mac", firstDevice.getId());
        sendBindRequestPacket.put("t", "bind");
        sendBindRequestPacket.put("uid", 0);
        
        // Now Encrypt the Binding Request pack
        String encryptedBindReqPacket = cryptoUtil.encryptPack(generalKey, sendBindRequestPacket.toString());
        String unencryptedBindReqPacket = cryptoUtil.decryptPack(generalKey, encryptedBindReqPacket);
        JSONObject sendBindRequest = new JSONObject();
        sendBindRequest.put("cid", "app");
        sendBindRequest.put("i", 1);
        sendBindRequest.put("t", "pack");
        sendBindRequest.put("uid", 0);
        sendBindRequest.put("pack", new String(encryptedBindReqPacket.getBytes(), UTF8_CHARSET));
        String bindReqStr = sendBindRequest.toString();
        sendData = bindReqStr.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, remoteAddress, remotePort);
        clientSocket.send(sendPacket);
        System.out.println("TO SERVER:" + bindReqStr);

        // Read the response
        clientSocket.receive(receivePacket);
        modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        readScanDataPacketJson=new JSONObject(modifiedSentence);
        pack = readScanDataPacketJson.getString("pack");
        decryptedMsg = cryptoUtil.decryptPack(generalKey, pack);
        System.out.println("Result: " + decryptedMsg);
        JSONObject  bindResponsePacketJson = new JSONObject(decryptedMsg);
        String resp = bindResponsePacketJson.getString("t");
        String bindkey = bindResponsePacketJson.getString("key");
//        String r = bindResponsePacketJson.getString("r");

        // Now lets power on the Aircon
        JSONObject sendPowOnRequestPacket = new JSONObject();
        sendPowOnRequestPacket.put("opt", "Pow");
        sendPowOnRequestPacket.put("p", 1);
        sendPowOnRequestPacket.put("t", "cmd");
        // Now Encrypt the Binding Request pack
        String encryptedPowOnReqPacket = cryptoUtil.encryptPack(bindkey.getBytes(), sendPowOnRequestPacket.toString());
        String unencryptedPowOnReqPacket = cryptoUtil.decryptPack(bindkey.getBytes(), encryptedPowOnReqPacket);
        JSONObject sendPowOnRequest = new JSONObject();
        sendPowOnRequest.put("cid", "app");
        sendPowOnRequest.put("i", 0);
        sendPowOnRequest.put("t", "pack");
        sendPowOnRequest.put("uid", 0);
        sendPowOnRequest.put("pack", new String(encryptedPowOnReqPacket.getBytes(), UTF8_CHARSET));
        String poOnReqStr = sendPowOnRequest.toString();
        sendData = poOnReqStr.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, remoteAddress, remotePort);
        clientSocket.send(sendPacket);
        System.out.println("TO SERVER:" + poOnReqStr);
        
        
        // Read the response
        clientSocket.receive(receivePacket);
        modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        readScanDataPacketJson=new JSONObject(modifiedSentence);
        pack = readScanDataPacketJson.getString("pack");
        decryptedMsg = cryptoUtil.decryptPack(bindkey.getBytes(), pack);
        System.out.println("Result: " + decryptedMsg);
        
*/
        
      clientSocket.close();
    }
    

}
