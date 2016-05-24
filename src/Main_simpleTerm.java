import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import serial.SerialPortWrapper;
import serial.SerialPortWrapperEvent;
import serial.SerialPortWrapperEventListener;

import jssc.SerialPortList;


public class Main_simpleTerm implements KeyListener
{
	JFrame fenster;
	int textDimension_x=60;
	int textDimension_y=25;


	JTextArea TextBereich;

	SerialPortWrapper serial;


	public Main_simpleTerm()
	{
		fenster=new JFrame("java serial terminal");
		
		fenster.addWindowListener(
			new WindowAdapter() 
			{
			      public void windowClosing(WindowEvent e) 
			      {
			    	serial.close();
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
		TextBereich.getCaret().setBlinkRate(1000);
		
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
		
		//***********************************************************************************
		
		serial=new SerialPortWrapper();
	      
		//************************ show serial ports ****************************************
		TextBereich.append("serial port available on this system:\n\r");
		
		String[] portNames  = SerialPortList.getPortNames();
		
		for(int i = 0; i < portNames.length; i++)
		{
		    //System.out.println(portNames[i]);
		    TextBereich.append(portNames[i]+"\n\r");
		}
		
		TextBereich.append("\n\r");
		
		serial.addEventListener(new SerialCallBack(TextBereich));
		serial.open();

		/* ping echo test
	    try 
	    {
	        Thread.sleep(2000);           
	    } catch(InterruptedException ex) 
	    {
	        Thread.currentThread().interrupt();
	    }
	    
	    for(int n=0;n<1000;n++)
	    {
	    	TextBereich.append(""+(char)serial.ping(n+65));
	    }
		 */
	
	}

	static class SerialCallBack implements SerialPortWrapperEventListener
	{
		int NumberOfLinesUntilTextCleared=25;
		JTextArea outputTextBereich;
		
		public SerialCallBack(JTextArea outputTextBereich)
		{
			this.outputTextBereich=outputTextBereich;
		}
		
		private void outputText(final String rx)
		{
			SwingUtilities.invokeLater // prevent race condition due to slow UI
			(
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
		
		public void serialEvent(SerialPortWrapperEvent event)
		{
			outputText(event.getString());
		}
	}
	
	/*********************+ handle keyboard events *******************************/
	@Override
    public void keyTyped(KeyEvent e) {}
	
	@Override
    public void keyPressed(KeyEvent e) 
    {
        System.out.println("Taste: " + e.getKeyChar() + ", Code: " + e.getKeyCode()); 

		char c = e.getKeyChar();
		if(e.getKeyCode()!=16) serial.write(c);

    }
    
	@Override
	public void keyReleased(KeyEvent arg0) {}
	
	/************************** main ********************************************/
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