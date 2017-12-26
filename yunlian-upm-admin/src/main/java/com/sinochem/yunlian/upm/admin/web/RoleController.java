package com.sinochem.yunlian.upm.admin.web;

import com.sinochem.yunlian.upm.admin.bean.RoleBean;
import com.sinochem.yunlian.upm.admin.bean.RoleListBean;
import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.admin.service.ApplicationService;
import com.sinochem.yunlian.upm.admin.service.RolePermissionRltService;
import com.sinochem.yunlian.upm.admin.service.RoleService;
import com.sinochem.yunlian.upm.filter.annotation.RequiresPermissions;
import com.sinochem.yunlian.upm.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/role")
public class RoleController {
    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

    @Resource
    private RoleService roleService;
    @Resource
    private RolePermissionRltService rolePermissionRltService;
    @Resource
    private ApplicationService applicationService;


    @RequiresPermissions("role:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView view = new ModelAndView("/role/createRole");
        RoleBean bean = new RoleBean();
        bean.setId(null);
        LOG.debug("create " + bean);
        view.addObject("role", bean);
        return view;
    }

    @RequiresPermissions("role:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(RoleBean bean) {
        ModelAndView view = new ModelAndView("/role/edit");
        AclRole role = roleService.getById(bean.getId());
        if (role == null) {
            view.addObject("errMsg", "该角色不存在");
            return view;
        }
        BeanUtils.copyProperties(role, bean);
        bean.setApplicationName(applicationService.getNameById(bean.getApplicationId()));
        LOG.debug("edit " + bean);
        view.addObject("role", bean);
        return view;
    }

    @RequiresPermissions("role:save")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(RoleBean bean) {
        ModelAndView view = new ModelAndView("redirect:/role/list");
        view.addObject("role", bean);
        LOG.debug("save " + bean);
        bean = roleService.insertOrUpdate(bean);
        if (bean != null) {
            view.addObject("msg", "保存成功");
            view.addObject("role", bean);
        } else {
            String error = UpmCacheUtil.getErrMsg();
            LOG.info("save role failed..." + error);
            view.addObject("errMsg", error);
        }
        return view;
    }

    @RequiresPermissions("role:createRole")
    @RequestMapping(value = "/createRole", method = RequestMethod.POST)
    public void createRole(RoleBean bean,HttpServletResponse response)throws  Exception{
        response.setCharacterEncoding("UTF-8");
        LOG.debug("save " + bean);
        bean = roleService.insertOrUpdate(bean);
        String msg = "保存成功";
        if (bean == null) {
            msg = UpmCacheUtil.getErrMsg();
        }
        response.getWriter().write(msg);
    }

    @RequiresPermissions("role:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(RoleListBean roleListBean, Page page) {
        if (page == null) {
            page = new Page(1, 20);
        }

        if (null != roleListBean
                && (null == roleListBean.getApplicationId() || roleListBean.getApplicationId().equals(0))) {
            roleListBean.setApplicationId((String) ThreadCacheUtil.getData("appId"));
        }

        String userId = UpmCacheUtil.getCurrentUserId();
        List<AclRole> list = roleService.getList(roleListBean);

        ModelAndView view = new ModelAndView("role/list");
        view.addObject("roleListBean", roleListBean);
        view.addObject("page", page);
        view.addObject("list", roleService.makeRoleBean(list));

        return view;
    }

    @RequiresPermissions("role:listOfApp")
    @RequestMapping("/listOfApp")
    @ResponseBody
    public Map<String, Object> listOfApp() {
        List<AclRole> aclRoles = roleService.getByApp((String) ThreadCacheUtil.getData("appId"));
        Map<String, Object> map = AjaxResultUtil.createAjaxSuccessMap();
        map.put("data", roleService.makeRoleBean(aclRoles));
        return map;
    }

    @RequiresPermissions("role:permission")
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public ModelAndView permission(String curRoleId, String appId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("/role/permission");
        if (appId == null) {
            appId = (String) ThreadCacheUtil.getData("appId");
        }
        AclRole role = roleService.getById(curRoleId);
        view.addObject("roleId", role.getId());
        view.addObject("name", role.getName());
        view.addObject("code", role.getCode());
        view.addObject("comment", role.getComment());
        view.addObject("appName", CookieUtil.getAppName(request));
        return view;
    }

    @RequiresPermissions("role:getMenus")
    @RequestMapping("/getMenus/{roleId}")
    public void getMenus(@PathVariable String roleId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String applicationId = CookieUtil.getCookie(request, "appId");
        String jsonMenu = roleService.getMenuByRoleId(applicationId, roleId);
        response.getWriter().write(jsonMenu);
    }

    @RequiresPermissions("role:permission")
    @RequestMapping(value = "/permission", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePermission(String roleId, String[] ids) throws IOException {
        try {
            if (roleId != null && ids != null && ids.length != 0) {
                rolePermissionRltService.addPermission(roleId, ids);
                return AjaxResultUtil.createAjaxSuccessMap("保存成功");
            }
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        return AjaxResultUtil.createAjaxFailureMap("服务内部异常");
    }

    @RequiresPermissions("role:delete")
    @RequestMapping("delete")
    @ResponseBody
    public Map<String, Object> deleteRole(String id) {
        String msg = roleService.deleteRoleById(id);
        if (msg == null) {
            return AjaxResultUtil.createAjaxSuccessMap("已删除角色");
        } else {
            return AjaxResultUtil.createAjaxFailureMap(msg);
        }
    }

    @RequestMapping("createCode.ajax")
    @ResponseBody
    public Map<String, Object> createCode(String appId, String name) {
        Map<String, Object> map = AjaxResultUtil.createAjaxSuccessMap();
        String md5 = DigestUtils.md5Hex(name + new Date().getTime());
        String appName = applicationService.getAppkeyById(appId);
        map.put("data", appName + "_" + md5);
        return map;
    }
}
