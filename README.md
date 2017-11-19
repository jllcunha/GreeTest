# GreeTest
A Java interface for controlling a Gree Airconditioner.
The code is written in Java using NetBeans 8.0.1
Based on the hard work from the guys working on and contributing to https://github.com/tomikaa87/gree-remote. 
I take no credit for the research that went into understanding how the interface works.

The application can be tested by using and modifying the greeTest.java file.

I've used the Google Gson library for handing DatagramPacket objects sent to and received from the Devices.
The process to communicating with an Air conditioner is as follows (as can be seen in the greeTest.java file):   
  - Use the GreeDeviceFinder object to scan for Gree Air Conditioners.
  - Retrieve a GreeDevice object from the GreeDeviceFinder. The GreeDevice object represents an airconditioner.
  - Next use the GreeDevice object to bind with the Airconditioner.
  - From this point onwards you can control the air conditioner using the GreeDevice object.

