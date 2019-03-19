package rediscount;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/8 14:53.
 */
public class RedisApp {

    public static void main(String[] args) {

        ExecutorService executorService = new ThreadPoolExecutor(
                10,
                20,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 100; i++) {
            executorService.submit(new RedisMutiThread(String.valueOf(i)));
        }

    }
}
