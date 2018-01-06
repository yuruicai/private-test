package com.sinochem.yunlian.upm.api.vo;

import com.sinochem.yunlian.upm.api.util.ReflectionUtils;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangyang
 * @Description: 返回给client的响应数据对象
 * @date 2017/12/28 下午1:34
 */

@Getter
public class Response {
    private static final String NOT_SUPPORT_CLASS = "String;Integer;Double;Long;Float;Short";

    private boolean success;
    private String msg;
    private Map data;

    private Response(boolean success, Map data, String msg) {
        this.success = success;
        this.data = (data == null ? new HashMap() : data);
        this.msg = msg;
    }

    public static Response succeed(Object data) {
        if (data == null) {
            data = Collections.emptyMap();
        }
        String simpleName = data.getClass().getSimpleName();
        if (NOT_SUPPORT_CLASS.contains(simpleName)) {
            throw new RuntimeException("不支持基本数据类型：type=" + simpleName);
        }
        if (data instanceof Map) {
            return new Response(true, (Map) data, "");
        }
        return new Response(true, ReflectionUtils.toMap(data), "");
    }


    public static Response succeed() {
        return new Response(true, null, "");
    }

    public static Response fail(String msg) {
        return new Response(false, null, msg);
    }

    public static Response build(boolean success, Map data, String msg) {
        return new Response(success, data, msg);
    }

    /**
     * 添加数据项
     *
     * @param key
     * @param value
     * @return
     */
    public Response put(String key, Object value) {
        data.put(key, value);
        return this;
    }
}
