package com.sinochem.yunlian.upm.filter.cache.redis;
import com.sinochem.yunlian.upm.filter.cache.CacheException;
import com.sinochem.yunlian.upm.filter.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * 自定义授权缓存管理类
 * @author ThinkGem
 * @version 2014-7-20
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String cacheKeyName = null;

    private RedisUtil redisUtil;

    private int expireSeconds = 300;

    public RedisCache(RedisUtil redisUtil, String cacheKeyName,int expireSeconds) {
        this.cacheKeyName = cacheKeyName;
        this.redisUtil = redisUtil;
        this.expireSeconds = expireSeconds;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(K key) throws CacheException {
        if (key == null){
            return null;
        }

        V value = null;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            value = (V)RedisUtil.unserialize(jedis.get(RedisUtil.getBytesKey(cacheKeyName + key)));
            logger.debug("get {} {}", cacheKeyName, key);
        } catch (Exception e) {
            logger.error("get {} {}", cacheKeyName, key);
        } finally {
            redisUtil.returnResource(jedis);
        }

        return value;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (key == null){
            return null;
        }

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            jedis.set(RedisUtil.getBytesKey(cacheKeyName + key), RedisUtil.serialize(value));
            jedis.expire(RedisUtil.getBytesKey(cacheKeyName + key), expireSeconds);
            logger.debug("put {} {} = {}", new Object[]{cacheKeyName, key, value});
        } catch (Exception e) {
            logger.error("put {} {}", cacheKeyName, key);
        } finally {
            redisUtil.returnResource(jedis);
        }
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        V value = null;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            jedis.del(RedisUtil.getBytesKey(cacheKeyName + key));
            logger.debug("remove {} {}", cacheKeyName, key);
        } catch (Exception e) {
            logger.warn("remove {} {}", cacheKeyName, key);
        } finally {
            redisUtil.returnResource(jedis);
        }
        return value;
    }

    @Override
    public void clear() throws CacheException {
    }

    @Override
    public int size() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        return null;
    }
}