package com.singleandcluster.util;

import java.util.List;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListRedisUtil
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
    public void remove(String tv, long i, Object value)
    {
        ListOperations<String, String> listOper = redisTemplate.opsForList();
        listOper.remove(tv, i, value);
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
                    ListOperations<String, Object> listOper = operations.opsForList();
                    return listOper.size(tv);
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
    public List<Object> getall(final String tv)
    {

        List<Object> result = redisTemplate.execute(new SessionCallback<List<Object>>()
        {
            @Override
            public List<Object> execute(RedisOperations operations)
            {
                try
                {
                    ListOperations<String, Object> listOper = operations.opsForList();
                    return listOper.range(tv, 0, size(tv));
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
     * 出队
     * 
     * @param key
     * @return
     */
    public Object popCache(final String tv)
    {

        Object result = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ListOperations<String, Object> listOper = operations.opsForList();
                    return listOper.rightPop(tv);
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

    // lrange key start end // 从左边依次返回key的[start,end] 的所有值，注意返回结果包含两端的值。
    //
    // ltrim key start end //删除指定索引之外的所有元素，注意删除之后保留的元素包含两端的start和end索引值。
    /**
     * 出队
     * 
     * @param key
     * @return
     */
    public List<Object> popCacheNumleft(final String tv, int size)
    {

        List<Object> result = (List<Object>) redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ListOperations<String, Object> listOper = operations.opsForList();

                    List<Object> list = listOper.range(tv, size(tv) - size, size(tv));

                    listOper.trim(tv, size, size(tv));

                    return list;
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

    // lrange key start end // 从左边依次返回key的[start,end] 的所有值，注意返回结果包含两端的值。
    //
    // ltrim key start end //删除指定索引之外的所有元素，注意删除之后保留的元素包含两端的start和end索引值。
    /**
     * 出队
     * 
     * @param key
     * @return
     */
    public List<Object> popCacheNumRight(final String tv, int size)
    {

        List<Object> result = (List<Object>) redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ListOperations<String, Object> listOper = operations.opsForList();

                    List<Object> list = listOper.range(tv, 0, size(tv) - size);

                    listOper.trim(tv, 0, size(tv) - size - 1);

                    return list;
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
     * 入队
     * 
     * @param key
     * @param value
     * @return
     */
    public long pushCache(final String tv, final Object ob)
    {

        long result = 0;
        result = redisTemplate.execute(new SessionCallback<Long>()
        {
            @Override
            public Long execute(RedisOperations operations)
            {
                try
                {
                    ListOperations<String, Object> listOper = operations.opsForList();
                    return listOper.leftPush(tv, ob);
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

    public void setRedisTemplate(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

   
}
