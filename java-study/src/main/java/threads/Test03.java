package threads;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.hadoop.conf.Configuration;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import config.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Test03 {

	public static void main(String[] args) {
//		Configuration conf = new Configuration();
//		conf.set("ADDR_ARRAY", "118.25.229.83:6379");
//		conf.setBoolean("isCluster", false);
//		RedisUtil redis = RedisUtil.getInstance(conf);
//		final JedisPool jedisPool = redis.getJedisPool();
		// 定时任务
		Timer timer = new Timer();
		// 默认时间间隔一分钟
		long PERIOD_TIME = 60 * 1000;
		// 当前时间 2019-02-18 15:56:13
		Date date = DateUtil.date();
		// 一天的开始 零点 2019-02-18 15:56:00
		Date beginOfMin = beginOfMin(date);
		// 明天的开始 零点 2019-02-18 15:57:00
		Date endOfMin = DateUtil.offsetMinute(beginOfMin, 1);
		final String outerKey = "KeyLocationHistoryData";
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date currentDate = DateUtil.date();
				// 明天的开始 零点 2019-02-18 15:57:00
				System.out.println("执行定时任务时间：" + currentDate);
//				// 2019-02-18 15:56
//				String oldDay = DateUtil.format(beginOfMin(currentDate), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
//				Jedis jedis = jedisPool.getResource();
//				// 1.GETSET命令获取当前计数器的值并且重置为0 机场、火车站、天安门
//				String airportNum = jedis.getSet("airport", "0");
//				String trainNum = jedis.getSet("train", "0");
//				String tiananmenNum = jedis.getSet("tiananmen", "0");
//
//				// 2.把当前获取到的计数器的值存入map作为历史数据 innerkey:时间 value:计数器的值
//				jedis.hset(outerKey, "airport_" + oldDay, airportNum);
//				jedis.hset(outerKey, "train_" + oldDay, trainNum);
//				jedis.hset(outerKey, "tiananmen_" + oldDay, tiananmenNum);
//				jedis.close();
			}
		}, /*endOfMin,*/ PERIOD_TIME);
		System.out.println("启动定时任务程序时间: " + date);
		System.out.println(beginOfMin);
		System.out.println("计划第一次运行定时任务的时间: " + endOfMin);
		System.out.println("从kafka拉取消息,每5s进行指标统计");
//		start();
	}

	public static Date beginOfMin(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new DateTime(calendar);
	}

	public static void start() {
		Configuration conf = new Configuration();
		conf.set("ADDR_ARRAY", "118.25.229.83:6379");
		conf.setBoolean("isCluster", false);
		RedisUtil redis = RedisUtil.getInstance(conf);
		final JedisPool jedisPool = redis.getJedisPool();
		// 定时任务
		Timer timer = new Timer();
		// 默认时间间隔5s
		long PERIOD_TIME = 5 * 1000;
		// 当前时间 2019-02-18 15:56:13
		Date date = DateUtil.date();
		// 2019-02-18 15:56:18
		DateTime endOfSecond = DateUtil.offsetSecond(date, 5);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date currentDate = DateUtil.date();
				// 2019-02-18 15:56:18
				System.out.println("执行定时任务时间：" + currentDate);
				Jedis jedis = jedisPool.getResource();
				// 更新计数器
				jedis.incrBy("airport", 5);
				jedis.incrBy("train", 5);
				jedis.incrBy("tiananmen", 5);
				jedis.close();
			}
		}, endOfSecond, PERIOD_TIME);
	}
}
