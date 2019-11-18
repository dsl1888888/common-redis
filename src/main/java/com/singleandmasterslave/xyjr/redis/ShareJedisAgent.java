package com.singleandmasterslave.xyjr.redis;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ShareJedisAgent
{
    private static Logger log = LoggerFactory.getLogger(ShareJedisAgent.class);
    private MSJedisPool mSJedisPool;
    private Random random = new Random();

    public ShareJedisAgent()
    {
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
            log.info("redis关闭");
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
            log.info("redis关闭");
        }
    }

    //	  public String set(final String key, final String value)
    //	  {
    //	    (String)masterExecute(new CmdProxy()
    //	    {
    //	      public Object execute(Jedis jedis)
    //	      {
    //	        return jedis.set(key, value);
    //	      }
    //	    });
    //	  }

    //	  public String get(final String key)
    //	  {
    //	    (String)slaveExecute(new CmdProxy()
    //	    {
    //	      public Object execute(Jedis jedis)
    //	      {
    //	        return jedis.get(key);
    //	      }
    //	    });
    //	  }

    //	  public Long del(final String key)
    //	  {
    //	    (Long)masterExecute(new CmdProxy()
    //	    {
    //	      public Object execute(Jedis jedis)
    //	      {
    //	        return jedis.del(key);
    //	      }
    //	    });
    //	  }

    //	  public Boolean exists(final String key)
    //	  {
    //	    (Boolean)slaveExecute(new CmdProxy()
    //	    {
    //	      public Object execute(Jedis jedis)
    //	      {
    //	        return jedis.exists(key);
    //	      }
    //	    });
    //	  }

    //	  public Long expire(final String key, final int seconds)
    //	  {
    //	    (Long)masterExecute(new CmdProxy()
    //	    {
    //	      public Object execute(Jedis jedis)
    //	      {
    //	        return jedis.expire(key, seconds);
    //	      }
    //	    });
    //	  }

    private static abstract interface CmdProxy
    {
        public abstract Object execute(Jedis paramJedis);
    }

}
