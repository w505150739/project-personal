package com.personal.shiro.cache;

import com.personal.common.util.LogUtil;
import com.personal.shiro.ShiroRedisManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description: 实现Shiro的CacheManager接口，负责所有缓存的主要管理组件，它返回Cache实例
 * @author tanqiang
 * @date 2016-07-08 14:45
 */
public class ShiroCacheManager implements CacheManager {

	private static final LogUtil logger = LogUtil.getLogger(ShiroCacheManager.class);

	@SuppressWarnings("rawtypes")
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	@Autowired
	private ShiroRedisManager redisManager;

	public ShiroRedisManager getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(ShiroRedisManager redisManager) {
		this.redisManager = redisManager;
	}

	/**
	 * 根据缓存名字获取一个Cache
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		if(logger.isDebugEnabled()){
			logger.debug("获取名称为: " + name + " 的RedisCache实例");
		}
		Cache c = caches.get(name);
		if (c == null) {
			//初始化jedis实例
			redisManager.init();
			//创建一个RedisCache实例
			c = new ShiroCache<K, V>(redisManager);
			//保存到缓存中
			caches.put(name, c);
		}
		return c;
	}

}
