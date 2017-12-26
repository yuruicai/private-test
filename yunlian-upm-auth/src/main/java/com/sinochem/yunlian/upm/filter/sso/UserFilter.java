package com.sinochem.yunlian.upm.filter.sso;

import com.sinochem.yunlian.upm.filter.AccessControlFilter;
import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import com.sinochem.yunlian.upm.tools.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class UserFilter extends AccessControlFilter {
    private static final Logger LOG = LoggerFactory.getLogger(UserFilter.class);

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        UserUtils.clean();
        Cookie cookie = SSOUtils.getCookie(WebUtils.toHttp(request), getTokenCookie());
        if (cookie != null) {
            String sid = cookie.getValue();
            if (sid != null) {
                User user = UserUtils.fetchUser(sid);
                LOG.debug("bind user " + user);
                if (user != null && user.getId() != null) {
                    request.setAttribute("__user__", user);
                    request.setAttribute("__currentUser__", user);
                    request.setAttribute("_currentUser", user);
                    return true;
                } else {
                    LOG.info("can't get user from sid... " + sid + "," + user);
                }
            } else {
                LOG.info("access denied, sid is null... " + sid + "," + cookie);
            }
        } else {
            LOG.info("access denied, cookie is null... " + getTokenCookie() + "," + cookie);
        }
        return false;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //Accept是application/json，则通过HTTP状态码进行返回;否则执行默认的HTTP Redirect
        if(!isIgnoreAcceptHeader() && WebUtils.isAcceptJSON(request)){
            sendUnauthorized(response);
            return false;
        }

        User user = UserUtils.getUser();
        LOG.info("onAccessDenied... " + user);
        UserUtils.clean();
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        String entry = SSOUtils.getEntry(WebUtils.toHttp(request));
        Cookie cookie = new Cookie("sso.saveRequest", entry);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        LOG.info("onAccessDenied... set sso.saveRequest " + entry);
        redirectToLogin(request, httpServletResponse);
        return false;
    }
}
