/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openhab.binding.greeair.internal;

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
import sun.misc.BASE64Encoder;
import java.nio.charset.Charset;
import java.util.HashMap;



public class GreeTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // Create a socket to communicate over
        DatagramSocket clientSocket = new DatagramSocket();
        

        // Scan for Gree Airconditioners
        GreeDeviceFinder deviceFinder = new GreeDeviceFinder(InetAddress.getByName("192.168.1.255"));
        deviceFinder.Scan(clientSocket);
        
        // I know that one is at a specific address so we'll be working with this one
        GreeDevice airconDevice = deviceFinder.GetDeviceByIPAddress("192.168.1.96");

        // Bind with the device
        airconDevice.BindWithDevice(clientSocket);
        
       
        // Set Power On
        airconDevice.SetDevicePower(clientSocket, 1);
        
        //Thread.sleep(5000);
        
        // Get the status of the device.
        airconDevice.getDeviceStatus(clientSocket);
        PrintDeviceStatus( airconDevice);
        
        airconDevice.SetDeviceSwingVertical(clientSocket, 0); //Full Range swing
        airconDevice.getDeviceStatus(clientSocket);
        PrintDeviceStatus( airconDevice);
        
        
        // Set the temperature to 20 degrees
        //airconDevice.SetTemperature(clientSocket, 20);
        
        Thread.sleep(5000);
        
        // Turn off the Air Condtioner
        airconDevice.SetDevicePower(clientSocket, 0);
        
        // Close the socket.
        clientSocket.close();
    }
    
    private static void PrintDeviceStatus(GreeDevice airconDevice)
    {
        GreeStatusResponsePack4Gson statuspack = airconDevice.getGreeStatusResponse4Gson().packJson;
        int numberValues = statuspack.cols.length;
        for (int n=0; n<numberValues; n++)
        {
            String output = statuspack.cols[n] + " = " + new Integer(statuspack.dat[n]).toString() + ", ";
            System.out.print(output);
        }
        System.out.print("\n");
    }

}
