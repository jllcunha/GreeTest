/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openhab.binding.greeair.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class GreeDeviceFinder {
 
    protected InetAddress mIPAddress = null;
    protected HashMap<String,GreeDevice> mDevicesHashMap = new HashMap<>();
    
    public GreeDeviceFinder(InetAddress broadcastAddress) throws UnknownHostException
    {
        mIPAddress = InetAddress.getByName("192.168.1.255");
    }
    
    public void Scan(DatagramSocket clientSocket) throws IOException, Exception
    {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        
        // Send the Scan message
        //GreeProtocolUtils protocolUtils = new GreeProtocolUtils();
        //sendData = protocolUtils.CreateScanRequest();
        GreeScanRequest4Gson scanGson = new GreeScanRequest4Gson();
        scanGson.t="scan";
        
        GsonBuilder gsonBuilder = new GsonBuilder();  
        gsonBuilder.setLenient();  
        Gson gson = gsonBuilder.create();
        String scanReq = gson.toJson(scanGson);
        sendData = scanReq.getBytes();
        
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, mIPAddress, 7000);
        clientSocket.send(sendPacket);
        
        // Recieve a response
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        InetAddress remoteAddress = receivePacket.getAddress();
        String address = remoteAddress.getHostAddress();
        int remotePort = receivePacket.getPort();
        
        // Read the response
        String modifiedSentence = new String(receivePacket.getData());
        StringReader stringReader = new StringReader(modifiedSentence);
        GreeScanResponse4Gson scanResponseGson = gson.fromJson(new JsonReader(stringReader), GreeScanResponse4Gson.class);
        scanResponseGson.decryptedPack = CryptoUtil.decryptPack(CryptoUtil.GetAESGeneralKeyByteArray(), scanResponseGson.pack);
        String decryptedMsg = CryptoUtil.decryptPack(CryptoUtil.GetAESGeneralKeyByteArray(), scanResponseGson.pack);
  
        // Create the JSON to hold the response values
        stringReader = new StringReader(decryptedMsg);
        scanResponseGson.packJson = gson.fromJson(new JsonReader(stringReader), GreeScanReponsePack4Gson.class);

        // Create a new GreeDevice
        GreeDevice newDevice = new GreeDevice ();
        newDevice.setAddress(remoteAddress);
        newDevice.setPort(remotePort);
        newDevice.setScanResponseGson(scanResponseGson);

        AddDevice(newDevice);
    }
    
    public void AddDevice(GreeDevice newDevice)
    {
        mDevicesHashMap.put(newDevice.getId(), newDevice);
    }
    
    public GreeDevice GetDevice(String id)
    {
        return mDevicesHashMap.get(id);
    }
    
    public HashMap<String,GreeDevice> GetDevices()
    {
        return mDevicesHashMap;
    }
            
    public GreeDevice GetFirstDevice()
    {
        Set keySet = mDevicesHashMap.keySet();
        Iterator iter = keySet.iterator();
        Object firstkey = (String)iter.next();
        return mDevicesHashMap.get(firstkey);
    }
    
    public GreeDevice GetDeviceByIPAddress(String ipAddress)
    {
        GreeDevice returnDevice = null;
        
        Set keySet = mDevicesHashMap.keySet();
        Iterator iter = keySet.iterator();
        while(returnDevice==null && iter.hasNext()) {
            Object thiskey = (String)iter.next();
            GreeDevice currDevice = mDevicesHashMap.get(thiskey);
            if (currDevice != null && currDevice.getAddress().getHostAddress().equals(ipAddress))
            {
                returnDevice = currDevice;
            }
        }

        return returnDevice;
    }
    

}
