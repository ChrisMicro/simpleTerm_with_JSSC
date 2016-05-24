package serial;

public class SerialPortWrapperEvent 
{
	 public static final int RXCHAR = 1;
	 public static final int DATARECEIVED = 2;
	 
	 String message;
	 int eventType;
	 
	 public SerialPortWrapperEvent(int eventType, int eventValue)
	 {

	 }
	 
	 public SerialPortWrapperEvent(int eventType, String message)
	 {
		 this.message=message;
		 this.eventType=eventType;
	 }
	 
	 public String getString()
	 {
		 return message;
	 }
}
