package gui_elements;
import java.awt.Color;
import java.awt.Graphics;

public class CHGraph extends CHObject 
{
	private int xAchsePos;
	private int yAchsePos;
	private int xDimension=300;
	private int yDimension=200;
	
	double daten[];
	int dataSize=100;
	
	double scalex=1;
	double scaley=50;
	double xMax=10000;
	double xMin=0;
	
	RingBuffer buffer;
	
	public CHGraph(String graphName)
	{
		super(graphName);
		this.add(labelText);
		
		daten=new double[dataSize];

		xAchsePos=yDimension/2;
		yAchsePos=40;
		
		// dummy test data
		for(int n=0;n<dataSize;n++)daten[n]=Math.sin((double)n/5);
		buffer=new RingBuffer(daten);
		
		xMax=daten.length;		
		scalex=(xDimension-yAchsePos)/(xMax-xMin);
		
		width=xDimension;
		heigth=yDimension;
		super.setBounds(next_xPosition,next_yPosition,width,heigth);
		next_yPosition+=heigth;
	}
	
	public CHGraph(String graphName,double[] data)
	{
		this(graphName);
		
		buffer=new RingBuffer(data);
		
		daten=data;
		dataSize=daten.length;
		xMax=dataSize;		
		scalex=(xDimension-yAchsePos)/(xMax-xMin);
	}
	
	public void addValue(double value)
	{
		buffer.ringBufferAdd(value);
		repaint();
	}
	
	@Override 
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g); // original paintComponent aus JPanel aufrufen
		g.setColor(Color.blue);
		
		for(int n=0;n<(dataSize-2);n++)
		{

			int p1x=(int)(n*scalex);
			int p1y=(int)(buffer.ringBufGetValue(n)*scaley);
			int p2x=(int)((n+1)*scalex);
			int p2y=(int)(buffer.ringBufGetValue(n+1)*scaley);
			
			g.drawLine(yAchsePos+p1x,xAchsePos-p1y,yAchsePos+p2x,xAchsePos-p2y);

		}


		// draw axis
		g.setColor(Color.gray);
		g.drawLine(0,xAchsePos,xDimension,xAchsePos);
		g.drawLine(yAchsePos,0,yAchsePos,yDimension);

		// Beschriftung
		//g.drawString(""+y, xDimension/2, 10 );

		//g.drawString(""+y, 0, xAchsePos);
	}
}
