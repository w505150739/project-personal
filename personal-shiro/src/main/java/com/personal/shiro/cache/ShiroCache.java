package com.personal.shiro.cache;

import com.personal.common.util.LogUtil;
import com.personal.common.util.SerializeUtil;
import com.personal.shiro.ShiroRedisManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Description: 实现Shiro的Cache接口（Shiro缓存增、删、改、查，Shiro本身没有实现，只是定义了接口）
 * @author tanqiang
 * @date 2016-07-08 14:45
 * 
 * @param <K>
 * @param <V>
 */
public class ShiroCache<K, V> implements Cache<K, V> {

	private LogUtil logger = LogUtil.getLogger(ShiroCache.class);

	@Autowired
	private ShiroRedisManager cache;

	/**
	 * 通过一个JedisManager实例构造RedisCache
	 * 
	 * @param cache jedis实例
	 */
	public ShiroCache(ShiroRedisManager cache) {
		if (cache == null) {
			throw new IllegalArgumentException("Cache argument cannot be null.");
		}
		this.cache = cache;
	}

	/**
	 * 获得byte[]型的key
	 * 
	 * @param key
	 * @return
	 */
	private byte[] getByteKey(Object key) {
		if (key instanceof String) {
			String preKey = cache.getKeyPrefix() + key;
			return preKey.getBytes();
		} else {
			return SerializeUtil.serialize(key);
		}
	}

	/**
	 * 根据Key获取缓存中的值
	 */
	@Override
	public V get(K key) throws CacheException {
		if(logger.isDebugEnabled()){
			logger.debug("Get by Key [" + key + "]");
		}
		if (key == null) {
			return null;
		}
		
		try {
			byte[] rawValue = cache.get(getByteKey(key));
			@SuppressWarnings("unchecked")
			V value = (V) SerializeUtil.deserialize(rawValue);
			return value;
		} catch (Throwable t) {
//			throw new CacheException(t);
			return null;
		}

	}

	/**
	 * 往缓存中放入key-value，返回缓存中之前的值
	 */
	@Override
	public V put(K key, V value) throws CacheException {
		if(logger.isDebugEnabled()){
			logger.debug("Put key [" + key + "]" +"and value");
		}
		try {
			cache.set(getByteKey(key), SerializeUtil.serialize(value));
			return value;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	/**
	 * 往缓存中放入key-value，并设置过期时间，返回缓存中之前的值
	 */
	public V putexpire(K key, V value, Integer seconds) {
		if(logger.isDebugEnabled()){
			logger.debug("Put k-v with expire [" + key + "]");
		}
		try {
			cache.set(getByteKey(key), SerializeUtil.serialize(value), seconds);
			return value;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	/**
	 * 移除缓存中key对应的值，返回该值
	 */
	@Override
	public V remove(K key) throws CacheException {
		if(logger.isDebugEnabled()){
			logger.debug("remove key [" + key + "]");
		}
		try {
			V previous = get(key);
			cache.del(getByteKey(key));
			return previous;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	/**
	 * 清空整个缓存
	 */
	@Override
	public void clear() throws CacheException {
		if(logger.isDebugEnabled()){
			logger.debug("clear all");
		}
		try {
			cache.flushDB();
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	/**
	 * 返回缓存大小
	 */
	@Override
	public int size() {
		try {
			Long longSize = new Long(cache.dbSize());
			return longSize.intValue();
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	/**
	 * 获取缓存中所有的key
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		try {
			Set<byte[]> keys = cache.keys(cache.getKeyPrefix() + "*");
			if (CollectionUtils.isEmpty(keys)) {
				return Collections.emptySet();
			} else {
				Set<K> newKeys = new HashSet<K>();
				for (byte[] key : keys) {
					newKeys.add((K) key);
				}
				return newKeys;
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	/**
	 * 获取缓存中所有的value
	 */
	@Override
	public Collection<V> values() {
		try {
			Set<byte[]> keys = cache.keys(cache.getKeyPrefix() + "*");
			if (!CollectionUtils.isEmpty(keys)) {
				List<V> values = new ArrayList<V>(keys.size());
				for (byte[] key : keys) {
					@SuppressWarnings("unchecked")
					V value = get((K) key);
					if (value != null) {
						values.add(value);
					}
				}
				return Collections.unmodifiableList(values);
			} else {
				return Collections.emptyList();
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

}
