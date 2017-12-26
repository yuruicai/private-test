package com.sinochem.yunlian.upm.sso.interceptor;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.sso.service.SessionService;
import com.sinochem.yunlian.upm.sso.util.SSOUtil;
import com.sinochem.yunlian.upm.sso.model.Session;
import com.sinochem.yunlian.upm.sso.model.User;
import com.sinochem.yunlian.upm.tools.Menu;
import com.sinochem.yunlian.upm.tools.UpmAuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author zhangxi
 * @created 14-1-23
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private SessionService sessionService;
    @Resource
    private UpmAuthService upmAuthService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String uri = request.getRequestURI().replaceAll(";.*", "");
        if (uri.startsWith("/admin")) {
            String token = SSOUtil.getSid(request);
            if (token != null) {
                Session session = sessionService.getSession(token);
                if (session != null) {
                    List<Menu> menus = upmAuthService.getMenus("sso", session.getUserId());
                    saveMenus(request, menus);
                    User user = new User();
                    user.setId(session.getUserId());
                    user.setLogin(session.getUserLogin());
                    user.setName(session.getUserName());
                    request.setAttribute("__user__", user);
                    request.setAttribute("__currentUser__", user);
                    request.setAttribute("_currentUser", user);
                    return true;
                }
            }
            setTarget(request, response);
            response.sendRedirect("/login");
            return false;
        } else {
            return true;
        }
    }

    private void saveMenus(HttpServletRequest request, List<Menu> menus) {
        if (menus == null) {
            return;
        }
        request.setAttribute("__menus__", menus);
        request.setAttribute("__menus", menus);
        request.setAttribute("__menus__json", JSON.toJSONString(menus));

        String currentURI = request.getRequestURI();
        for (Menu menu : menus) {
            for (Menu subMenu : menu.getMenus()) {
                String url = subMenu.getUrl();
                if (url.indexOf("?") > 0) {
                    url = url.substring(0, url.indexOf("?"));
                }
                // 如果该operation有menuid，则使用该operation的
                Integer operationMenuId = (Integer) request.getAttribute("_currentMenuId");
                if (operationMenuId != null && operationMenuId != 0) {
                    if (subMenu.getId().equals(operationMenuId)) {
                        request.setAttribute("_currentMenuId", operationMenuId);
                        request.setAttribute("_currentParentMenuId", menu.getId());
                        return;
                    }
                }
                if (url.equals(currentURI)) {
                    request.setAttribute("_currentMenuId", subMenu.getId());
                    request.setAttribute("_currentParentMenuId", menu.getId());
                    return;
                }
            }
        }
    }

    private void setTarget(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        String entry = getEntry(request);
        Cookie cookie = new Cookie("sso.saveRequest", java.net.URLEncoder.encode(entry, "UTF-8"));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * @param request
     * @return
     */
    private String getEntry(HttpServletRequest request) {
        String entry = request.getRequestURL().toString();
        String query = request.getQueryString();
        if (query != null) {
            entry += "?" + query;
        }
        entry = entry.substring(entry.indexOf("//") + 2);

        String host = getHost(request);
        if (entry.indexOf("/") != -1) {
            entry = "http://" + host + entry.substring(entry.indexOf("/"));
        } else {
            entry = "http://" + host;
        }
        return entry;
    }

    public String getHost(HttpServletRequest request) {
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null) {
            host = request.getHeader("Host");
        }
        return host;
    }

    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
