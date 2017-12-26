package com.sinochem.yunlian.upm.filter.sso;

import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.PathMatchingFilter;
import com.sinochem.yunlian.upm.filter.util.StringUtils;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class LogoutFilter extends PathMatchingFilter {
    private static final Logger LOG = LoggerFactory.getLogger(LogoutFilter.class);
    private String logoutUrl;
    public static final String DEFAULT_CALLBACK_URI = "/mt-sso";
    private String callbackUri = DEFAULT_CALLBACK_URI;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        UserUtils.clean();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        Cookie cookie = SSOUtils.getCookie(httpRequest, getTokenCookie());
        if (cookie != null) {
            String sid = cookie.getValue();
            if (sid != null) {
                LOG.info("api logout..." + sid);
                if (!getAuthService().logout(sid)) {
                    LOG.warn("api logout fail...");
                }
            }
        }
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        SSOUtils.removeCookie(getTokenCookie(), httpRequest, httpResponse);
        String path = request.getParameter("path");
        String service = "";
        if(!StringUtils.hasText(path)) {
            service = "http://" + httpRequest.getHeader("Host") + httpRequest.getContextPath();
        }else{
            service = "http://" + httpRequest.getHeader("Host") + httpRequest.getContextPath() + path;
        }
        WebUtils.issueRedirect(request, response, getLogoutUrl() + "/logout?service=" + URLEncoder.encode
                (service, "UTF-8"));
        return false;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getCallbackUri() {
        return callbackUri;
    }

    public void setCallbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
    }
}
