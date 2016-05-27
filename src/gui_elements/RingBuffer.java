/*
============================================================================
Name        : ringBuffer.c
Author      : chris
Version     :
Copyright   : GPL lizense 3
              ( chris (at) roboterclub-freiburg.de )
Description : This ringBuffer overwrites circular the old values if
              the bufferSize is overflow.
              It is not a FIFO. 
              
History: original code was in C

============================================================================
*/

package gui_elements;


public class RingBuffer 
{
	double[] data;
	int bufferSize;
	int writePosition;
	int fillSize;
	
	public RingBuffer(int dataLength)
	{
		data=new double[dataLength];
		bufferSize=dataLength;
		writePosition = 0;
		fillSize      = 0;
	}
	
	public void setData(double[] data)
	{
		this.data=data;
		bufferSize=data.length;
		writePosition = 0;
		fillSize      = bufferSize;
	}
	public RingBuffer(double[] data)
	{
		this.setData(data);
		/*
		this.data=data;
		bufferSize=data.length;
		writePosition = 0;
		fillSize      = bufferSize;
		*/
	}
	
	public void ringBufferAdd( double value)
	{
	 data[writePosition] = value;

	 writePosition++;
	 // wrap arround if overflow
	 if ( writePosition >= bufferSize ) writePosition = 0;  
	 
	 fillSize++;
	 // if buffer is full, no longer increase fillSize 
	 if(fillSize >= bufferSize) fillSize = bufferSize;
	}

	//read a value from position n relative to the current write position
	public double ringBufGetValue( int index)
	{
	 int pos;

	 pos = writePosition + index - fillSize;

	 // if wrap arround
	 if ( pos < 0 ) pos += bufferSize; 

	 return data[pos];
	}

	public int ringBufGetFillSize( )
	{
	 return fillSize;
	}
}




