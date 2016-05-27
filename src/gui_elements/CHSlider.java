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

public class CHSlider extends CHObject
{

	JTextField valueText;
	int sliderValue;
	
//	int width=300;
//	int heigth=50;

	public CHSlider(String label, int value, int min, int max)
	{
		super(label);
		super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		labelText=new JLabel(label);
		super.add(labelText);
		
		sliderValue=value;

		JSlider meinSlider = new JSlider();
		 
		meinSlider.setMinimum(min);

		meinSlider.setMaximum(max);

		int minorSpace=(max-min)/20;
		int majorSpace=minorSpace*5;
		
		meinSlider.setMajorTickSpacing(majorSpace);
		meinSlider.setMinorTickSpacing(minorSpace);
		//meinSlider.setMajorTickSpacing(5);
		//meinSlider.setMinorTickSpacing(1);
 		meinSlider.createStandardLabels(1);
 		meinSlider.setPaintTicks(true);
 		meinSlider.setPaintLabels(true);
 		meinSlider.setValue(value);
 		meinSlider.addChangeListener(new SliderListener());
 		super.add(meinSlider);
 		
		valueText=new JTextField(""+value);
		super.add(valueText);
		valueText.setCaretColor(Color.WHITE);
		valueText.setPreferredSize( new Dimension( 100, 20 ) );
			
		super.setBounds(next_xPosition,next_yPosition,width,heigth);
		next_yPosition+=heigth;
	}
	public CHSlider(String label, int value)
	{
		this(label, value, 0, 20);
	}

	public void setValueText(String value)
	{
		valueText.setText(value);
	}
	
	public int getInt()
	{
		return sliderValue;
	}
	
	class SliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) 
	    {
	        JSlider source = (JSlider)e.getSource();

            int value = (int)source.getValue();
            sliderValue=value;
            valueText.setText(""+value);
	    }
	}
}
