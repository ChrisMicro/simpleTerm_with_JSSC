package gui_elements;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CHObject extends JPanel
{

	public JLabel labelText;

	static int next_xPosition=0;
	static int next_yPosition=0;
	
	int width=300;
	int heigth=50;

	public CHObject(String name)
	{
		labelText=new JLabel(name);
	}
	
	// minimalistic layout manager
	public void setPosition(int x, int y)
	{
		super.setBounds(x,y,width,heigth);
		next_yPosition=y+heigth;	
		next_xPosition=x;
	}
	
}
