package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PropertiesUtil {
	
	private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class);
	
	/**
	 * ָ�������ļ���Ϣ��ȡ
	 * @return
	 */
    public Properties getProperties(String path, String charset) {
    	//Ĭ���ļ�����
    	String filePath = "param.properties";
    	if (StringUtils.isNotBlank(path)) {
    		filePath = path;
    	}
    	InputStream inputStream = null;
        try {
        	inputStream = this.getClass().getClassLoader().getResourceAsStream("redis_config.properties");
        	LOGGER.info("inputStream == " + (inputStream == null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(inputStream, charset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
    
    public static Properties getRedisProperties(String path, String charset){
    	return new PropertiesUtil().getProperties(path, charset);
    }
    
    /**
     * ������Ϣ��ȡ
     * @param path
     * @return
     */
    public static String getInfoByKey(String path, String charset, String key) {
    	//������Ϣ��ȡ
    	Properties properties=getRedisProperties(path, charset);
    	//���ùؼ��ֶ�ȡ
        return properties.getProperty(key);
    }
    
}
