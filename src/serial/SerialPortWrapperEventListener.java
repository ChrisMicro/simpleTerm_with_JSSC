package serial;

public interface SerialPortWrapperEventListener {

    //public abstract void serialEvent(int rxchar);
    public abstract void serialEvent(SerialPortWrapperEvent re);
}
