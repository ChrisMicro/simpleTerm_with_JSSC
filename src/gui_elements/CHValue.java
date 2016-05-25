package gui_elements;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CHValue extends JPanel
{
	String label;
	String value;
	
	JLabel labelText;
	JTextField valueText;
	
	public CHValue(String label, String value)
	{
		//super(new BorderLayout());
		this.label=label;
		this.value=value;
		labelText=new JLabel(label);
		super.add(labelText,"West");
		valueText=new JTextField(value);
		super.add(valueText,"East");
		valueText.setCaretColor(Color.WHITE);
	}
}
