package com.singleandcluster.util;
 
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashRedisUtil
{

    private StringRedisTemplate redisTemplate;

    public void deleteAll(String tv)
    {
        redisTemplate.delete(tv);

    }

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
    public void remove(String tv, final String key)
    {
        if (exists(tv, key))
        {
            HashOperations<String, Object, Object> hashOper = redisTemplate.opsForHash();
            hashOper.delete(tv, key);
        }
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
                    HashOperations<String, String, Object> hashOper = operations.opsForHash();
                    return hashOper.size(tv);
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
     * 判断缓存中是否有对应的value
     * 
     * @param key
     * @return
     */
    public boolean exists(final String tv, final String key)
    {

        Boolean o = redisTemplate.execute(new SessionCallback<Boolean>()
        {
            @Override
            public Boolean execute(RedisOperations operations)
            {
                try
                {
                    HashOperations<String, String, Object> hashOper = operations.opsForHash();
                    return hashOper.hasKey(tv, key);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;

                }
            }
        });
        return o;
    }

    /**
     * 获取所有keys
     * @param tv
     * @return
     */
    public Set<String> keys(final String tv)
    {

        Set<String> o = (Set<String>) redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Set<String> execute(RedisOperations operations) throws DataAccessException
            {
                HashOperations<String, String, Object> oper = operations.opsForHash();
                return oper.keys(tv);
            }
        });
        return o;
    }

    
    /**
     * 读取缓存
     * 
     * @param key
     * @return
     */
    public Object get(final String tv, final String key)
    {

        Object o = redisTemplate.execute(new SessionCallback<Object>()
        {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException
            {
                HashOperations<String, String, Object> oper = operations.opsForHash();
                return oper.get(tv, key);
            }
        });
        return o;
    }

    public Object getData(final String tv, final String keys)
    {
        Object o = redisTemplate.execute(new RedisCallback<Object>()
        {
            @Override
            public Object doInRedis(RedisConnection conn)
            {
                try
                {
                    HashOperations<String, Object, Object> hashOper = redisTemplate.opsForHash();
                    return hashOper.get(tv, keys);
                }
                catch (Exception ex)
                {
                    return null;
                }
                finally
                {
                    if (!conn.isClosed())
                    {
                        conn.close();
                        log.info("****getData RedisConn is close*****");
                    }
                }
            }
        });

        return o;
    }

    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean setData(final String tv, final String key, final Object value)
    {
        Boolean o = redisTemplate.execute(new RedisCallback<Boolean>()
        {
            @Override
            public Boolean doInRedis(RedisConnection conn) throws DataAccessException
            {
                try
                {
                    HashOperations<String, Object, Object> hashOper = redisTemplate.opsForHash();
                    hashOper.put(tv, key, value);
                    return true;
                }
                catch (Exception ex)
                {
                    return false;
                }
                finally
                {
                    if (!conn.isClosed())
                    {
                        conn.close();
                        log.info("****setData RedisConn is close*****");
                    }
                }
            }
        });
        return o;
    }

    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String tv, final String key, final Object value)
    {
        Boolean o = redisTemplate.execute(new SessionCallback<Boolean>()
        {
            @Override
            public Boolean execute(RedisOperations operations)
            {
                try
                {
                    HashOperations<String, String, Object> hashOper = operations.opsForHash();
                    hashOper.put(tv, key, value);
                    return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;

                }
            }
        });
        return o;
    }
    

    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean setSingleString(final String tv, final String key, final String value)
    {
        Boolean o = redisTemplate.execute(new SessionCallback<Boolean>()
        {
            @Override
            public Boolean execute(RedisOperations operations)
            {
                try
                {
                    HashOperations<String, String, Object> hashOper = operations.opsForHash();
                    hashOper.put(tv, key, value);
                    return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;

                }
            }
        });
        return o;
    }

    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean setAll(final String tv,  List<Map<String, Object>> hashList)
    {
        Boolean o = redisTemplate.execute(new SessionCallback<Boolean>()
        {
            @Override
            public Boolean execute(RedisOperations operations)
            {
                try
                {
                    HashOperations<String, String, Object> hashOper = operations.opsForHash();

                    for (Map<String, Object> map : hashList)
                    {
                        // byte[] key = serializer.serialize(tableName + ":" +
                        // map.get("hcode").toString());
                        // byte[] name =
                        // serializer.serialize(map.get("object"));

                        Iterator it = map.entrySet().iterator();
                        while (it.hasNext())
                        {
                            Map.Entry entry = (Map.Entry) it.next();
                            Object key = entry.getKey();
                            Object value = entry.getValue();

                            hashOper.put(tv, String.valueOf(key), String.valueOf(value));
                        }

                    }
                    return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;

                }
            }
        });
        return o;
    }
    

    /**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public List<Object> getall(String tv)
    {
        HashOperations<String, Object, Object> hashOper = redisTemplate.opsForHash();
        return hashOper.values(tv);
    }
    
    public Map entries(String tv)
    {
        HashOperations<String, Object, Object> hashOper = redisTemplate.opsForHash();
        
        return hashOper.entries(tv); 
    }

    /**
     * 批量写入缓存
     * 
     * @param key
     * @param value
     *            是一个 map 数据对象，key-value
     * 
     * @return
     */
    public boolean setDataByMap(final String tv, final Map<String, Object> value)
    {
        Boolean o = redisTemplate.execute(new SessionCallback<Boolean>()
        {
            @Override
            public Boolean execute(RedisOperations operations)
            {
                try
                {
                    HashOperations<String, String, Object> hashOper = operations.opsForHash();
                    hashOper.putAll(tv, value);
                    return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        return o;
    }

    /**
     * 批量向redis中插入H码:key(tableName:hcode) value(pcode) 如果键已存在则返回false,不更新,防止覆盖。使用pipeline批处理方式(不关注返回值)
     * 
     * @param list
     *            一个map代表一行记录,2个key:hcode & pcode。
     * @param tableName
     *            redis中key的值为tableName:hcode 对应value值为pcode。
     * @return
     */
    public boolean addHcode(final List<Map<String, Object>> list, final String tableName)
    {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>()
        {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException
            {
                RedisSerializer<Object> serializer = (RedisSerializer<Object>) redisTemplate.getDefaultSerializer();
                for (Map<String, Object> map : list)
                {
                    byte[] key = serializer.serialize(tableName + ":" + map.get("hcode").toString());
                    byte[] name = serializer.serialize(map.get("object"));
                    connection.setNX(key, name);
                }
                return true;
            }
        }, false, true);
        return result;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }
    
    
    
//    /**
//     * 批量向redis中插入H码:key(tableName:hcode) value(pcode) 如果键已存在则返回false,不更新,防止覆盖。使用pipeline批处理方式(不关注返回值)
//     * 
//     * @param list
//     *            一个map代表一行记录,2个key:hcode & pcode。
//     * @param tableName
//     *            redis中key的值为tableName:hcode 对应value值为pcode。
//     * @return
//     */
//    public boolean addHcodeForInterfaceSet(final List<Map<String, String>> list, final String tableName)
//    {
//        boolean result = redisTemplate.execute(new RedisCallback<Boolean>()
//        {
//            @Override
//            public Boolean doInRedis(RedisConnection connection) throws DataAccessException
//            {
//                RedisSerializer<Object> serializer = (RedisSerializer<Object>) redisTemplate.getDefaultSerializer();
//                for (Map<String, String> map : list)
//                {
//                    
////                    byte[] key = serializer.serialize(tableName + ":" + map.get("hcode").toString());
////                    byte[] name = serializer.serialize(map.get("object"));
//                    
//                    
//                    Iterator it = map.entrySet().iterator();
//                    while (it.hasNext()) {
//                     Map.Entry entry = (Map.Entry) it.next();
//                     Object key = entry.getKey();
//                     Object value = entry.getValue();
////                     System.out.println("key=" + key + " value=" + value);
//                     
//                     byte[] key1 = serializer.serialize(tableName + ":" +String.valueOf(key));
//                     byte[] name1 = serializer.serialize(String.valueOf(value));
//                     
//                     connection.setNX(key1, name1);
//                     
//                    }
//                    
//                    
//                   
//                }
//                
//               
//                return true;
//            }
//        }, false, true);
//        return result;
//    }
    
    
    
    
}
