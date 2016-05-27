package gui_elements;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CHValue extends CHObject
{
//	JLabel labelText;
	JTextField valueText;
//	static int next_xPosition=0;
//	static int next_yPosition=0;
	
	int width=200;
	int heigth=25;
	
	public CHValue(String label, String value)
	{
		super(label);
		labelText=new JLabel(label);
		super.add(labelText,"West");
		valueText=new JTextField(value);
		super.add(valueText,"East");
		valueText.setCaretColor(Color.WHITE);
		valueText.setPreferredSize( new Dimension( 100, 20 ) );
		super.setBounds(next_xPosition,next_yPosition,width,heigth);
		next_yPosition+=heigth;
	}

	public void setValueText(String value)
	{
		valueText.setText(value);
	}
}
