package serial;

public class ConfigurationStorage 
{
	String fileName;
	
	public ConfigurationStorage(String fileName)
	{
		this.fileName=fileName;
	}
	
	public String getConfigurationString( String valueName, String defaultValue )
	{
		return defaultValue;
	}
	
	public int getConfigurationInteger( String valueName, int defaultValue )
	{
		return defaultValue;
	}
}
