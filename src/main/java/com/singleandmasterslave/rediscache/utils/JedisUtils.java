package com.singleandmasterslave.rediscache.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Jedis工具类
 * @author marver
 * @version 1.0
 */
@Component
@Slf4j
public class JedisUtils{

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static JedisUtils jedisUtils;

	@PostConstruct
	public void init() {
		jedisUtils = this;
		jedisUtils.redisTemplate = this.redisTemplate;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Object get(String key) {
		Object value = null;
		try {
			if (jedisUtils.redisTemplate.hasKey(key)) {
				value = jedisUtils.redisTemplate.opsForValue().get(key);
				log.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			log.warn("get {} = {}", key, value, e);
		}
		return value;
	}
	

	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static boolean set(String key, String value, long cacheSeconds) {
		try {
			if (cacheSeconds != 0) {
				 jedisUtils.redisTemplate.opsForValue().set(key, value, cacheSeconds, TimeUnit.SECONDS);
			}else{
				jedisUtils.redisTemplate.opsForValue().set(key, value);
			}
			log.debug("set {} = {}", key, value);
			return true;
		} catch (Exception e) {
			log.warn("set {} = {}", key, value, e);
		}
		return false;
	}

	/**
	 * 设置hash缓存
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static boolean hmset(String key, Map<Object, Object> value) {
		try {
			jedisUtils.redisTemplate.opsForHash().putAll(key, value);
			log.debug("hmset {} = {}", key, value);
			return true;
		} catch (Exception e) {
			log.warn("hmset {} = {}", key, value, e);
		}
		return false;
	}

	/**
	 * 存放单个hash缓存
	 * @param key 键
	 * @param hkey 键
	 * @param value 值
	 * @return
	 */
	public static boolean hput(String key, String hkey, Object value) {
		try {
			jedisUtils.redisTemplate.opsForHash().put(key, hkey, value);
			log.debug("hput {} = {}", key+hkey, value);
			return true;
		} catch (Exception e) {
			log.warn("hput {} = {}", key+hkey, value, e);
		}
		return false;
	}

	/**
	 * 获取hash 所有的值
	 * @param key 键
	 * @return 值
	 */
	public static Map<Object, Object> hgetAll(String key) {
		Map<Object, Object> value = null;
		try {
			value = jedisUtils.redisTemplate.opsForHash().entries(key);
			log.debug("hgetAll {} = {}", key, value);
		} catch (Exception e) {
			log.warn("hgetAll {} = {}", key, value, e);
		}
		return value;
	}


	/**
	 * 获取hash 指定key值
	 * @param key 键
	 * @param hkey hash中的键
	 * @return 值
	 */
	public static Object hget(String key, String hkey) {
		Object value = null;
		try {
			value = jedisUtils.redisTemplate.opsForHash().get(key, hkey);
			log.debug("hget {} = {}", key, value);
		} catch (Exception e) {
			log.warn("hget {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * 获取hash 多个key值
	 * @param key 键
	 * @param hkeys hash中的键
	 * @return 值
	 */
	public static Object hkeysGet(String key, List<Object> hkeys) {
		Object value = null;
		try {
			value = jedisUtils.redisTemplate.opsForHash().multiGet(key, hkeys);
			log.debug("hkeysGet {} = {}", key, value);
		} catch (Exception e) {
			log.warn("hkeysGet {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * 设置set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static boolean setSet(String key, String value, long cacheSeconds) {
		try {
			jedisUtils.redisTemplate.opsForSet().add(key, value);
			if (cacheSeconds != 0) {
				jedisUtils.redisTemplate.expire(key, cacheSeconds, TimeUnit.SECONDS);
			}
			log.debug("setSet {} = {}", key, value);
			return true;
		} catch (Exception e) {
			log.warn("setSet {} = {}", key, value, e);
		}
		return false;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		try {
			if (jedisUtils.redisTemplate.hasKey(key)) {
				value = jedisUtils.redisTemplate.opsForSet().members(key);
				log.debug("getSet {} = {}", key, value);
			}
		} catch (Exception e) {
			log.warn("getSet {} = {}", key, value, e);
		}
		return value;
	}

	/**
	 * 获取set中 随机数量的值
	 * @param key
	 * @param value
	 * @return
	 */
	public static long removeSet(String key, String... value) {
		long num = 0;
		try {
			num = jedisUtils.redisTemplate.opsForSet().remove(key, value);
			log.debug("removeSet {}", key);
		} catch (Exception e) {
			log.warn("removeSet {}", key, e);
		}
		return num;
	}

	/**
	 * 获取set中 随机数量的值
	 * @param key
	 * @param num
	 * @return
	 */
	public static Set<String> getRandomMembers(String key, int num) {
		Set<String> result = null;
		try {
			result = jedisUtils.redisTemplate.opsForSet().distinctRandomMembers(key, num);
			log.debug("getRandomMembers {}", key);
		} catch (Exception e) {
			log.warn("getRandomMembers {}", key, e);
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static boolean del(String key) {
		boolean result = false;
		try {
			if (jedisUtils.redisTemplate.hasKey(key)){
				result = jedisUtils.redisTemplate.delete(key);
				log.debug("del {}", key);
			}else{
				log.debug("del {} not exists", key);
			}
		} catch (Exception e) {
			log.warn("del {}", key, e);
		}
		return result;
	}

	/**
	 * 模糊删除缓存
	 * @param keyPattern 键
	 * @return
	 */
	public static boolean delByPattern(String keyPattern) {
		boolean result = false;
		try {
			Set<String> set = jedisUtils.redisTemplate.keys(keyPattern);
			for(String key : set){
				result = jedisUtils.redisTemplate.delete(key);
				log.debug("delByPattern {}", key);
			}
		} catch (Exception e) {
			log.warn("delByPattern {}", keyPattern, e);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		try {
			result = jedisUtils.redisTemplate.hasKey(key);
			log.debug("exists {}", key);
		} catch (Exception e) {
			log.warn("exists {}", key, e);
		}
		return result;
	}

	/**
	 * 获取缓存 模糊匹配key
	 * @param keyPattern 模糊键
	 * @return
	 */
	public static Set<String> getByPattern(String keyPattern) {
		Set<String> result = null;
		try {
			result = jedisUtils.redisTemplate.opsForValue().getOperations().keys(keyPattern);
			log.debug("getByPattern {}", keyPattern);
		} catch (Exception e) {
			log.warn("getByPattern {}", keyPattern, e);
		}
		return result;
	}

	/**
	 * 分页存取数据
	 * @param key  hash存取的key
	 * @param hkey hash存取的hkey
	 * @param score 按value指定字段排序
	 * @param value
	 * @return
	 */
	public static boolean setPage(String key, String hkey, double score, String value){
		boolean result = false;
		try {
			jedisUtils.redisTemplate.opsForZSet().add(key+":page", hkey, score);
			result = hput(key, hkey, value);
			log.debug("setPage {}", key);
		} catch (Exception e) {
			log.warn("setPage {}", key, e);
		}
		return result;
	}

	/**
	 * 分页取出 hash中hkey值
	 * @param key
	 * @param offset
	 * @param count
	 * @return
	 */
	public static Set<String> getPage(String key, int offset, int count){
		Set<String> result = null;
		try {
			result = jedisUtils.redisTemplate.opsForZSet().rangeByScore(key+":page", 1, 100000, (offset-1)*count, count);
			log.debug("getPage {}", key);
		} catch (Exception e) {
			log.warn("getPage {}", key, e);
		}
		return result;
	}

	/**
	 * 计算key值对应的数量
	 * @param key
	 * @return
	 */
	public static Integer getSize(String key){
		Integer num = 0;
		try {
			Long size = jedisUtils.redisTemplate.opsForZSet().zCard(key+":page");
			log.debug("getSize {}", key);
			return size.intValue();
		} catch (Exception e) {
			log.warn("getSize {}", key, e);
		}
		return num;
	}
}
