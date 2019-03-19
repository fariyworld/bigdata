package rediscount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/12 14:37.
 */
public class RedisMutiThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMutiThread.class);

    private String value;

    public RedisMutiThread(String value) {
        this.value = value;
    }

    @Override
    public void run() {
        JedisPool jedisPool = JedisPoolUtils.getJedisPoolInstance();
//        LOGGER.info("当前线程的JedisPool:{}", jedisPool.toString());
        Jedis jedis = jedisPool.getResource();
        // 多线程环境可能得不到 test_list 的长度为 10
        if (jedis.llen("test_list") < 10) {
            jedis.lpush("test_list", value);
        } else {
            jedis.brpop(10, "test_list");
            jedis.lpush("test_list", value);
        }
        jedis.close();
    }
}
