/**
 * 
 */
package com.singleandmasterslave.xyjr.redis.constants;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class JedisPoolConfigBean implements Serializable
{

    /**
     * 序列化值
     */
    private static final long serialVersionUID = 1L;

    //最大空闲连接数
    private int maxIdle;
    //最大连接数
    private int maxTotal;
    //设置最大阻塞时间，记住是毫秒数milliseconds
    private long maxWaitMillis;
    //在获取连接的时候检查有效性, 默认false
    private boolean testOnBorrow;
    //Redis主机IP
    private List<String> redisHostIP;
    //Redis主机Port
    private List<String> redisHostport;
    //超时时间
    private int timeout;

    public List<String> getRedisHostIP()
    {
        return redisHostIP;
    }

    public void setRedisHostIP(List<String> redisHostIP)
    {
        this.redisHostIP = redisHostIP;
    }

    public List<String> getRedisHostport()
    {
        return redisHostport;
    }

    public void setRedisHostport(List<String> redisHostport)
    {
        this.redisHostport = redisHostport;
    }

    public int getMaxIdle()
    {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle)
    {
        this.maxIdle = maxIdle;
    }

    public int getMaxTotal()
    {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal)
    {
        this.maxTotal = maxTotal;
    }

    public long getMaxWaitMillis()
    {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis)
    {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow()
    {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow)
    {
        this.testOnBorrow = testOnBorrow;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

}
