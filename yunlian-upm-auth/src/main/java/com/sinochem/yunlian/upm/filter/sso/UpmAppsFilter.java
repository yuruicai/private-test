package com.sinochem.yunlian.upm.filter.sso;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.filter.PathMatchingFilter;
import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.tools.Application;
import com.sinochem.yunlian.upm.tools.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UpmAppsFilter extends PathMatchingFilter {
    private static final Logger LOG = LoggerFactory.getLogger(UpmAppsFilter.class);
    private String logoutUrl;
    public static final String DEFAULT_CALLBACK_URI = "/mt-sso";
    private String callbackUri = DEFAULT_CALLBACK_URI;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        UserUtils.clean();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);

        List<Application> applications = new ArrayList<Application>();
        Cookie cookie = SSOUtils.getCookie(httpRequest, getTokenCookie());
        if (cookie != null) {
            String sid = cookie.getValue();
            if (sid != null) {
                LOG.info("api logout..." + sid);
                User user = UserUtils.fetchUser(sid);
                if(user != null && user.getId() != null){
                    applications = getAuthService().getUserApps(user.getId());
                }
            }
        }
        httpResponse.setContentType("application/json");
        PrintWriter printWriter = httpResponse.getWriter();
        printWriter.write(JSON.toJSONString(applications));
        printWriter.close();

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
