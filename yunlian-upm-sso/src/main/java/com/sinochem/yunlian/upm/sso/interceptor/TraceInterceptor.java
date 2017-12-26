package com.sinochem.yunlian.upm.sso.interceptor;

import com.sinochem.yunlian.upm.common.util.ThreadCacheUtil;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangxi
 * @created 13-5-27
 */
@Component
public class TraceInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(TraceInterceptor.class);
    private static List<String> restUrls = Arrays.asList("/logout/", "/acl/sso/sessioninfo/", "/acl/account/logout/",
            "/api/logout/", "/api/session/");

    @Resource
    private SessionService sessionService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            ThreadCacheUtil.release();
            TraceContext.remove();
            String uri = request.getRequestURI().replaceAll(";.*", "");
            uri = uri.substring(request.getContextPath().length());
            // 静态文件不记log
            if (uri.indexOf('.') != -1 || uri.equalsIgnoreCase("/api/monitor/alive")) {
                return true;
            }
            // transform restUrls
            String sidFromRestUrl = null;
            for (String r : restUrls) {
                if (uri.startsWith(r)) {
                    sidFromRestUrl = uri.substring(r.length());
                    uri = r + "sid";
                }
            }
            String method = request.getMethod();
            TraceContext.put("action", uri + "." + method);
            TraceContext.put(Constants.CONTEXT_UUID_KEY, SSOUtil.getCookieValue(request, Constants.UUID_KEY));
            TraceContext.put(Constants.SEC_UTC_KEY, SSOUtil.getCookieValue(request, Constants.SEC_UTC_KEY));
            TraceContext.put(Constants.CONTEXT_USER_ID, SSOUtil.getCookieValue(request, Constants.CONTEXT_USER_ID));
            TraceContext.put(Constants.CONTEXT_IP_KEY, getRealIp(request));
            TraceContext.put(Constants.CONTEXT_USER_NAME, request.getParameter(Constants.CONTEXT_USER_NAME));
            String parameter = request.getParameter(Constants.CONTEXT_ISMOBILE_KEY);
            String token = SSOUtil.getCookieValue(request, "skmtusid");
            if (StringUtil.isNotBlank(sidFromRestUrl) && !sidFromRestUrl.equalsIgnoreCase(token)) {
                if (StringUtil.isNotBlank(token)) {
                    LOG.warn(uri + " with diff sid " + sidFromRestUrl + "," + token);
                }
                token = sidFromRestUrl;
            }
            TraceContext.put(Constants.CONTEXT_TOKEN, token);
            String service = SSOUtil.getRedirectService(request);
            TraceContext.put(Constants.CONTEXT_SERVICE_KEY, getServiceHost(service));
            // TODO need fix after use serviceToken
            TraceContext.put("serviceToken", token);
            TraceContext.put("origin", service);
            TraceContext.put("status", Status.ok);
            // other auth context
            TraceContext.put(Constants.CONTEXT_UA_KEY, request.getHeader("User-Agent"));
            TraceContext.put(Constants.CONTEXT_REFERER_KEY, request.getHeader(Constants.CONTEXT_REFERER_KEY));
            TraceContext.put(Constants.CONTEXT_OS_KEY, request.getParameter(Constants.CONTEXT_OS_KEY));
            TraceContext.put(Constants.CONTEXT_SCREEN_KEY, request.getParameter(Constants.CONTEXT_SCREEN_KEY));

            // upgrade user info by token
            if (StringUtil.isNotBlank(token)) {
                Session session = sessionService.getSession(token);
                if (session != null) {
                    TraceContext.put(Constants.CONTEXT_USER_ID, session.getUserId());
                    TraceContext.put(Constants.CONTEXT_USER_NAME, session.getUserLogin());
                }
            }
        } catch (Exception e) {
            LOG.error("tracer catch exception", e);
        }
        return true;
    }

    private static Pattern pattern = Pattern.compile("^(?i)http(s?)://([a-z0-9\\-._~%]+|\\[[a-f0-9:.]+\\]|\\[v[a-f0-9][a-z0-9\\-._~%!$&'()*+,;=:]+\\])(:[0-9]+)?/?(.*)$");

    public static String getServiceHost(String service) {
        try {
            if (StringUtil.isNotBlank(service)) {
                Matcher matcher = pattern.matcher(service);
                if (matcher.matches()) {
                    return matcher.group(2);
                } else if (!service.equalsIgnoreCase("moma") && !service.contains("http")) {
                    return "notHttpRequest";
                }
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return service;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        try {
            ThreadCacheUtil.release();
            TraceContext.remove();
        } catch (Exception e) {
            LOG.error("tracer catch exception", e);
        }
    }

    private static String getRealIp(HttpServletRequest request) {
        String ip = head(request, "X-Real-IP");
        if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = head(request, "X-Forwarded-For");
        if (ip != null) {
            int index = ip.indexOf(',');
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            return (index != -1) ? ip.substring(0, index) : ip;
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip == null ? "unkown" : ip;
    }

    private static String head(HttpServletRequest req, String s) {
        return req.getHeader(s);
    }
}
