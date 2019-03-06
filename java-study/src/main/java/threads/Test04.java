package threads;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import config.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Test04 {

	private static final Logger LOGGER = LoggerFactory.getLogger(Test04.class);
	
	public static JedisPool jedisPool;
	
	//一个时间点的数据 key:201902201800
    public static HashMap<String, Map<String, String>> onemap = new HashMap<>();

	static {
		Configuration conf = new Configuration();
		conf.set("ADDR_ARRAY", "118.25.229.83:16379");
		conf.setBoolean("isCluster", false);
		RedisUtil redis = RedisUtil.getInstance(conf);
		jedisPool = redis.getJedisPool();
	}

	public static void main(String[] args) {
		
	}

	public static void test1() {
		// 定时任务
		Timer timer = new Timer();
		// 默认时间间隔 1min
		long PERIOD_TIME = 60 * 1000;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 2019-02-18 15:57:13
				DateTime now = DateUtil.date();
				LOGGER.info("执行定时任务时间：" + now);
				Jedis jedis = jedisPool.getResource();
				//1.获取锁
				if (jedis.setnx("test_lock", UUID.randomUUID().toString()+Thread.currentThread().getName()) == 0) {
					//没有获取到锁
					LOGGER.info("没有获取到锁,已有线程处理了,本线程不做任何处理,退出");
				} else {
					//设置锁过期时间,避免无法释放锁
					jedis.expire("test_lock", 30);
					// 2.更新
			        Map<String, String> thermalMapData = jedis.hgetAll("ThermalMapData");
			        onemap.clear();
			        onemap.put(DateUtil.format(now, "yyyyMMddHHmm"), thermalMapData);
			        String jsonStr = JSONUtil.toJsonStr(onemap);
			        String hot_point_list_key = "hot_point_" + DateUtil.format(now, "yyyyMMdd");
			        jedis.lpush(hot_point_list_key, jsonStr);
					LOGGER.info("已更新");
				}
				jedis.close();
			}
		}, PERIOD_TIME);
		// 当前时间 2019-02-18 15:56:13
		LOGGER.info("当前时间：" + DateUtil.now());
	}
}
