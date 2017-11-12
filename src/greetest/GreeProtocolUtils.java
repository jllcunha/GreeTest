/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greetest;

import java.net.DatagramPacket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;


public class GreeProtocolUtils {
    private final static Charset UTF8_CHARSET = Charset.forName("UTF-8");
    
    public GreeProtocolUtils() {
    }
    
    public byte[] CreateScanRequest()
    {
        String sentence = "{\"t\": \"scan\"}";
        return sentence.getBytes();
    }
    
    public byte[] CreateBindingRequest(GreeDevice device) throws Exception
    {
        JSONObject sendBindRequestPacket = new JSONObject();
        sendBindRequestPacket.put("mac", device.getId());
        sendBindRequestPacket.put("t", "bind");
        sendBindRequestPacket.put("uid", 0);
        
        // Now Encrypt the Binding Request pack
        String encryptedBindReqPacket = CryptoUtil.encryptPack(CryptoUtil.GetAESGeneralKeyByteArray(), sendBindRequestPacket.toString());
        JSONObject sendBindRequest = new JSONObject();
        sendBindRequest.put("cid", "app");
        sendBindRequest.put("i", 1);
        sendBindRequest.put("t", "pack");
        sendBindRequest.put("uid", 0);
        sendBindRequest.put("pack", new String(encryptedBindReqPacket.getBytes(), UTF8_CHARSET));
        String bindReqStr = sendBindRequest.toString();
        return bindReqStr.getBytes();

    }

    byte[] createDeviceCommandRequest(GreeDevice device, HashMap<String,Integer> parameters) throws Exception
    {
        if (parameters.isEmpty())
            return null;

        JSONObject commandRequestPacket = new JSONObject();
        
        // Convert the parameter map values to arrays
        String[] keyArray = parameters.keySet().toArray(new String[0]);
        Integer[] valueArray = parameters.values().toArray(new Integer[0]);
        
        // Out the command values into the Packet
        commandRequestPacket.put("opt", new JSONArray(keyArray));
        commandRequestPacket.put("p", new JSONArray(valueArray));
        commandRequestPacket.put("t", "cmd");
        
        
        // Now Encrypt the Binding Request pack
        String encryptedCommandReqPacket = CryptoUtil.encryptPack(device.getKey().getBytes(), commandRequestPacket.toString());
        String unencryptedCommandReqPacket = CryptoUtil.decryptPack(device.getKey().getBytes(), encryptedCommandReqPacket);
        JSONObject sendCommandRequest = new JSONObject();
        sendCommandRequest.put("cid", "app");
        sendCommandRequest.put("i", 0);
        sendCommandRequest.put("t", "pack");
        sendCommandRequest.put("uid", 0);
        sendCommandRequest.put("pack", new String(encryptedCommandReqPacket.getBytes(), UTF8_CHARSET));
        String commandReqStr = sendCommandRequest.toString();
        return commandReqStr.getBytes();

    }

}
