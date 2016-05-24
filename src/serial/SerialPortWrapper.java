package serial;

import javax.swing.JOptionPane;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/*
This program uses JSSC

JSSC ist a serial interface driver for JAVA. It is used in the ARDUINO-IDE and should therefore run very stable on any computer platform.

It is hosted on GitHub here: https://github.com/scream3r/java-simple-serial-connector

How to integrate JSSC into your projects:

    Extract all file somewhere.
    In Eclipse: Project Properties -> Java Build path -> Libraries
    Press Add external jars and choose jssc.jar
 
 */

public class SerialPortWrapper 
{
	String portName = "/dev/ttyACM0";
	
	static SerialPort serialPort;
	SerialPortReader serPortReader;
	
	boolean SerialPortOpenedFlag=false;

	
	SerialPortWrapperEventListener listener;
	
	public SerialPortWrapper()
	{
		serPortReader=new SerialPortReader(listener);
	}

	public boolean open()
	{
		String[] portNames  = SerialPortList.getPortNames();
		
		for(int i = 0; i < portNames.length; i++)
		{
		    System.out.println(portNames[i]);
		}
		
		SerialPortOpenedFlag=false;
		
		if(portNames.length>0)
		{
		    portName = (String) JOptionPane.showInputDialog( null,
		              "Port",
		              "choose port",
		              JOptionPane.QUESTION_MESSAGE,
		              null, portNames,
		              portNames[0]);
		    
		    if(portName!=null)	
		    {
		    	openSerialConnection();
		    	SerialPortOpenedFlag=true;
  
		    }else System.out.println("no ports found, please exit\n\r");
		    
		}else
		{
			System.out.println("no ports found, exit\n\r");
		}
		return SerialPortOpenedFlag;

	}
	
	public void close()
	{
		closeSerialConnection();
	}
	
	public void write(char c)
	{
		try {
			serialPort.writeString(""+c);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * write a value to the serial port and receive a value afterwards
	 * 
	 */
	public int ping(int value) 
	{
		byte data[]={0};
		long timeout_ms=100; 
				
		try 
		{
		    serialPort.purgePort(SerialPort.PURGE_RXCLEAR); // clear receiver buffer
		    
		    serPortReader.blockReceiverEvent();
		    serialPort.writeByte((byte)value);
		    
		    /*
		    try 
		    {
		        Thread.sleep(100);           
		    } catch(InterruptedException ex) 
		    {
		        Thread.currentThread().interrupt();
		    }
		    */
		    long startTime=System.currentTimeMillis();
		    long stopTime=startTime+timeout_ms;
		    do
		    {
		    	
		    }while(serialPort.getInputBufferBytesCount()<1 && System.currentTimeMillis()<stopTime);
		    
			data=serialPort.readBytes();
			serPortReader.enableReceiverEvent();


		} catch (SerialPortException e) 
		{
			byte dat[]={0};
			data=dat;
			e.printStackTrace();
		}
		
		if(data!=null)		return (int) data[0];
		else return 0;
	}
	
    public void addEventListener(SerialPortWrapperEventListener listener) 
    {
    	this.listener=listener;
    }
    
	private void openSerialConnection()
	{
		serialPort = new SerialPort(portName);
		try 
		{
		  
		    System.out.println("port open :" + serialPort.openPort());//Open port
		    serialPort.setParams(SerialPort.BAUDRATE_115200,
		            SerialPort.DATABITS_8,
		            SerialPort.STOPBITS_1,
		            SerialPort.PARITY_NONE);
		    
		    // clear rx data
		    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
 
		    int mask = SerialPort.MASK_RXCHAR;//Prepare mask
		    serialPort.setEventsMask(mask);//Set mask
		    //serialPort.addEventListener(new SerialPortReader(listener));//Add SerialPortEventListener
		    serPortReader.setListener(listener);
		    serialPort.addEventListener(serPortReader);
		    SerialPortOpenedFlag=true;
		   
		} catch (SerialPortException ex) 
		{
		    System.out.println(ex);
		}
	 }
    
	private void closeSerialConnection()
	{
		try 
		{
			if(SerialPortOpenedFlag)
			{
				serialPort.closePort();		
			    SerialPortOpenedFlag=false;
			}

		} catch (SerialPortException e) 
		{
			System.out.println("closePort error");
			e.printStackTrace();
		}
	}


	static class SerialPortReader implements SerialPortEventListener
	{

		SerialPortWrapperEventListener listener;
		boolean ReceiverEventBlockedFlag=false;
		

		public SerialPortReader(SerialPortWrapperEventListener listener)
		{
			this.listener=listener;
		}
		
		public void setListener(SerialPortWrapperEventListener listener)
		{
			this.listener=listener;
		}
		private void outputText(final String rx)
		{
			SerialPortWrapperEvent ev=new SerialPortWrapperEvent(SerialPortWrapperEvent.DATARECEIVED,rx);
			if(listener!=null)	listener.serialEvent(ev);
		}

		public void blockReceiverEvent()
		{
			ReceiverEventBlockedFlag=true;
		}
		
		public void enableReceiverEvent()
		{
			ReceiverEventBlockedFlag=false;
		}

	    public void serialEvent(SerialPortEvent event) 
	    {
	        if (event.isRXCHAR() && !ReceiverEventBlockedFlag) 
	        {
	        	int numberOfBytes=event.getEventValue();
	            if (numberOfBytes > 0) 
	            {
	                try 
	                {
				        byte buffer[] = serialPort.readBytes(numberOfBytes);
				        int n;
				        String rxString="";
				        for(n=0;n<buffer.length;n++) rxString+=((char)(buffer[n]));

				        System.out.println(rxString);
				        outputText(rxString);
	                } catch (SerialPortException ex) 
	                {
	                    System.out.println(ex);
	                }
	            }
	        } 
	    }
	}


}
