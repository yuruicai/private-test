package com.sinochem.yunlian.upm.sso.cache;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private int maxIdle;

    private int maxTotal;

    private int maxWaitMillis;

    private String serverHosts;

    private String masterName;

    private String password;

    private static JedisSentinelPool sentinelPool;




    @PostConstruct
    public void init() {
        try {
            sentinelPool = initPool();
            logger.info("----------------init pool end.-------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JedisSentinelPool initPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(getMaxIdle());
        jedisPoolConfig.setMaxTotal(getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(getMaxWaitMillis());

        Set<String> sentinels = new HashSet<String>();

        String hosts[] = StringUtils.splitByWholeSeparator(getServerHosts(), ",");

        for (int i = 0; i < hosts.length; i++) {
            String[] host_port = StringUtils.splitByWholeSeparator(StringUtils.trim(hosts[i]), ":");
            sentinels.add(new HostAndPort(StringUtils.trim(host_port[0]), Integer.parseInt(StringUtils.trim(host_port[1]))).toString());
        }

        logger.info("init pool sentinels={}", sentinels);
        return new JedisSentinelPool(getMasterName(), sentinels, jedisPoolConfig,5000,password);
    }

    /**
     * get Jedis instance
     *
     * @return
     */
    public synchronized Jedis getJedis() {
        logger.info("------ get jedis begin -------");
        try {
            if (sentinelPool != null) {
                logger.info("------ sentinelPool is not null,return instance. -------");
                return sentinelPool.getResource();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return jedis Resource
     *
     * @param jedis
     */
    public void returnResource(final Jedis jedis) {
        if (jedis != null) {
            sentinelPool.returnResource(jedis);
        }
    }

    public void returnBrokenResource(Jedis resource) {
        if (resource != null) {
            sentinelPool.returnBrokenResource(resource);
        }

    }



    public void destroy() {
        if (sentinelPool != null) {
            sentinelPool.destroy();
        }
    }

    public Long hincrBy(String key, String field, long value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Long num = jedis.hincrBy(key, field, value);
            returnResource(jedis);
            return num;
        } catch (Exception e) {
//            log.error("hincrementBy failed.", e);
            returnResource(jedis);
        }
        return null;
    }

    /**
     * 是否有锁权限
     *
     * @param key
     * @param expireSeconds key的过期时间
     * @param timeoutMsecs  获取锁的超时时间
     * @return
     */
    public boolean tryLock(String key, Integer expireSeconds, long timeoutMsecs) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null) {
                while (timeoutMsecs >= 0) {
                    logger.debug("try lock key: " + key);
                    Long i = jedis.setnx(key, key);
                    if (i == 1) {
                        jedis.expire(key, expireSeconds);
                        logger.debug("get lock, key: " + key + " , expire in " + expireSeconds + " seconds.");
                        return true;
                    } else { // 存在锁
                        logger.debug("key: " + key + " locked by another business：" + key);
                    }
                    timeoutMsecs -= 100;
                    Thread.sleep(100);
                }
                return false;
            } else {
                logger.error("try lock jedis is null");
                return false;
            }
        } catch (Exception e) {
            logger.error("key={}; expireSeconds={}, timeoutMsecs={}", key, expireSeconds, timeoutMsecs, e);
        } finally {
            returnResource(jedis);
        }
        return false;
    }

    public boolean unLock(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null) {
                jedis.del(key);
                logger.debug("del key={};", key);
                return true;
            } else {
                logger.error("del key jedis is null");
            }
            return false;
        } catch (Exception e) {
            logger.error("del key={};", key, e);
        } finally {
            returnResource(jedis);
        }
        return false;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}