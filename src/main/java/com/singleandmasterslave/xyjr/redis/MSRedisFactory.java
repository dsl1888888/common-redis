package com.singleandmasterslave.xyjr.redis;

import org.apache.log4j.Logger;

import com.singleandmasterslave.xyjr.redis.constants.JedisPoolConfigBean;

import redis.clients.jedis.JedisPoolConfig;


/**
 * @ClassName: MSRedisFactory
 * @Description: redis 工厂
 * @author Yun.Lin
 * @date 2017年5月31日 下午3:04:17
 * 
 */
public class MSRedisFactory
{

    public static Logger log = Logger.getLogger(MSRedisFactory.class);

    private static JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    public static MSRedis produce(JedisPoolConfigBean jedisMaster, JedisPoolConfigBean jedisSlave)
    {
        if (jedisMaster == null || jedisMaster.getRedisHostIP() == null)
        {
            throw new NullPointerException("master jedis can not be null" + jedisMaster);
        }
        //设置最大空闲数
        jedisPoolConfig.setMaxIdle(jedisMaster.getMaxIdle());
        //设置最大连接数
        jedisPoolConfig.setMaxTotal(jedisMaster.getMaxTotal());
        //设置超时时间
        jedisPoolConfig.setMaxWaitMillis(jedisMaster.getMaxWaitMillis());
        //设置返回对象是否是有效
        jedisPoolConfig.setTestOnReturn(jedisMaster.isTestOnBorrow());

        jedisPoolConfig.setLifo(true);

        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(1000);

        MSJedisPool msJedisPool = MSJedisPool.getInstance(jedisPoolConfig, jedisMaster.getRedisHostIP().get(0),
                jedisMaster.getRedisHostport().get(0), jedisSlave.getRedisHostIP(), jedisSlave.getRedisHostport());
        return new MSRedis(msJedisPool);

    }
}
