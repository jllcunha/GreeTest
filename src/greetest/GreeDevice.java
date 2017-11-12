/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greetest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import org.json.JSONObject;

public class GreeDevice {
    private Boolean mIsBound = false;
    private InetAddress mAddress;
    private int mPort = 0;
    private String mKey;
    private String mName;
    private String mId;

    public void GreeDevice()
    {
        
    }
    
    
    public Boolean getIsBound() {
        return mIsBound;
    }

    public void setIsBound(Boolean isBound) {
        this.mIsBound = isBound;
    }

    public InetAddress getAddress() {
        return mAddress;
    }

    public void setAddress(InetAddress address) {
        this.mAddress = address;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        this.mPort = port;
    }
    
    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setmName(String name) {
        this.mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
    
    
    public void BindWithDevice(DatagramSocket clientSocket) throws Exception
    {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[347];
        
        // Send the Bind message
        GreeProtocolUtils protocolUtils = new GreeProtocolUtils();
        sendData = protocolUtils.CreateBindingRequest(this);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getAddress(), getPort());
        clientSocket.send(sendPacket);
        
        // Recieve a response
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        //System.out.println("FROM SERVER:" + modifiedSentence);

        // Read the response
        JSONObject  readScanDataPacketJson=new JSONObject(modifiedSentence);
        String pack = readScanDataPacketJson.getString("pack");
        String id = readScanDataPacketJson.getString("cid");
        String decryptedMsg = CryptoUtil.decryptPack(CryptoUtil.GetAESGeneralKeyByteArray(), pack);
        //System.out.println("Result: " + decryptedMsg);
        JSONObject  bindResponsePacketJson = new JSONObject(decryptedMsg);
        String resp = bindResponsePacketJson.getString("t");
        String bindkey = bindResponsePacketJson.getString("key");
        
        // Now set the Bind values in the Device object
        setKey(bindkey);
        setIsBound(Boolean.TRUE);
    }
    
    public void SetPoweredOn(DatagramSocket clientSocket, boolean on) throws Exception
    {
         // Only allow this to happen if this device has been bound
        if (getIsBound()!=Boolean.TRUE)
            return;
        
        // Set the values in the HashMap
        HashMap<String,Integer> parameters = new HashMap<>();
        parameters.put("Pow", on ? new Integer(1) : new Integer(0));
        
        ExecuteCommand(clientSocket, parameters);
    }
    
    public void SetTemperature(DatagramSocket clientSocket, int temperature) throws Exception
    {
         // Only allow this to happen if this device has been bound
        if (getIsBound()!=Boolean.TRUE)
            return;
        
        // Set the values in the HashMap
        HashMap<String,Integer> parameters = new HashMap<>();
        parameters.put("TemUn", new Integer(0));
        parameters.put("SetTem", new Integer(temperature));
        
        ExecuteCommand(clientSocket, parameters);
    }
   
    
    
    
    
    protected void ExecuteCommand(DatagramSocket clientSocket, HashMap<String,Integer> parameters) throws Exception
    {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        
        GreeProtocolUtils protocolUtils = new GreeProtocolUtils();
        sendData = protocolUtils.createDeviceCommandRequest(this, parameters);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getAddress(), getPort());
        clientSocket.send(sendPacket);
        
        // Recieve a response
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        //byte[] modifiedSentenceArray = receivePacket.getData();

        // Read the response
        JSONObject  readScanDataPacketJson=new JSONObject(modifiedSentence);
        String pack = readScanDataPacketJson.getString("pack");
        String id = readScanDataPacketJson.getString("cid");
        String decryptedMsg = CryptoUtil.decryptPack(this.getKey().getBytes(), pack);
        System.out.println("Result: " + decryptedMsg);
    }
}
