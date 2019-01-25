package mapreduce.input.filter;

import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 需要过滤的符合正则
 * 
 * @author 15257
 *
 */
public class RegexPathFilter implements PathFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegexPathFilter.class);

	private Configuration conf;
	FileSystem fs;
	private final String regex;//正则
	private final String key;//符合正则的该数据的总大小

	
	public RegexPathFilter(Configuration conf, String regex, String key) {
		this.conf = conf;
		this.regex = regex;
		this.key = key;
		LOGGER.info("开始匹配 {} 符合 {} 的文件...", key, regex);
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
        		LOGGER.info("符合正则的文件名path.getName(): {}", path.getName());
        		LOGGER.info("符合正则的文件路径path.toString(): {}", path.toString());
    			conf.setLong(key, conf.getLong(key, 0L) + fstatus.getLen());
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
