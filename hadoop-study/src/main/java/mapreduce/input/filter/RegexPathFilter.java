package mapreduce.input.filter;

import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

/**
 * 需要过滤的符合正则
 * 
 * @author 15257
 *
 */
public class RegexPathFilter implements PathFilter {

	private Configuration conf;
	FileSystem fs;
	private final String regex;
	private long fileLen;

	public RegexPathFilter(String regex, Configuration conf) {
		this.regex = regex;
		this.conf = conf;
		fileLen = 0;
	}

	@Override
	public boolean accept(Path path) {
        FileStatus fstatus= null;
        try {
            fs = FileSystem.get(conf);
            fstatus = fs.getFileStatus(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!fstatus.isDirectory()) {
        	if(path.getName().matches(regex)){
        		System.out.println(path.toString());//file:/D:/Hadoop/study/data/20180930/172.16.92.5_201809301111_wordcount.txt
    			System.out.println(path.getName());//172.16.92.5_201809301111_wordcount.txt
    			fileLen += fstatus.getLen();
    			conf.setLong("fileSize", fileLen);
    			return true;
    		}else{
    			return false;
    		}
        }
		return true;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public Configuration getConf() {
		return this.conf;
	}

}
