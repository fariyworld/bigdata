package mapreduce.job.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import business.utils.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;


public class MrTextMapper extends Mapper<LongWritable, Text, TestKey, Text> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MrTextMapper.class);
	
	private Map<String, String> cacheMap = new HashMap<>();
	
	private String inputStr;
	private Text outvalue = new Text(StringUtils.EMPTY);

	private Jedis jedis;
	private JedisCluster jedisCluster;
	private boolean isRedisCluster;
	
	@Override
	protected void setup(Mapper<LongWritable, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("MrTextMapper setup()");
		//map join
		super.setup(context);
		Configuration cfg = context.getConfiguration();
		RedisUtil redis = RedisUtil.getInstance(cfg);
		isRedisCluster = redis.isCluster();
		if (isRedisCluster) {
			jedisCluster = redis.getCluster();
		} else {
			jedis = redis.getJedisPool().getResource();
		}
		URI[] cacheFiles = context.getCacheFiles();
		if (cacheFiles != null && cacheFiles.length > 0) {
			Path names = new Path(cacheFiles[0]);
			LOGGER.info("getName() ->   {}", names.getName());
			LOGGER.info("toString() ->   {}", names.toString());
			if(cfg.getBoolean("local", false)){
				// toString()
				 BufferedReader reader = null;
				 try {
					 reader = new BufferedReader(new FileReader(new File(names.toString())));
					 String lineTxt = null;
					 while ((lineTxt = reader.readLine()) != null) {
						 String[] arr = lineTxt.split(":", -1);
						 cacheMap.put(arr[0], arr[1]);
					 }
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					// TODO: handle finally clause
					if(reader != null){
						reader.close();
					}
				}
			}else{
				// getName()
				 BufferedReader reader = null;
				 try {
					 reader = new BufferedReader(new FileReader(new File(names.getName())));
					 String lineTxt = null;
					 while ((lineTxt = reader.readLine()) != null) {
						 String[] arr = lineTxt.split(":", -1);
						 cacheMap.put(arr[0], arr[1]);
					 }
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					// TODO: handle finally clause
					if(reader != null){
						reader.close();
					}
				}
			}
		}
	}

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {	
		inputStr = value.toString();
		String[] arr = inputStr.split(",", -1);
		outvalue.set(inputStr.concat(",").concat(cacheMap.get(arr[0])));
		if (isRedisCluster) {
			LOGGER.info("redis连接情况: {}", jedisCluster.echo("PONG"));
		} else {
			LOGGER.info("redis连接情况: {}", jedis.ping());
		}
		TestKey testKey = new TestKey(new Text(StringUtils.EMPTY), new Text(arr[0]), new Text(arr[1]), new Text(arr[2]));
		context.write(testKey, outvalue);
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("MrTextMapper cleanup()");
		if (isRedisCluster) {
			jedisCluster.close();
			LOGGER.info("释放redis连接......");
		} else {
			jedis.close();
			LOGGER.info("释放redis连接......");
		}
		super.cleanup(context);
	}

	
}

