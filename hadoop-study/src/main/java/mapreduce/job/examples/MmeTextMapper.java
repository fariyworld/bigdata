package mapreduce.job.examples;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import business.utils.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class MmeTextMapper extends Mapper<LongWritable, Text, TestKey, Text> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MmeTextMapper.class);

	private String inputStr;
//	private Text outvalue = new Text(StringUtils.EMPTY);
	private Jedis jedis;
	private JedisCluster jedisCluster;
	private boolean isRedisCluster;
	
	@Override
	protected void setup(Mapper<LongWritable, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("MmeTextMapper setup()");
		super.setup(context);
		Configuration conf = context.getConfiguration();
		RedisUtil redis = RedisUtil.getInstance(conf);
		isRedisCluster = redis.isCluster();
		if (isRedisCluster) {
			jedisCluster = redis.getCluster();
		} else {
			jedis = redis.getJedisPool().getResource();
		}
	}

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		inputStr = value.toString();
		String[] arr = inputStr.split(",", -1);
		TestKey testKey = new TestKey(new Text(arr[3]), new Text(arr[0]), new Text(arr[1]), new Text(arr[2]));;
		if (isRedisCluster) {
			LOGGER.info("redis连接情况: {}", jedisCluster.echo("PONG"));
		} else {
			LOGGER.info("redis连接情况: {}", jedis.ping());
		}
		context.write(testKey, value);
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("MmeTextMapper cleanup()");
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
