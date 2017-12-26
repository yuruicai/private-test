package com.sinochem.yunlian.upm.sso.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 处理json的工具类
 * 
 * @version 1.0
 */
public final class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
    }


    /**
     * 对象转换成json字符串
     * 
     */
    public static String toJsonString(Object o) {
        return JSON.toJSONString(o);
    }

    /**
     * 将json串转换成对象
     * 
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Throwable e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 将json串转换成map
     * 
     */
    public static Map<?, ?> toMap(String json) {
        try {
            return JSON.parseObject(json, Map.class);
        } catch (Throwable e) {
            log.error("", e);
            return null;
        }
    }

    public static List<?> toList(String json) {
        try {
            return JSON.parseObject(json, List.class);
        } catch (Throwable e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 将json串转换成数组
     * 
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(String json, T[] array) {
        try {
            array = (T[]) JSON.parseObject(json, Object[].class);
            return array;
        } catch (Throwable e) {
            log.error("", e);
            return null;
        }
    }
}
