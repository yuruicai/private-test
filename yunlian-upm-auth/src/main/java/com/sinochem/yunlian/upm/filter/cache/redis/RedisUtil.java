package com.sinochem.yunlian.upm.filter.cache.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private int maxIdle;

    private int maxTotal;

    private int maxWaitMillis;

    private String masterName;

    private String password;

    private String serverHosts;



    /**
     * 获取JedisCluster对象
     */

    public synchronized JedisCluster getJedisCluster(){
        String[] serverArray = serverHosts.split(",");
        Set<HostAndPort> nodes = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        JedisCluster jedisCluster = new JedisCluster(nodes, 1000, 1000, 1, password, new GenericObjectPoolConfig());
        return jedisCluster;
    }

//    /**
//     * return jedis Resource
//     *
//     * @param jedis
//     */
//    public void returnResource(final JedisCluster jedis) {
//        if (jedis != null) {
//            try {
//                jedis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


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

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }


    public String getServerHosts() {
        return serverHosts;
    }

    public void setServerHosts(String serverHosts) {
        this.serverHosts = serverHosts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}