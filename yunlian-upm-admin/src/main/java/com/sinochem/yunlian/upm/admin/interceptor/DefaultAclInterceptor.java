package com.sinochem.yunlian.upm.admin.interceptor;

import com.sinochem.yunlian.upm.admin.service.ApplicationService;
import com.sinochem.yunlian.upm.admin.service.UpmService;
import com.sinochem.yunlian.upm.filter.servlet.WebUtils;
import com.sinochem.yunlian.upm.filter.util.SSOUtils;
import com.sinochem.yunlian.upm.filter.util.UserUtils;
import com.sinochem.yunlian.upm.tools.User;
import com.sinochem.yunlian.upm.tools.Menu;
import com.sinochem.yunlian.upm.util.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DefaultAclInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UpmService upmService;
    @Resource
    private ApplicationService applicationService;

    /**
     * 请求前处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // log标记
        ThreadCacheUtil.setData("acl_log_flag", UUID.randomUUID().toString());

        String userId = UpmCacheUtil.getCurrentUserId();
        if (StringUtil.isNotBlank(userId)) {
            saveMenus(request);
            saveUser(request, UserUtils.getUser());

            String appId = CookieUtil.getAppId(request);
            Set<String> allowedAppIds = upmService.getAllowedAppIds(userId);
            if(CollectionUtils.isEmpty(allowedAppIds)){
                CookieUtil.removeCookie(UpmCacheUtil.KEY_APPID, request, response);
                CookieUtil.removeCookie(UpmCacheUtil.KEY_APPNAME, request, response);
            }else{
                if (null == appId || !allowedAppIds.contains(appId)) {
                    if (!CollectionUtils.isEmpty(allowedAppIds)) {
                        appId = allowedAppIds.iterator().next();
                    }
                }
                if (StringUtil.isNotBlank(appId)) {
                    ThreadCacheUtil.setData(UpmCacheUtil.KEY_APPID, appId);
                    CookieUtil.addAppCookie(appId, applicationService.getNameById(appId), request, WebUtils.toHttp(response));
                }
            }
        }

        return true;
    }

    private void saveMenus(HttpServletRequest request) {
        // ajax 请求不需要菜单
        if (HttpUtil.isAjax(request)) {
            return;
        }

        List<Menu> menus = UserUtils.getMenus();
        if (menus == null) {
            return;
        }
        request.setAttribute("__menus__", menus);
        request.setAttribute("__menus", menus);

        String currentURI = request.getRequestURI();
        for (Menu menu : menus) {

            List<Menu> subMeuns = menu.getMenus();
            if(subMeuns==null){
                continue;
            }
            for (Menu subMenu : subMeuns) {
                String url = subMenu.getUrl();
                if (url.indexOf("?") > 0) {
                    url = url.substring(0, url.indexOf("?"));
                }
                // 如果该operation有menuid，则使用该operation的
                String operationMenuId = (String) request.getAttribute("_currentMenuId");
                if (operationMenuId != null) {
                    if (subMenu.getId() == operationMenuId) {
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

    private void saveUser(HttpServletRequest request, User user) {
        request.setAttribute("__user__", user);
        request.setAttribute("__currentUser__", user);
        request.setAttribute("_currentUser", user);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        AclUser aclUser = UpmCacheUtil.getCurrentUser();
        if (modelAndView != null) {
            Cookie appCookie = SSOUtils.getCookie(request, "appId");
            if (appCookie != null) {
                modelAndView.addObject("appId", appCookie.getValue());
            }
            if (aclUser == null) {
                modelAndView.addObject("curUserName", "xxx");
            } else {
                String service = "http://" + request.getHeader("Host")
                        + request.getContextPath() + "/mt-sso";
                modelAndView.addObject("curUserName", aclUser.getName());
                modelAndView.addObject("logoutUrl", "http://localhost:8080"
                        + "/logout?service=" + service);
                Cookie errMsgCookie = SSOUtils.getCookie(request, "UpmErrMsg");
                Cookie msgCookie = SSOUtils.getCookie(request, "UpmMsg");
                if (errMsgCookie != null) {
                    modelAndView.addObject("errMsg", CookieUtil.getMsg(response, errMsgCookie));
                } else if (msgCookie != null) {
                    modelAndView.addObject("msg", CookieUtil.getMsg(response, msgCookie));
                }
            }
        }
        ThreadCacheUtil.release();
    }
}
