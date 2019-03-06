package mapreduce.output.format;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import business.utils.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

//此处的泛型是reduce端的输出的类型的key、 value
public class RedisRecordWriter extends RecordWriter<NullWritable, Text> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisRecordWriter.class);
	
	private Configuration conf;
	private Jedis jedis;
	private JedisCluster jedisCluster;
	private boolean isRedisCluster;
	private final String KEY_PREFIX = "test-";
	private int count;
	
	public RedisRecordWriter(Configuration conf) {
		super();
		this.conf = conf;
		RedisUtil redis = RedisUtil.getInstance(conf);
		isRedisCluster = redis.isCluster();
		if (isRedisCluster) {
			jedisCluster = redis.getCluster();
		} else {
			jedis = redis.getJedisPool().getResource();
		}
		LOGGER.info("RedisRecordWriter().............");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		count = 0;
	}


	/**
	 * reduce结果输出的具体实现
	 */
	@Override
	public void write(NullWritable key, Text value) throws IOException, InterruptedException {
		LOGGER.info("RedisRecordWriter.write().............");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		count ++;
		if (isRedisCluster) {
			jedisCluster.set(KEY_PREFIX + count, value.toString());
		} else {
			jedis.set(KEY_PREFIX + count, value.toString());
		}
	}

	
	/**
	 * 释放资源
	 */
	@Override
	public void close(TaskAttemptContext context) throws IOException, InterruptedException {
		LOGGER.info("RedisRecordWriter.close().............");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (isRedisCluster) {
			jedisCluster.close();
			LOGGER.info("释放redis连接......");
		} else {
			jedis.close();
			LOGGER.info("释放redis连接......");
		}
	}

}
