/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greetest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;


public class GreeDeviceFinder {
 
    protected InetAddress mIPAddress = null;
    protected HashMap<String,GreeDevice> mDevicesHashMap = new HashMap<>();
    
    public GreeDeviceFinder() throws UnknownHostException
    {
        mIPAddress = InetAddress.getByName("192.168.1.255");
    }
    
    public void Scan(DatagramSocket clientSocket) throws IOException, Exception
    {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[347];
        
        // Send the Scan message
        GreeProtocolUtils protocolUtils = new GreeProtocolUtils();
        sendData = protocolUtils.CreateScanRequest();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, mIPAddress, 7000);
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
        String decryptedMsg = CryptoUtil.decryptPack(CryptoUtil.GetAESGeneralKeyByteArray(), pack);
//        System.out.println("Result: " + decryptedMsg);
  
        // Create the JSON to hold the response values
        JSONObject  readPackDataPacketJson=new JSONObject(decryptedMsg);
        String mac = readPackDataPacketJson.getString("mac");
        String mid = readPackDataPacketJson.getString("mid");
        String model = readPackDataPacketJson.getString("model");
        String name = readPackDataPacketJson.getString("name");
        String ver = readPackDataPacketJson.getString("ver");
        
        // Create a new GreeDevice
        GreeDevice newDevice = new GreeDevice ();
        newDevice.setId(mac);
        newDevice.setAddress(remoteAddress);
        newDevice.setPort(remotePort);
        newDevice.setmName(name);
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
