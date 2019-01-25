package business.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
    /*
     * redis服务器ip1:port2;ip2:port2 如果为redis-cluster时, 指定master节点的 host1:port1,host2:port2
     */
    private String ADDR_ARRAY;
    /*
     * 密码
     */
    private String AUTH;
    /*
     * 可用连接实例的最大数目，默认值为8
     * 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
     */
    private int MAX_ACTIVE = 8;
    /*
     * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
     */
    private int MAX_IDLE = 2;
    /*
     * 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException
     */
    private int MAX_WAIT = -1;
    /*
     * 超时时间
     */
    private int TIMEOUT = 60000;
    /*
     * 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
     */
    private boolean TEST_ON_BORROW = true;
    private boolean TEST_ON_RETURN = true;
    /*
     * 是否为集群
     */
    private boolean hasCluster = false;
    /*
     * redis连接池
     */
    private JedisPool jedisPool = null;
    /*
     * 集群连接
     */
    private JedisCluster cluster=null;
    
    private static volatile RedisUtil redis;
    
	public RedisUtil(Configuration conf) {
    	this.ADDR_ARRAY = conf.get("ADDR_ARRAY");
    	this.AUTH = conf.get("AUTH");
    	this.MAX_ACTIVE = conf.getInt("MAX_ACTIVE", 8);
    	this.MAX_IDLE = conf.getInt("MAX_IDLE", 4);
    	this.MAX_WAIT = conf.getInt("MAX_WAIT", -1);
    	this.TIMEOUT = conf.getInt("TIMEOUT", 0);
    	this.hasCluster = conf.getBoolean("isCluster", false);
    	initialPool();
	}

	/**
     * 初始化Redis连接池
     */
    private void initialPool() {

    	if (StringUtils.isBlank(ADDR_ARRAY)) {
    		LOGGER.error("ADDR_ARRAY为空");
    		throw new IllegalArgumentException("Redis ADDR_ARRAY为空");
    	}
		JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(MAX_ACTIVE);
        // 最大空闲数
        poolConfig.setMaxIdle(MAX_IDLE);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        poolConfig.setMaxWaitMillis(MAX_WAIT);
        poolConfig.setTestOnReturn(TEST_ON_RETURN);
        poolConfig.setTestOnBorrow(TEST_ON_BORROW);
        LOGGER.info("redis连接池 初始化");
    	if (hasCluster) {
            String[] clusterAddr = ADDR_ARRAY.split(";");
            Set<HostAndPort> nodes = new LinkedHashSet<>();
            for(String addr : clusterAddr){
                nodes.add(new HostAndPort(addr.split(":")[0], Integer.parseInt(addr.split(":")[1])));
            }
            if (StringUtils.isBlank(AUTH)) {
            	this.cluster = new JedisCluster(nodes, TIMEOUT, poolConfig);
            } else {
            	this.cluster = new JedisCluster(nodes, TIMEOUT, TIMEOUT, 3, AUTH, poolConfig);
            }
    	} else {
    		// 如果密码有配置
            if (StringUtils.isNotBlank(AUTH)) {
                // 如果密码不为空
                jedisPool = new JedisPool(poolConfig, ADDR_ARRAY.split(";")[0].split(":")[0], Integer.parseInt(ADDR_ARRAY.split(";")[0].split(":")[1]), TIMEOUT, AUTH);
            } else {
                jedisPool = new JedisPool(poolConfig, ADDR_ARRAY.split(";")[0].split(":")[0], Integer.parseInt(ADDR_ARRAY.split(";")[0].split(":")[1]), TIMEOUT);
            }
    	}
    }
    
    public static RedisUtil getInstance(Configuration conf){
    	if (redis == null) {
    		synchronized (RedisUtil.class) {
    			if (redis == null) {
    				redis = new RedisUtil(conf);
    			}
			}
    	}
    	return redis;
    }
    
    public String getADDR_ARRAY() {
        return ADDR_ARRAY;
    }

    public void setADDR_ARRAY(String ADDR_ARRAY) {
        this.ADDR_ARRAY = ADDR_ARRAY;
    }

    public String getAUTH() {
        return AUTH;
    }

    public void setAUTH(String AUTH) {
        this.AUTH = AUTH;
    }

    public int getMAX_ACTIVE() {
        return MAX_ACTIVE;
    }

    public void setMAX_ACTIVE(int MAX_ACTIVE) {
        this.MAX_ACTIVE = MAX_ACTIVE;
    }

    public int getMAX_IDLE() {
        return MAX_IDLE;
    }

    public void setMAX_IDLE(int MAX_IDLE) {
        this.MAX_IDLE = MAX_IDLE;
    }

    public int getMAX_WAIT() {
        return MAX_WAIT;
    }

    public void setMAX_WAIT(int MAX_WAIT) {
        this.MAX_WAIT = MAX_WAIT;
    }

    public int getTIMEOUT() {
        return TIMEOUT;
    }

    public void setTIMEOUT(int TIMEOUT) {
        this.TIMEOUT = TIMEOUT;
    }

    public boolean isTEST_ON_BORROW() {
        return TEST_ON_BORROW;
    }

    public void setTEST_ON_BORROW(boolean TEST_ON_BORROW) {
        this.TEST_ON_BORROW = TEST_ON_BORROW;
    }

    public boolean isTEST_ON_RETURN() {
        return TEST_ON_RETURN;
    }

    public void setTEST_ON_RETURN(boolean TEST_ON_RETURN) {
        this.TEST_ON_RETURN = TEST_ON_RETURN;
    }

    public boolean isCluster() {
        return hasCluster;
    }

    public void setCluster(boolean cluster) {
    	hasCluster = cluster;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisCluster getCluster() {
        return cluster;
    }

    public void setCluster(JedisCluster cluster) {
        this.cluster = cluster;
    }
	
}
