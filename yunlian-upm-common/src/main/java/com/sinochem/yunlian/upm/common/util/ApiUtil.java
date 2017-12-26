package com.sinochem.yunlian.upm.common.util;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.common.io.IOUtils;
import com.sinochem.yunlian.upm.common.json.JSONUtil;
import com.sinochem.yunlian.upm.common.net.MtHttpResponse;
import com.sinochem.yunlian.upm.common.reflect.TypeReference;
import com.sinochem.yunlian.upm.common.rpc.ErrorCode;
import com.sinochem.yunlian.upm.common.rpc.MtError;
import com.sinochem.yunlian.upm.common.rpc.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

public final class ApiUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ApiUtil.class);

    private ApiUtil() {
    }

    /**
     * 根据data内容返回response
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Response<T> response(T data) {
        return MtHttpResponse.create(data);
    }

    /**
     * 根据data内容返回json格式的response
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String jsonResponse(T data) {
        return JSONUtil.toJSONString(response(data));
    }

    /**
     * 根据data内容返回json格式的response，并根据properties对clazz的属性进行过滤
     *
     * @param data
     * @param clazz
     * @param properties 保留的属性，可变参数
     * @param <T>
     * @return
     */
    public static <T> String jsonResponse(T data, Class<?> clazz, String... properties) {
        return JSONUtil.toJSONString(response(data), clazz, properties);
    }

    /**
     * 根据data内容返回json格式的response，并根据shows对clazz的属性进行过滤
     *
     * @param data
     * @param shows 保留的属性，以,分割
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> String jsonResponse(T data, String shows, Class<?> clazz) {
        return JSONUtil.toJSONString(response(data), clazz, shows.split(","));
    }

    /**
     * 根据系统定义错误返回response
     *
     * @param error  MtError
     * @param params MtError错误信息里含有格式化信息的情况下使用，如MtError.paramNeedRequired
     * @return
     */
    public static Response errorResponse(MtError error, Object... params) {
        String message = format(error.getMessage(), params);
        return MtHttpResponse.error(new ErrorCode(error.getCode(), error.getType().getName(), message));
    }

    /**
     * 根据系统定义错误返回json格式的response
     *
     * @param error  MtError
     * @param params MtError错误信息里含有格式化信息的情况下使用，如MtError.paramNeedRequired
     * @return
     */
    public static String jsonErrorResponse(MtError error, Object... params) {
        return JSONUtil.toJSONString(errorResponse(error, params));
    }

    private static String format(String message, Object... params) {
        try {
            return (params != null && params.length != 0) ? String.format(message, params) : message;
        } catch (Exception e) {
            LOG.warn("format error message exception...", e);
            return message;
        }
    }

    /**
     * 根据自定义错误码及message返回response
     *
     * @param errorcode 必须大于20000
     * @param message
     * @return
     */
    public static Response errorResponse(int errorcode, String message) {
        return MtHttpResponse.error(new ErrorCode(errorcode, MtError.Type.application.getName(), message));
    }

    /**
     * 根据自定义错误码及message返回json格式的response
     *
     * @param errorcode 必须大于20000
     * @param message
     * @return
     */
    public static String jsonErrorResponse(int errorcode, String message) {
        return JSONUtil.toJSONString(errorResponse(errorcode, message));
    }

    /**
     * 从Reader中读取json串反序列化为指定对象
     *
     * @param reader
     * @param clazz
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

    /**
     * 从Reader中读取json串反序列化为指定对象
     *
     * @param reader
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse(Reader reader, TypeReference<T> type) {
        try {
            String json = IOUtils.copyToString(reader);
            return JSON.parseObject(json, type.getType());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从Reader中读取json串反序列化为指定对象
     *
     */
    public static String parse(Reader reader) {
        try {
            String json = IOUtils.copyToString(reader);
            return json;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
