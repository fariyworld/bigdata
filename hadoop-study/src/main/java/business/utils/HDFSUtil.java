package business.utils;

import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Hadoop文件系统 Java API
 * @author 15257
 *
 */
public class HDFSUtil {

//	private static final Logger LOGGER = Logger.getLogger(HDFSUtil.class);
	
	/*
	 * 通过 Hadoop URL 读取数据
	 */
	static{
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	
	public static void main(String[] args) {
		InputStream ins = null;
		try {
			ins = new URL("hdfs://bigdata:9000/test/data/wordcount.txt").openStream();
			IOUtils.copyBytes(ins, System.out, 1024*4, false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// TODO: handle finally clause
			IOUtils.closeStream(ins);
		}
	}
}
