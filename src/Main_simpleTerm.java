import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Main_simpleTerm implements KeyListener
{
	String portName = "/dev/ttyACM0";
  
	JFrame fenster;
	int textDimension_x=60;
	int textDimension_y=25;
	boolean SerialPortOpenedFlag=false;

	JTextArea TextBereich;

	static SerialPort serialPort;
  	    
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
		TextBereich.setForeground(Color.GREEN);
		TextBereich.setBackground(Color.BLACK);
		
		TextBereich.setLineWrap(true);
		TextBereich.setWrapStyleWord(true);
		TextBereich.setEditable(false);
		lowerPanel.add(new JScrollPane(TextBereich));
		
		TextBereich.addKeyListener(this);
		
		/*// cursor 
        Caret caret = new DefaultCaret()
        {
            public void focusGained(FocusEvent e)
            {
                setVisible(true);
                setSelectionVisible(true);
            }
        };
        caret.setVisible(true);        
        caret.setBlinkRate( UIManager.getInt("TextField.caretBlinkRate") );
        TextBereich.setCaret(caret);
		*/  
		fenster.pack();
		fenster.setVisible(true);
	      
		//************************ show serial ports ****************************************
		TextBereich.append("serial port available on this system:\n\r");
		
		String[] portNames  = SerialPortList.getPortNames();
		
		for(int i = 0; i < portNames.length; i++)
		{
		    System.out.println(portNames[i]);
		    TextBereich.append(portNames[i]+"\n\r");
		}
		TextBereich.append("\n\r");
		//***********************************************************************************
		
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
  
		    }else TextBereich.append("no ports found, please exit\n\r");
		}else
		{
			TextBereich.append("no ports found, exit\n\r");
		}
	
	}

	public void openSerialConnection()
	{
		TextBereich.append("trying to open port\n\r");
		TextBereich.append(portName+"\n\r");
		    
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

		    
		    TextBereich.append("BAUDRATE_115200\n\r");
			TextBereich.append("\n\r");
		    
		    int mask = SerialPort.MASK_RXCHAR;//Prepare mask
		    serialPort.setEventsMask(mask);//Set mask
		    serialPort.addEventListener(new SerialPortReader(TextBereich));//Add SerialPortEventListener
		    SerialPortOpenedFlag=true;
		   
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
		int NumberOfLinesUntilTextCleared=25;
		JTextArea outputTextBereich;

		public SerialPortReader(JTextArea outputTextBereich)
		{
			this.outputTextBereich=outputTextBereich;
		}
		
		private void outputText(final String rx)
		{
		    SwingUtilities.invokeLater(
		    		new Runnable() 
		    		{
		    			@Override
		    			public void run() 
		    			{
		    				outputTextBereich.append(rx);
		    				if(outputTextBereich.getLineCount()>NumberOfLinesUntilTextCleared)outputTextBereich.setText(""); // clear panel
		    			}
		    		}
		    	);
		}

	    public void serialEvent(SerialPortEvent event) 
	    {
	        if (event.isRXCHAR()) 
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

	@Override
    public void keyTyped(KeyEvent e) {}
	
	@Override
    public void keyPressed(KeyEvent e) 
    {
        System.out.println("Taste: " + e.getKeyChar() + ", Code: " + e.getKeyCode()); 

        try 
        {
        	char c = e.getKeyChar();
        	if(SerialPortOpenedFlag && e.getKeyCode()!=16)    	serialPort.writeString(""+c);
		} catch (SerialPortException e1) 
		{
			e1.printStackTrace();
		}
    }
    
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) 
	{
		new Main_simpleTerm();
	}

}

/****************************************************

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
/****************************************************
//Arduino Serial Echo Code
   
 
void setup() 
{
  Serial.begin(115200);
  Serial.println("arduino serial echo ready");
}

void loop() 
{
  if (Serial.available()) 
  {
    Serial.write(Serial.read());
  }        
}
****************************************************/