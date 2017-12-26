/*
 * Copyright (c) 2010-2013 meituan.com
 * All rights reserved.
 *
 */
package com.sinochem.yunlian.upm.admin.web;

import com.sinochem.yunlian.upm.admin.bean.MenuBean;
import com.sinochem.yunlian.upm.admin.bean.MenuListBean;
import com.sinochem.yunlian.upm.admin.domain.AclMenu;
import com.sinochem.yunlian.upm.admin.service.MenuService;
import com.sinochem.yunlian.upm.filter.annotation.RequiresPermissions;
import com.sinochem.yunlian.upm.util.AjaxResultUtil;
import com.sinochem.yunlian.upm.util.CookieUtil;
import com.sinochem.yunlian.upm.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("menu")
public class MenuController {

    private static final Logger LOG = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private MenuService menuService;

    @RequiresPermissions("menu:list")
    @RequestMapping("list")
    public ModelAndView list(MenuListBean menuListBean, HttpServletRequest request) {
        String applicationId = CookieUtil.getCookie(request, "appId");
        ModelAndView mv = new ModelAndView("menu/list");
        List<AclMenu> menuList = menuService.getAll(applicationId);
        List<MenuBean> result = new ArrayList<MenuBean>();
        for (AclMenu aclMenu:menuList){
            MenuBean m = new MenuBean();
            copy(m,aclMenu );
            result.add(m);
        }
        mv.addObject("menuList1", JsonUtil.toJsonString(result));
        return mv;
    }

    private void copy(MenuBean m,AclMenu aclMenu){
        m.setId(aclMenu.getId());
        m.setpId(aclMenu.getParentId());
        m.setName(aclMenu.getTitle());
        m.setIsShow(aclMenu.getIsShow());
        m.setPermission(aclMenu.getPermission());
        m.setUrl(aclMenu.getUrl());
        m.setShowType(aclMenu.getShowType());
    }

    @RequestMapping("getMenu.ajax")
    public String getMenu(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String applicationId = CookieUtil.getCookie(request, "appId");
        List<AclMenu> menuList = menuService.getAll(applicationId);

        List<MenuBean> result = new ArrayList<MenuBean>();
        for (AclMenu aclMenu:menuList){
            MenuBean m = new MenuBean();
            copy(m,aclMenu );
            result.add(m);
        }
        response.getWriter().write(JsonUtil.toJsonString(result));
        return null;
    }

    @RequiresPermissions("menu:save")
    @RequestMapping("save")
    @ResponseBody
    public String saveMenu(MenuBean menu, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (menu.getParentId() == null) {
        } else if (menu.getTitle() == null) {
        }
        String applicationId = CookieUtil.getCookie(request, "appId");
        menu.setParentId(menu.getpId());
        menu.setApplicationId(applicationId);
        int re = menuService.save(menu);
        if (re == 1) {
            response.getWriter().write("success");
        }else{
            response.getWriter().write("err");
        }
        return null;
    }

    @RequiresPermissions("menu:delete")
    @RequestMapping("delete")
    public String deleteMenu(AclMenu menu, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String msg = menuService.deleteMenu(menu.getId());
        if (msg == null) {
            response.getWriter().write("success");
        }else{
            response.getWriter().write("err");
        }
        return null;
    }

    @RequiresPermissions("menu:moveMenu")
    @RequestMapping("moveMenu")
    @ResponseBody
    public Map<String, Object> moveMenu(String id, String targetId, String newParentId, String moveType) {
        if (id == null) {
            return AjaxResultUtil.createAjaxFailureMap("节点不能为空");
        }
        if (targetId == null) {
            return AjaxResultUtil.createAjaxFailureMap("目标节点不能为空");
        }
        String msg = menuService.updateMoveMenu(id, null, targetId, moveType);
        if (msg != null) {
            return AjaxResultUtil.createAjaxFailureMap(msg);
        }
        return AjaxResultUtil.createAjaxSuccessMap("移动成功");
    }
}
