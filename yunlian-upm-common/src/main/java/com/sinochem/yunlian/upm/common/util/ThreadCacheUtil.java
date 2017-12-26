package com.sinochem.yunlian.upm.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangxi
 * @created 13-3-7
 */
public class ThreadCacheUtil {
    private static final ThreadLocal<ThreadContext> CACHE = new ThreadLocal<ThreadContext>() {
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };

    private ThreadCacheUtil() {
    }

    private static class ThreadContext {
        Map<String, Object> data = new ConcurrentHashMap<String, Object>();
        boolean readonly;
    }

    public static void setReadonly(boolean readonly) {
        CACHE.get().readonly = readonly;
    }

    public static boolean isReadonly() {
        return CACHE.get().readonly;
    }

    public static void release() {
        CACHE.remove();
    }

    public static void setData(String key, Object data) {
        CACHE.get().data.put(key, data);
    }

    public static Object getData(String key) {
        return CACHE.get().data.get(key);
    }
}
