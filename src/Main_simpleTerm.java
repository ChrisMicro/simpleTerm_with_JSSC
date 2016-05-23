import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Main_simpleTerm 
{
	String portName = "/dev/ttyACM0";
  
	JFrame fenster;
	int textDimension_x=80;
	int textDimension_y=40;

	JTextArea TextBereich;
    
	static SerialPort serialPort;
  
	public void openSerialConnection()
	{
		TextBereich.append("trying to open port\n\r");
		TextBereich.append(portName+"\n\r");
		TextBereich.append("\n\r");
		    
		serialPort = new SerialPort(portName);
		try 
		{
		  
		    System.out.println("port open :" + serialPort.openPort());//Open port
		    serialPort.setParams(SerialPort.BAUDRATE_115200,
		            SerialPort.DATABITS_8,
		            SerialPort.STOPBITS_1,
		            SerialPort.PARITY_NONE);
		
		    int mask = SerialPort.MASK_RXCHAR;//Prepare mask
		    serialPort.setEventsMask(mask);//Set mask
		    serialPort.addEventListener(new SerialPortReader(TextBereich));//Add SerialPortEventListener
		   
		} catch (SerialPortException ex) 
		{
		    TextBereich.append("port error");
		    System.out.println(ex);
		}
	 }
    
	public void closeSerialConnection()
	{
		try 
		{
			serialPort.closePort();
		} catch (SerialPortException e) 
		{
			System.out.println("closePort error");
			e.printStackTrace();
		}
	}
	    
	public Main_simpleTerm()
	{
		fenster=new JFrame("java serial terminal");
		
		fenster.addWindowListener(
			new WindowAdapter() 
			{
			      public void windowClosing(WindowEvent e) 
			      {
			        closeSerialConnection();
			          System.exit(0);        
			      }        
			}
		);
	
		//************************ create window with text **********************************
		JPanel lowerPanel = new JPanel();
		
		fenster.getContentPane().add(lowerPanel, "South");
		
		TextBereich = new JTextArea(textDimension_y, textDimension_x);
		
		TextBereich.setLineWrap(true);
		TextBereich.setWrapStyleWord(true);
		lowerPanel.add(new JScrollPane(TextBereich));
		  
		fenster.pack();
		fenster.setVisible(true);
	      
		//************************ show serial ports ****************************************
		TextBereich.append("serial port available on this system:\n\r");
		  
		String[] portNames = SerialPortList.getPortNames();
		
		for(int i = 0; i < portNames.length; i++)
		{
		    System.out.println(portNames[i]);
		    TextBereich.append(portNames[i]+"\n\r");
		}
		TextBereich.append("\n\r");
		//***********************************************************************************
	
		openSerialConnection();
	
	}
  
	static class SerialPortReader implements SerialPortEventListener
	{
		int NumberOfLinesUntilTextCleared=40;
		JTextArea outputTextBereich;

		public SerialPortReader(JTextArea outputTextBereich)
		{
			this.outputTextBereich=outputTextBereich;
		}
	  
	  //SwingUtilities.invokeLater()
	  //{
		    public void serialEvent(SerialPortEvent event) 
		    {
		        if (event.isRXCHAR()) 
		        {
		          int numberOfBytes=event.getEventValue();
		            if (numberOfBytes > 1) 
		            {
		                try 
		                {
					        byte buffer[] = serialPort.readBytes(numberOfBytes);
					        int n;
					        String rxString="";
					        for(n=0;n<buffer.length;n++) rxString+=((char)(buffer[n]));
	
					        System.out.println(rxString);
					        if(outputTextBereich.getLineCount()>NumberOfLinesUntilTextCleared)outputTextBereich.setText(""); // clear panel
					        outputTextBereich.append(rxString);
		                } catch (SerialPortException ex) 
		                {
		                    System.out.println(ex);
		                }
		            }
		        } 
		    }
	}
	//}
    
	public static void main(String[] args) 
	{
		new Main_simpleTerm();
	}

}

/*************************************************

//Arduino Testsender-Code

void setup() 
{
	Serial.begin(115200);
}

int Counter=0;

void loop() 
{
	Serial.println(Counter++);
	delay(50);        
}

****************************************************/