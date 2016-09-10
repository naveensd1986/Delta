package generics;

import java.io.FileInputStream;
import java.util.Properties;

public class Property 
{
	public static String getPropertyValue(String filePath, String key)
	{
		String value = "";
		Properties ppt=new Properties();
		try
		{
			ppt.load(new FileInputStream(filePath));
			value=ppt.getProperty(key);
			return value;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}

}
