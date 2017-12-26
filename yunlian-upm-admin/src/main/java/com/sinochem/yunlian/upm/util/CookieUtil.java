package com.sinochem.yunlian.upm.util;

import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * TODO 在这里编写类的功能描述
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-07-05
 */
public class CookieUtil {

    private static final Logger log = LoggerFactory.getLogger(CookieUtil.class);

    public static String getAppId(HttpServletRequest request) {
        return getCookie(request, UpmCacheUtil.KEY_APPID);
    }

    public static String getAppName(HttpServletRequest request) {
        return getCookie(request, UpmCacheUtil.KEY_APPNAME);
    }

    public static void addAppCookie(String appId, String appName, HttpServletRequest request, HttpServletResponse response) {
        try {
            SSOUtils.removeCookie(UpmCacheUtil.KEY_APPID, request, response);
            SSOUtils.removeCookie(UpmCacheUtil.KEY_APPNAME, request, response);

            addAppCookie(UpmCacheUtil.KEY_APPID, String.valueOf(appId), response);
            addAppCookie(UpmCacheUtil.KEY_APPNAME, URLEncoder.encode(appName, "UTF-8"), response);
        }catch (UnsupportedEncodingException e ){
            log.error("addAppCookie error", e);
        }

    }

    private static void addAppCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(SSOUtils.ROOT_PATH);
        cookie.setMaxAge(SSOUtils.DEFAULT_MAX_AGE);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String key) {
        try {
            Cookie cookie = SSOUtils.getCookie(request, key);
            if(cookie != null && StringUtil.isNotBlank(cookie.getValue())){
               return URLDecoder.decode(cookie.getValue().trim(), "UTF-8");
            }
            return null;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    public static void setOkMsg(HttpServletResponse response, String okMsg) {
        Cookie cookie = null;
        try {
            cookie = new Cookie("UpmMsg", URLEncoder.encode(okMsg.trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        cookie.setMaxAge(5);
        response.addCookie(cookie);
    }

    public static void setErrMsg(HttpServletResponse response, String errMsg) {
        Cookie cookie = null;
        try {
            cookie = new Cookie("UpmErrMsg", URLEncoder.encode(errMsg.trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        cookie.setMaxAge(5);
        response.addCookie(cookie);
    }

    public static String getMsg(HttpServletResponse response, Cookie cookie) {
        String okMsg = null;
        try {
            okMsg = URLDecoder.decode(cookie.getValue(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return okMsg;
    }

    public static void removeCookie(String name, HttpServletRequest request, HttpServletResponse response){
        SSOUtils.removeCookie(name, request, response);
    }

}
