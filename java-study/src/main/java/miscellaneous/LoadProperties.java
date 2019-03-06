package miscellaneous;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class LoadProperties {
	
	public static Properties getProperties(String fileNameIn) {
		String fileName = "property.properties";
		if (StringUtils.isNotBlank(fileNameIn)) {
			fileName = fileNameIn;
		}
		InputStream inputStream = null;
		try {
			inputStream = LoadProperties.class.getClassLoader().getResourceAsStream(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Properties properties = new Properties();
		try {
			properties.load(new InputStreamReader(inputStream, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
