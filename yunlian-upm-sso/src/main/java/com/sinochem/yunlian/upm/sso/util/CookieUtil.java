package com.sinochem.yunlian.upm.sso.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-07-05
 */
public class CookieUtil {

    private static final Logger log = LoggerFactory.getLogger(CookieUtil.class);

    public static void setCookie(HttpServletResponse response, String key, Object value) {
        String json = JSON.toJSONString(value);
        Cookie cookie = null;
        try {
            cookie = new Cookie(key, URLEncoder.encode(json, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        response.addCookie(cookie);
    }

    public static <T> T getCookie(HttpServletRequest request, String key, Type type) {
        try {
            Cookie cookie = SSOUtil.getCookie(request, key);
            String json = URLDecoder.decode(cookie.getValue().trim(), "UTF-8");
            return JSON.parseObject(json, type);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
