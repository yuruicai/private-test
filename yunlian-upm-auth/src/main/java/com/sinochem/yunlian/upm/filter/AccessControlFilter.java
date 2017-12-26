package com.sinochem.yunlian.upm.filter;

import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AccessControlFilter extends PathMatchingFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AccessControlFilter.class);
    public static final String DEFAULT_LOGIN_URL = "/login";
    public static final String DEFAULT_CALLBACK_URI = "/mt-sso";

    private String loginUrl = DEFAULT_LOGIN_URL;
    private String callbackUri = DEFAULT_CALLBACK_URI;
    private String appkey;
    private String secret;
    private boolean ignoreAcceptHeader = true;

    public AccessControlFilter() {
    }

    public String getCallbackUri() {
        return callbackUri;
    }

    public void setCallbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public boolean isIgnoreAcceptHeader() {
        return ignoreAcceptHeader;
    }

    public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader) {
        this.ignoreAcceptHeader = ignoreAcceptHeader;
    }

    protected abstract boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception;

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return onAccessDenied(request, response);
    }

    protected abstract boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception;

    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }

    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        return pathsMatch(getLoginUrl(), request);
    }

    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        saveRequest(request);
        redirectToLogin(request, response);
    }

    protected void saveRequest(ServletRequest request) {
        WebUtils.saveRequest(request);
    }

    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String host = SSOUtils.getHost(httpServletRequest);
        String service = "http://" + host + httpServletRequest.getContextPath() +
                getCallbackUri();
        StringBuilder sb = new StringBuilder();
        sb.append(getLoginUrl() ).append("/auth")
                .append("?return=").append(java.net.URLEncoder.encode(service, "UTF-8"));

        String url = sb.toString();
        LOG.info("redirect to sso " + url);
        WebUtils.issueRedirect(request, response, url);
    }

    protected void sendUnauthorized( ServletResponse response) throws IOException{
        LOG.debug("sendUnauthorized");
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setHeader("Cache-Control", "no-cache");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", -1);
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    protected void sendForbidden(ServletResponse response) throws IOException{
        LOG.debug("sendForbidden");
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setHeader("Cache-Control", "no-cache");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", -1);
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
