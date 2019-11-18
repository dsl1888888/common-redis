package com.singleandmasterslave.xyjr.redis;

import java.util.List;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MSJedisPool
{

    public JedisPool getMasterPool()
    {
        return masterPool;
    }

    public void setMasterPool(JedisPool masterPool)
    {
        this.masterPool = masterPool;
    }

    public JedisPool[] getSlavePool()
    {
        return slavePool;
    }

    public void setSlavePool(JedisPool[] slavePool)
    {
        this.slavePool = slavePool;
    }

    private static MSJedisPool mSJedisPool;

    private MSJedisPool()
    {

    }

    private JedisPool masterPool;
    private JedisPool[] slavePool;

    private MSJedisPool(JedisPoolConfig jedisPoolConfig, String masterIp, String masterPort, List<String> slaveIpList,
            List<String> slavePortList)
    {

        masterPool = new JedisPool(jedisPoolConfig, masterIp, Integer.parseInt(masterPort));
        int len = slaveIpList.size();
        slavePool = new JedisPool[len];
        for (int i = 0; i < len; i++)
        {
            slavePool[i] = new JedisPool(jedisPoolConfig, slaveIpList.get(i), Integer.parseInt(slavePortList.get(i)));
        }
    }

    public static MSJedisPool getInstance(JedisPoolConfig jedisPoolConfig, String masterIp, String masterPort,
            List<String> slaveIpList, List<String> slavePortList)
    {

        if (null == mSJedisPool)
        {
            synchronized (MSJedisPool.class)
            {
                mSJedisPool = new MSJedisPool(jedisPoolConfig, masterIp, masterPort, slaveIpList, slavePortList);
            }

        }

        return mSJedisPool;

    }

}
