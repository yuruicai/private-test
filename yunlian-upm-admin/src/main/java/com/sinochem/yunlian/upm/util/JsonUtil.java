package com.sinochem.yunlian.upm.util;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对象转换成json字符串
     * 
     */
    public static String toJsonString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonGenerationException e) {
            log.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json串转换成对象
     * 
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
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
            return objectMapper.readValue(json, Map.class);
        } catch (Throwable e) {
            log.error("", e);
            return null;
        }
    }

    public static List<?> toList(String json) {
        try {
            return objectMapper.readValue(json, List.class);
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
            array = (T[]) objectMapper.readValue(json, Object[].class);
            return array;
        } catch (Throwable e) {
            log.error("", e);
            return null;
        }
    }
}
