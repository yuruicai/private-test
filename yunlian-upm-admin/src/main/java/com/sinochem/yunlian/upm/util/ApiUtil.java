package com.sinochem.yunlian.upm.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

/**
 * @author zhangxi
 * @created 13-3-5
 */
public final class ApiUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ApiUtil.class);
    /**
     * 从Reader中读取json串反序列化为指定对象
     *
     * @param reader
     * @param <T>
     * @return
     */
    public static <T> T parse(Reader reader, Class<T> clazz) {
        try {
            String json = IOUtils.copyToString(reader);
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
