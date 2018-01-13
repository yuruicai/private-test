package com.sinochem.yunlian.upm.admin.web;

import com.sinochem.yunlian.upm.admin.bean.UserListBean;
import com.sinochem.yunlian.upm.admin.bean.UserRoleBean;
import com.sinochem.yunlian.upm.admin.service.RoleService;
import com.sinochem.yunlian.upm.admin.service.UserRoleService;
import com.sinochem.yunlian.upm.admin.service.UserService;
import com.sinochem.yunlian.upm.filter.annotation.RequiresPermissions;
import com.sinochem.yunlian.upm.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("userRole")
public class UserRoleController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @RequiresPermissions("userRole:singleConf")
    @RequestMapping("singleConf")
    public ModelAndView singleConf(String userId) {
        ModelAndView mv = new ModelAndView("userRole/singleConf");
        String appId = (String) ThreadCacheUtil.getData("appId");
        mv.addObject("roles", roleService.getByApp(appId));
        if (userId != null) {
            mv.addObject("userId", userId);
            mv.addObject("user", userService.getById(userId).getName());
        }
        List<String> contexts = new ArrayList<String>();
        mv.addObject("contextsJson", JsonUtil.toJsonString(contexts));

        return mv;
    }

    @RequiresPermissions("userRole:save")
    @RequestMapping("save")
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request) {
        Map<String, Object> mv;
        try {
            UserRoleBean userRoleBean = ApiUtil.parse(request.getReader(), UserRoleBean.class);
            LOG.info(userRoleBean.getUserIds().size() + " " + userRoleBean.getRoleConfBeans().size());
            LOG.info("save {}", JsonUtil.toJsonString(userRoleBean));

            userRoleService.saveUserRole(userRoleBean);
            mv = AjaxResultUtil.createAjaxSuccessMap("保存成功");
        } catch (Exception e) {
            LOG.warn("request reader err");
            mv = AjaxResultUtil.createAjaxFailureMap("保存失败");
        }
        return mv;
    }

    @RequestMapping("roleConf.ajax")
    @ResponseBody
    public Map<String, Object> roleConf(String userId) {
        Map<String, Object> mv = AjaxResultUtil.createAjaxSuccessMap();
        mv.put("data", userRoleService.getRoleConfsByUserId(userId, null));
        return mv;
    }

    /**
     * 配置角色的用户
     * @param userListBean
     * @param page
     * @param roleId
     * @return
     */
    @RequiresPermissions("userRole:listHaveRole")
    @RequestMapping(value = "listHaveRole", method = RequestMethod.GET)
    public ModelAndView listHaveRole(UserListBean userListBean, Page page, @RequestParam("roleId")String roleId) {
        List<AclUser> aclUserList = userRoleService.getUsersHaveRole(roleId);
        ModelAndView mv = new ModelAndView("userRole/listHaveRole");
        mv.addObject("userListBean", userListBean);
        mv.addObject("page", page);
        mv.addObject("userList", userRoleService.makeUserBean(aclUserList));
        mv.addObject("role", roleService.getById(roleId));
        return mv;
    }
    @RequestMapping(value = "/listHaveRoleNon/{roleId}")
    public ModelAndView listHaveRoleNon(UserListBean userListBean, @PathVariable String roleId, HttpServletRequest request, HttpServletResponse response ) throws IOException {
        List<AclUser> aclUserList = userRoleService.getUsersHaveRole(roleId);
//        ModelAndView mv = new ModelAndView("userRole/listHaveRole");
//        mv.addObject("userListBean", userListBean);
//        mv.addObject("page", page);
//        mv.addObject("userList", JsonUtil.toJsonString(aclUserList));
//        mv.addObject("role", roleService.getById(roleId));
//        return mv;

        String jsonStr = JsonUtil.toJsonString(aclUserList);
        response.getWriter().write(jsonStr);
        return null;
    }
    @RequiresPermissions("userRole:delete")
    @RequestMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(String userId, String roleId) {
        if(userId == null || roleId == null) {
            return AjaxResultUtil.createAjaxFailureMap("用户/角色为空");
        }
        userRoleService.deleteRltByUserAndRole(userId, roleId, null, null);
        return AjaxResultUtil.createAjaxSuccessMap();
    }

}
