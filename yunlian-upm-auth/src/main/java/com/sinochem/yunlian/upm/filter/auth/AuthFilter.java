package com.sinochem.yunlian.upm.filter.auth;

import com.sinochem.yunlian.upm.filter.exception.UnAuthorizedException;
import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.sso.UserFilter;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import com.sinochem.yunlian.upm.filter.util.StringUtils;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.tools.AuthResult;
import com.sinochem.yunlian.upm.tools.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends UserFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);
    private String unauthorizedUrl;
    private boolean throwUnAuthorizedException;

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public Boolean getThrowUnAuthorizedException() {
        return throwUnAuthorizedException;
    }

    public void setThrowUnAuthorizedException(Boolean throwUnAuthorizedException) {
        this.throwUnAuthorizedException = throwUnAuthorizedException;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean result = super.isAccessAllowed(request, response, mappedValue);
        if (result) {
            User user = UserUtils.getUser();
            String userId = user.getId();
            String resource = WebUtils.toHttp(request).getRequestURI();
            return auth(getAppkey(), userId, resource);
        } else {
            LOG.warn("access denied... " + result + "," + UserUtils.getUser());
        }
        return false;
    }

    protected boolean auth(String appkey, String userId, String resource) {
        AuthResult result = getAuthService().authResource(appkey, userId, resource);
        User user = UserUtils.getUser();
        return result.isAuth();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        User user = UserUtils.getUser();
        LOG.info("onAccessDenied... " + user);

        //Accept是application/json，则通过HTTP状态码进行返回;否则执行默认的HTTP Redirect
        if(!isIgnoreAcceptHeader() && WebUtils.isAcceptJSON(request)){
            if (user == null || user.getId() == null) {
                sendUnauthorized(response);
            }else{
                sendForbidden(response);
            }
            return false;
        }

        if (user == null || user.getId() == null) {
            LOG.info("redirect to login...");
            saveRequestAndRedirectToLogin(request, response);
        } else {
            LOG.info("redirect to un auth...");
            if (getThrowUnAuthorizedException()) {
                throw new UnAuthorizedException();
            }
            String unauthorizedUrl = getUnauthorizedUrl();
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                HttpServletResponse httpResponse = WebUtils.toHttp(response);
                httpResponse.setHeader("Cache-Control", "no-cache");
                httpResponse.setHeader("Pragma", "no-cache");
                httpResponse.setDateHeader("Expires", -1);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        return false;
    }

    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        String entry = SSOUtils.getEntry(WebUtils.toHttp(request));
        Cookie cookie = new Cookie("sso.saveRequest", entry);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        LOG.info("onAccessDenied... set sso.saveRequest " + entry);
        redirectToLogin(request, httpServletResponse);
    }
}
