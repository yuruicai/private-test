package com.sinochem.yunlian.upm.sso.cache;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SsoCacheFacade {

    private static final Logger LOG = LoggerFactory.getLogger(SsoCacheFacade.class);
    private static Map<String, Object> cache = new HashMap<String, Object>();

    @Resource
    private RedisUtil redisUtil;

    public void set(String key, String value) {
        if (key == null) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                jedis.set(key, value);
            }
        }finally {
            redisUtil.returnResource(jedis);
        }
    }

    public void set(String key, String value, int timeout) {
        if (key == null) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                jedis.setex(key, timeout, value);
            }
        }finally {
            redisUtil.returnResource(jedis);
        }
    }

    public String get(String key) {
        if (key == null) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                return jedis.get(key);
            }
        }finally {
            redisUtil.returnResource(jedis);
        }
        return null;
    }

    public void delete(String key) {
        if (key == null) {
            return ;
        }
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                jedis.del(key);
            }
        }finally {
            redisUtil.returnResource(jedis);
        }
    }


    public void setObject(final String key, int expireSecond, Object value){
        if(value == null){
            return;
        }
        set(key, JSON.toJSONString(value), expireSecond);
    }

    public void setObject(String key, Object value) {
        if(value == null){
            return;
        }
        set(key, JSON.toJSONString(value));
    }

    public <V> V getObject(String key, Class<V> clazz) {
        String value = get(key);
        if(value == null){
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    public int ttl(String key){
        if (key == null) {
            return 0;
        }
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                return jedis.ttl(key).intValue();
            }
        }finally {
            redisUtil.returnResource(jedis);
        }

        return 0;
    }

    public Boolean exists(String key){
        if (key == null) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if(jedis != null){
                return jedis.exists(key);
            }
        }finally {
            redisUtil.returnResource(jedis);
        }

        return false;
    }

    public void multiDelete(List<String> keys){
        if (CollectionUtils.isEmpty(keys)) {
            return ;
        }
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String [] keyArr = new String[keys.size()];
            if(jedis != null){
                jedis.del(keys.toArray(keyArr));
            }
        }finally {
            redisUtil.returnResource(jedis);
        }
    }
}
