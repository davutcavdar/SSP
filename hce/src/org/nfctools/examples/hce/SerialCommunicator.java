package org.nfctools.examples.hce;
// derived from SUN's examples in the javax.comm package
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
//import javax.comm.*; // for SUN's serial/parallel port libraries
// for rxtxSerial library

//rxtx dll i system32 nin altýna at
//rxtx jar ý da external olarak ekle

public class SerialCommunicator implements Runnable, SerialPortEventListener {
   static CommPortIdentifier portId;
   static CommPortIdentifier saveportId;
   static Enumeration        portList;
   InputStream           inputStream;
   SerialPort           serialPort;
   Thread           readThread;

   static String        messageString = "Hello, world!";
   static OutputStream      outputStream;
   static boolean        outputBufferEmptyFlag = false;

//   public static void main(String[] args) {
//      boolean           portFound = false;
//      String           defaultPort;
//      
//      // determine the name of the serial port on several operating systems
//      String osname = System.getProperty("os.name","").toLowerCase();
//      
//      defaultPort = "COM6";
//          
//      if (args.length > 0) {
//         defaultPort = args[0];
//      } 
//
//      System.out.println("Set default port to "+defaultPort);
//      
//      
//      try {
//		
//		nulltest reader = new nulltest();	
//      } catch (NoSuchPortException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//      
//      
//            // parse ports and if the default port is found, initialized the reader
////      portList = CommPortIdentifier.getPortIdentifiers();
////      while (portList.hasMoreElements()) {
////         portId = (CommPortIdentifier) portList.nextElement();
////         if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
////            if (portId.getName().equals(defaultPort)) {
////               System.out.println("Found port: "+defaultPort);
////               portFound = true;
////               // init reader thread
////               nulltest reader = new nulltest();
////            } 
////         } 
////         
////      } 
////      if (!portFound) {
////         System.out.println("port " + defaultPort + " not found.");
////      } 
//      
//   } 

   public void initwritetoport() {
      // initwritetoport() assumes that the port has already been opened and
      //    initialized by "public nulltest()"

      try {
         // get the outputstream
         outputStream = serialPort.getOutputStream();
      } catch (IOException e) {}

//      try {
//         // activate the OUTPUT_BUFFER_EMPTY notifier
//         serialPort.notifyOnOutputEmpty(true);
//      } catch (Exception e) {
//         System.out.println("Error setting event notification");
//         System.out.println(e.toString());
//         System.exit(-1);
//      }
      
   }

   public void writetoport(byte[] data) {
    //  System.out.println("Writing \""+messageString+"\" to "+serialPort.getName());
      try {
         // write string to serial port
         outputStream.write(data); //messageString.getBytes());
      } catch (IOException e) {}
   }

   public SerialCommunicator() {
	   
      // initalize serial port
      try {
    	  portId = CommPortIdentifier.getPortIdentifier("COM4");
         serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
      } catch (PortInUseException e) {} 
      catch (NoSuchPortException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
      try {
         inputStream = serialPort.getInputStream();
      } catch (IOException e) {}
   
      try {
         serialPort.addEventListener(this);
      } catch (TooManyListenersException e) {}
      
      // activate the DATA_AVAILABLE notifier
      serialPort.notifyOnDataAvailable(true);
   
      try {
         // set port parameters
         serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, 
                     SerialPort.STOPBITS_1, 
                     SerialPort.PARITY_NONE);
      } catch (UnsupportedCommOperationException e) {}
     
      initwritetoport();
      try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      // start the read thread
 //     readThread = new Thread(this);
 //     readThread.start();
      
   }

   
   public void run() {
      // first thing in the thread, we initialize the write operation
  
      try {
         while (true) {
        	 Thread.sleep(2000);
            // write string to port, the serialEvent will read it
        	 byte[] data = new byte[1];
        	 data[0] = '1';
            writetoport(data);
            
         }
      } catch (InterruptedException e) {}
   } 

   public void serialEvent(SerialPortEvent event) {
      switch (event.getEventType()) {
      case SerialPortEvent.BI:
      case SerialPortEvent.OE:
      case SerialPortEvent.FE:
      case SerialPortEvent.PE:
      case SerialPortEvent.CD:
      case SerialPortEvent.CTS:
      case SerialPortEvent.DSR:
      case SerialPortEvent.RI:
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
         break;
      case SerialPortEvent.DATA_AVAILABLE:
         // we get here if data has been received
         byte[] readBuffer = new byte[20];
         try {
            // read data
            while (inputStream.available() > 0) {
               int numBytes = inputStream.read(readBuffer);
            } 
            // print data
            String result  = new String(readBuffer);
          //  System.out.println(result);
         } catch (IOException e) {}
   
         break;
      }
   } 

}