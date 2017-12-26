/**
 * Copyright &copy; 2012-2014  All rights reserved.
 */
package com.sinochem.yunlian.upm.filter.cache.redis;

import com.sinochem.yunlian.upm.filter.cache.CacheException;
import com.sinochem.yunlian.upm.filter.cache.Cache;
import com.sinochem.yunlian.upm.filter.cache.CacheManager;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自定义授权缓存管理类
 * @author ThinkGem
 * @version 2014-7-20
 */
public class RedisCacheManager implements CacheManager {

	private ConcurrentMap<String,RedisCache> redisCacheMap = new ConcurrentHashMap<String, RedisCache>();

	private RedisUtil redisUtil;

	private int maxIdle;

	private int maxTotal;

	private int maxWaitMillis;

	private String serverHosts;

	private String masterName;

	private int expireSeconds = 300;

	@PostConstruct
	public void init(){
		redisUtil = new RedisUtil();
		redisUtil.setMasterName(getMasterName());
		redisUtil.setMaxIdle(getMaxIdle());
		redisUtil.setMaxTotal(getMaxTotal());
		redisUtil.setMaxWaitMillis(getMaxWaitMillis());
		redisUtil.setServerHosts(getServerHosts());

		redisUtil.init();
	}

	private String cacheKeyPrefix = "sinochem_yunlian_upm_auth_";

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		RedisCache<K,V> redisCache = redisCacheMap.get(name);
		if(redisCache != null){
			return redisCache;
		}
		redisCache = new RedisCache<K, V>(redisUtil, cacheKeyPrefix, expireSeconds);
		redisCacheMap.put(name, redisCache);
		return redisCache;
	}

	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}

	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public String getServerHosts() {
		return serverHosts;
	}

	public void setServerHosts(String serverHosts) {
		this.serverHosts = serverHosts;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public int getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}
}
