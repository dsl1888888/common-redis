package com.singleandcluster.util;

import java.util.Set;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetRedisUtil
{


    private StringRedisTemplate redisTemplate;

    /**
     * 批量删除对应的value
     * 
     * @param keys
     */
    public void remove(String tv, final String... keys)
    {
        for (String key : keys)
        {
            remove(tv, key);
        }

    }

    /**
     * 删除对应的value
     * 
     * @param key
     */
    public void remove(String tv, Object value)
    { 
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(tv, value);
    }

    /*
     * 返回模块数量
     */
    public Long size(final String tv)
    {

        long result = 0;
        result = redisTemplate.execute(new SessionCallback<Long>()
        {
            @Override
            public Long execute(RedisOperations operations)
            {
                try
                {
                    SetOperations<String, Object> setOperations = operations.opsForSet();
                     
                    return setOperations.size(tv);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return (long) 0;

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
    public Set<Object> getall(final String tv)
    {

        Set<Object> result = redisTemplate.execute(new SessionCallback<Set<Object>>()
        {
            @Override
            public Set<Object> execute(RedisOperations operations)
            { 
                try
                {
                    SetOperations<String, Object> setOperations = operations.opsForSet();
                    
                    return setOperations.members(tv);
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
    public boolean set(final String key, final Object value,double score)
    {
        Object result = 0;
        result = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    SetOperations<String, Object> setOperations = operations.opsForSet();
                    setOperations.add(key, value);
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
