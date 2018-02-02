package com.personal.shiro;

import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.Set;

/**
 * @author liuyuzhu
 * @description: jedis 工具类 操作redis
 * @date 2018/1/20 1:46
 */
@Setter
@Getter
public class ShiroRedisManager {
    /**
     * redis服务器地址
     */
    private String host;

    /**
     * redis服务端口
     */
    private int port;

    /**
     * redis服务器密码
     */
    private String password;

    /**
     * 过期时间（单位：秒）
     */
    private int expire;

    /**
     * 超时多久jedis重新连接redis服务（单位：毫秒）
     */
    private int timeout = 0;

    /**
     * redis数据库索引号（redis默认有16个数据库，索引号为0-15）
     */
    private int database;

    /**
     * redis缓存前缀
     */
    private String keyPrefix;

    /**
     * jedis连接池
     */
    private static JedisPool jedisPool = null;

    public ShiroRedisManager() {
    }

    /**
     * 初始化方法
     */
    public void init() {
        if (jedisPool == null) {
            if (password != null && !"".equals(password)) {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password, database);
            } else if (timeout != 0) {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, null, database);
            } else {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT, null, database,null);
            }

        }
    }

    /**
     * 根据Key得到Value
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            close(jedis);
        }
        return value;
    }

    /***
     * 根据Key得到Value
     *
     * @param key
     * @return
     */
    public String get(String key) {
        String result = null;
        Jedis jedis = jedisPool.getResource();
        try {
            result = jedis.get(key);
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * 设置key-value
     *
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        }finally {
            close(jedis);
        }
        return value;
    }

    /**
     * 设置key-value
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } finally {
            close(jedis);
        }
        return value;
    }

    /**
     * 设置key-value，并设置其过期时间expire
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            close(jedis);
        }
        return value;
    }

    /**
     * 设置key-value，并设置其过期时间expire
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String set(String key, String value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            close(jedis);
        }
        return value;
    }

    /**
     * 删除指定键对应的值
     *
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 删除指定键对应的值
     *
     * @param key
     */
    public void del(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally {
            close(jedis);
        }
    }

    /**
     * 删除当前选择的DB所有的键
     */
    public void flushDB() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.flushDB();
        } finally {
            close(jedis);
        }
    }

    /**
     * 获取当前选择的DB的键的数目。
     */
    public Long dbSize() {
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = jedis.dbSize();
        } finally {
            close(jedis);
        }
        return dbSize;
    }

    /**
     * 获取匹配指定通配符字符串的键
     *
     * @param pattern
     * @return
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern.getBytes());
        } finally {
            close(jedis);
        }
        return keys;
    }

    /**
     * 向集合中添加缓存信息
     *
     * @param setName
     * @param elementValue
     */
    public Long sadd(String setName, String elementValue) {
        Long addCount = 0l;
        Jedis jedis = jedisPool.getResource();
        try {
            addCount = jedis.sadd(setName, elementValue);
        } finally {
            close(jedis);
        }
        return addCount;
    }

    /**
     * 向集合中移除缓存信息
     *
     * @param setName
     * @param elementValue
     */
    public Long srem(String setName, String elementValue) {
        Long remCount = 0l;
        Jedis jedis = jedisPool.getResource();
        try {
            remCount = jedis.srem(setName, elementValue);
        } finally {
            close(jedis);
        }
        return remCount;
    }

    /**
     * 获取集合中的所有元素
     *
     * @param setName
     * @return
     */
    public Set<String> smembers(String setName) {
        Set<String> elements = null;
        Jedis jedis = jedisPool.getResource();
        try {
            elements = jedis.smembers(setName);
        } finally {
            close(jedis);
        }
        return elements;
    }

    private void close(Jedis jedis) {
        if(null != jedis){
            jedis.close();
        }

    }
}
