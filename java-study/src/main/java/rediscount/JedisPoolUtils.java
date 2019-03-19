package rediscount;

import cn.hutool.db.nosql.redis.RedisDS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/8 14:54.
 */
public class JedisPoolUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisPoolUtils.class);

    private JedisPoolUtils() { }

    private static volatile JedisPool jedisPool = null;

    public static JedisPool getJedisPoolInstance() {
        if (null == jedisPool) {
            synchronized (JedisPoolUtils.class) {
                if (null == jedisPool) {
                    //初始化连接池
                    initialPool();
                }
            }
        }
        return jedisPool;
    }

    /**
     * 初始化Redis连接池
     */
    private static void initialPool() {
        RedisDS redisDS = RedisDS.create();

        //获取配置文件

        //配置JedisPoolConfig
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(8);
        // 最大空闲数
        poolConfig.setMaxIdle(2);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        poolConfig.setMaxWaitMillis(2000);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestOnBorrow(false);

        //new JedisPool
        jedisPool = new JedisPool(poolConfig, "118.25.229.83", 16379, 2000, null, 0);
        LOGGER.info("Redis连接池初始化成功... JedisPool:{}", jedisPool.toString());
    }

}
