package com.singleandcluster.util;

import java.util.Set;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SortedSetRedisUtil
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
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.remove(tv, value);
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
                    ZSetOperations<String, Object> zSetOperations = operations.opsForZSet();
                     
                    return zSetOperations.size(tv);
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
                    ZSetOperations<String, Object> zSetOperations = operations.opsForZSet();
                    
                    return zSetOperations.range(tv, 0, size(tv));
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

    public Set<Object> pageCache(final String tv,   int page,  int rows)
    {

        Set<Object> result = (Set<Object>) redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ZSetOperations<String, Object> zSetOperations = operations.opsForZSet();
                    
                    int start=0;
                    int end=0;
                    
                    if (page<0  || rows<0 )
                    {
                        //默认 第一页            10条
                        //1 页
                        start=  (1-1)*10;   
                        end=start-1+10;
                    }else {
                        
                        //2 页
                        start=  (page-1)*rows;   
                        end=start-1+rows;
                        
//                      //1 页
//                        start=  (1-1)*2;   
//                        end=start-1+rows;
//                        
//                        //2 页
//                        start=  (2-1)*2;   
//                        end=start-1+rows;
//                        
//                        //3 页
//                        start=  (3-1)*2;   
//                        end=start-1+rows;
//                        
//                      //4 页
//                        start=  (4-1)*2;   
//                        end=start-1+rows;
                      
                         
                    }
                    Set<Object> set = zSetOperations.range(tv, start ,  end);
 
                    return set;
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
    
    /** for 排行榜*/
    public Set<Object> pageCachePaiHanBan(final String tv,   int page,  int rows)
    {

        Set<Object> result = (Set<Object>) redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ZSetOperations<String, Object> zSetOperations = operations.opsForZSet();
                    
                    int start=0;
                    int end=0;
                    
                    if (page<0  || rows<0 )
                    {
                        //默认 第一页            10条
                        //1 页
                        start=  (1-1)*10;   
                        end=start-1+10;
                    }else {
                        
                        //2 页
                        start=  (page-1)*rows;   
                        end=start-1+rows;
                        
//                      //1 页
//                        start=  (1-1)*2;   
//                        end=start-1+rows;
//                        
//                        //2 页
//                        start=  (2-1)*2;   
//                        end=start-1+rows;
//                        
//                        //3 页
//                        start=  (3-1)*2;   
//                        end=start-1+rows;
//                        
//                      //4 页
//                        start=  (4-1)*2;   
//                        end=start-1+rows;
                      
                         
                    }
                    Set<Object> set = zSetOperations.reverseRange(tv, start ,  end);
 
                    return set;
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
    public boolean add(final String key, final Object value,double score)
    {
        Object result = 0;
        result = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations)
            {
                try
                {
                    ZSetOperations<String, Object> zSetOperations = operations.opsForZSet();
                    zSetOperations.add(key, value, score);
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
