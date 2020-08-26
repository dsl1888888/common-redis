package com.singleandcluster.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import lombok.extern.slf4j.Slf4j;

/**
 * redis cache
 * 
 */
@Slf4j
public final class StringRedisUtil
{

    private StringRedisTemplate  redisTemplate;

    public void deleteAll(final String key)
    {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除对应的value
     * 
     * @param keys
     */
    public void remove(final String... keys)
    {
        for (String key : keys)
        {
            remove(key);
        }
    }

    /**
     * 批量删除key
     * 
     * @param pattern
     */
    public void removePattern(final String pattern)
    {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     * 
     * @param key
     */
    public void remove(final String key)
    {
        if (exists(key))
        {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * 
     * @param key
     * @return
     */
    public boolean exists(final String key)
    {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * 
     * @param key
     * @return
     */
    public Object get(final String key)
    {
        //		Object result = null;
        //		ValueOperations<String, Object> operations = redisTemplate
        //				.opsForValue();
        //		result = operations.get(key);
        //		return result;
        Object result = null;
        result = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
//                    ValueOperations<String, Object> stringRedis = operations.opsForValue();
                    ValueOperations opsForValue = operations.opsForValue();
                    return opsForValue.get(key);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return null;

                }
            }
        });
        return result;
    }

    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, final Object value)
    {
        //		boolean result = false;
        //		try {
        //			ValueOperations<String, Object> operations = redisTemplate
        //					.opsForValue();
        //			operations.set(key, value);
        //			result = true;
        //		} catch (Exception e) {
        //			e.printStackTrace();
        //		}
        //		return result;
        Object result = 0;
        result = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ValueOperations<String, Object> stringRedis = operations.opsForValue();
                    stringRedis.set(key, value);
                    return 1;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return 0;

                }
            }
        });
        if (1 == (Integer) result)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * 2020/8/11 星期二
     * @param key
     * @param incrementvalue
     * @return
     */
    public boolean increment(final String key, final long incrementvalue)
    {
        //		boolean result = false;
        //		try {
        //			ValueOperations<String, Object> operations = redisTemplate
        //					.opsForValue();
        //			operations.set(key, value);
        //			result = true;
        //		} catch (Exception e) {
        //			e.printStackTrace();
        //		}
        //		return result;
        Object result = 0;
        result = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ValueOperations<String, Object> stringRedis = operations.opsForValue();
                    
                    stringRedis.increment(key, incrementvalue);
                    return 1;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return 0;

                }
            }
        });
        if (1 == (Integer) result)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    
    /**
     *   
     * 
     * @param key,
     * @param value
     * @return
     */
	public boolean setExpire(final String key, final Long expireTime)
    {
        Object result = 0;
        result = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
					redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
					return 1;

				} catch (Exception e) {
					e.printStackTrace();
					return 0;

				}
			}
		});
		if (1 == (Integer) result) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 写入缓存,有效时间单位（秒）
	 * 
	 * @param       key,
	 * @param value
	 * @return
	 */
	public boolean setBySECONDS(final String key, final Object value, final Long expireTime) {
		Object result = 0;
		result = redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(RedisOperations operations) {
				try {
					ValueOperations<String, Object> stringRedis = operations.opsForValue();
                    stringRedis.set(key, value);
                    redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
                    return 1;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return 0;

                }
            }
        });
        if (1 == (Integer) result)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

 

}