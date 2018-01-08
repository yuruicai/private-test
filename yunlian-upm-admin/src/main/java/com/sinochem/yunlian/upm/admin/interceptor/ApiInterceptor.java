package com.sinochem.yunlian.upm.admin.interceptor;

import com.google.common.base.Joiner;
import com.sinochem.yunlian.upm.admin.conf.ParamName;
import com.sinochem.yunlian.upm.admin.service.ApplicationService;
import com.sinochem.yunlian.upm.util.HMACSHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiInterceptor extends HandlerInterceptorAdapter {
    private final static Logger LOG = LoggerFactory.getLogger(ApiInterceptor.class);
    private final static List<String> IGNORE_URLS = new ArrayList<>();
    //正则匹配
    private final static List<String> IGNORE_REGEX_URLS = new ArrayList<>();


    static {

        //不走验证名单
        IGNORE_URLS.add("/api/monitor/alive");
        IGNORE_URLS.add("/api/saveUserRole");
        IGNORE_URLS.add("/api/deleteUserRole");
        IGNORE_URLS.add("/api/roleList");
        IGNORE_URLS.add("/api/updateUserRole");
//        IGNORE_URLS.add("/api/company");
//        IGNORE_URLS.add("/api/company/modification");
        IGNORE_REGEX_URLS.add("/api/company(/)?.*");
        IGNORE_REGEX_URLS.add("/inner/api/users(/)?.*");
    }

    @Resource
    private ApplicationService applicationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String uri = request.getRequestURI();
        String action = uri.substring(request.getContextPath().length());
        // 特殊请求不走认证
        if (action.equalsIgnoreCase("/api/monitor/alive")) {
            return true;
        }
        for (String actionForadmin : IGNORE_URLS) {
            if (actionForadmin.equalsIgnoreCase(action)) {
                return true;
            }
        }
        for (String regexUrl : IGNORE_REGEX_URLS) {
            if (action.matches(regexUrl)) {
                return true;
            }
        }
        boolean result = auth(request);
        if (!result) {
            // 内网白名单
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    public boolean auth(HttpServletRequest request) {
        try {
            String uri = request.getRequestURI();
            String method = request.getMethod();
            String remoteIp = getRealIp(request);
            String dateStr = request.getHeader("Date");
            String auth = request.getHeader("Authorization");
            if (auth != null) {
                String clientId = auth.substring(auth.indexOf(" ") + 1, auth.indexOf(":"));
                request.setAttribute(ParamName.CLIENT_ID, clientId);
                String secret = getSecret(clientId);
                if (secret.isEmpty()) {
                    LOG.info(Joiner.on(",").useForNull("null")
                            .join("client forbidden", clientId, auth, uri, method, dateStr, remoteIp));
                    return false;
                }
                String auth2 = getAuthorization(uri, method, dateStr, clientId, secret);
                if (auth.equals(auth2)) {
                    LOG.info(Joiner.on(",").useForNull("null")
                            .join("auth ok", clientId, secret, auth, auth2, uri, method, dateStr, remoteIp));
                    return true;
                } else {
                    LOG.info(Joiner.on(",").useForNull("null")
                            .join("auth fail", clientId, secret, auth, auth2, uri, method, dateStr, remoteIp));
                }
            } else {
                LOG.info(Joiner.on(",").useForNull("null")
                        .join("non authorization", remoteIp, request.getRequestURL(), request.getQueryString(), method));
            }
        } catch (Exception e) {
            LOG.error("解析BA失败", e);
        }
        return false;
    }

    public String getAuthorization(String uri, String method, String dateStr, String clientId,
                                   String secret) {
        String stringToSign = method + " " + uri + "\n" + dateStr;
        String signature = HMACSHA1.getSignature(stringToSign, secret);
        return "MWS" + " " + clientId + ":" + signature;
    }

    public String getSecret(String clientId) {
        return applicationService.getSecretByAppkey(clientId);
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
