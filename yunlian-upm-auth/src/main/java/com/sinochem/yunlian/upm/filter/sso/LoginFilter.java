package com.sinochem.yunlian.upm.filter.sso;

import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import com.sinochem.yunlian.upm.filter.util.StringUtils;
import com.sinochem.yunlian.upm.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LoginFilter extends PathMatchingFilter {
    private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
    private String successUrl = "/";

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);

        String ticket = httpServletRequest.getParameter("ticket");
        String sid = getToken(ticket);
        if(!StringUtils.hasText(sid)){
            return false;
        }
        setSID(httpServletResponse, sid);
        String target = getTarget(httpServletRequest);

        LOG.info("SSO Target: {} {}", ticket, target);
        SSOUtils.removeCookie("sso.saveRequest", httpServletRequest, httpServletResponse);
        httpServletResponse.sendRedirect(target);
        return false;
    }

    private void setSID(HttpServletResponse response, String SID) {
        SSOUtils.addCookieHeader(response, getTokenCookie(), SID,
                null, null, SSOUtils.ROOT_PATH, SSOUtils.TIMEOUT, 0, false, true);
    }

    private String getTarget(HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equalsIgnoreCase("sso.saveRequest")) {
                    return java.net.URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }
        return getSuccessUrl();
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
}
