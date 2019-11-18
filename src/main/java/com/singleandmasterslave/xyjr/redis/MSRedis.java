package com.singleandmasterslave.xyjr.redis;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.singleandmasterslave.xyjr.redis.util.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName: MSRedis
 * @Description:
 * @author Yun.Lin
 * @date 2017年5月31日 下午2:54:07
 * 
 */
public class MSRedis
{

    private static Logger log = LoggerFactory.getLogger(MSRedis.class);
    private MSJedisPool mSJedisPool;
    private Random random = new Random();

    public MSRedis()
    {
    }

    public MSRedis(MSJedisPool mSJedisPool)
    {
        this.mSJedisPool = mSJedisPool;
    }

    private Object masterExecute(CmdProxy cmd)
    {
        JedisPool pool = this.mSJedisPool.getMasterPool();
        Jedis jedis = pool.getResource();
        try
        {
            return cmd.execute(jedis);
        }
        catch (Exception e)
        {
            pool.returnBrokenResource(jedis);
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        finally
        {
            pool.returnResource(jedis);
            log.info("redis释放成功");
        }
    }

    private Object slaveExecute(CmdProxy cmd)
    {
        JedisPool[] pool = this.mSJedisPool.getSlavePool();
        int index = this.random.nextInt(pool.length);
        JedisPool slavePool = pool[index];
        Jedis jedis = slavePool.getResource();
        try
        {
            return cmd.execute(jedis);
        }
        catch (Exception e)
        {
            slavePool.returnBrokenResource(jedis);
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        finally
        {
            slavePool.returnResource(jedis);
            log.info("redis释放成功");
        }
    }

    public String set(final String key, final String value)
    {
        return (String) masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                return jedis.set(key, value);
            }
        });
    }

    public String setList(final String key, @SuppressWarnings("rawtypes") final List value)
    {
        return (String) masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                byte[] byteKey = SerializeUtil.serialize(key);
                byte[] byteValue = SerializeUtil.serialize(value);
                return jedis.set(byteKey, byteValue);
            }
        });
    }

    public String setObject(final String key, final Object value)
    {
        return (String) masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                byte[] byteKey = SerializeUtil.serialize(key);
                byte[] byteValue = SerializeUtil.serialize(value);
                return jedis.set(byteKey, byteValue);
            }
        });
    }

    public Object getObject(final String key)
    {
        return slaveExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                byte[] byteKey = SerializeUtil.serialize(key);
                byte[] bytesValue = jedis.get(byteKey);
                return SerializeUtil.unserialize(bytesValue);
            }
        });
    }

    public String get(final String key)
    {
        return (String) slaveExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                return jedis.get(key);
            }
        });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Object> getList(final String key)
    {
        return (List) slaveExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                byte[] byteKey = SerializeUtil.serialize(key);
                byte[] bytesValue = jedis.get(byteKey);
                return SerializeUtil.unserializeList(bytesValue);
            }
        });
    }

    public Long del(final String key)
    {
        return (Long) masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                long ret = jedis.del(key).longValue();
                if (ret <= 0L)
                {
                    return jedis.del(SerializeUtil.serialize(key));
                }
                return Long.valueOf(ret);
            }
        });
    }

    public Boolean exists(final String key)
    {
        return (Boolean) slaveExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                boolean flag = jedis.exists(key).booleanValue();
                return Boolean.valueOf(!flag ? jedis.exists(SerializeUtil.serialize(key)).booleanValue() : flag);
            }
        });
    }

    public Long expire(final String key, final int seconds)
    {
        return (Long) masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                long ret = jedis.expire(key, seconds).longValue();
                if (ret <= 0L)
                {
                    return jedis.expire(SerializeUtil.serialize(key), seconds);
                }
                return Long.valueOf(ret);
            }
        });
    }

    public Long incr(final String key)
    {
        return (Long) masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                long ret = jedis.incr(key).longValue();
                return Long.valueOf(ret);
            }
        });
    }

    public Long decr(final String key)
    {
        return (Long) masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                long ret = jedis.decr(key).longValue();
                return Long.valueOf(ret);
            }
        });
    }

    public Object setex(final String key, final int seconds, final String value)
    {
        return masterExecute(new CmdProxy()
        {
            @Override
            public Object execute(Jedis jedis)
            {
                return jedis.setex(key, seconds, value);
            }
        });
    }

    private static abstract interface CmdProxy
    {
        public abstract Object execute(Jedis paramJedis);
    }

}
