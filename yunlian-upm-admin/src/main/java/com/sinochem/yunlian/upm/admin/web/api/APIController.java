package com.sinochem.yunlian.upm.admin.web.api;

import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.util.SortUtil;
import com.google.common.base.Joiner;
import com.sinochem.yunlian.upm.common.rpc.MtError;
import com.sinochem.yunlian.upm.common.rpc.Response;
import com.sinochem.yunlian.upm.common.util.ApiUtil;
import com.sinochem.yunlian.upm.admin.model.AuthResult;
import com.sinochem.yunlian.upm.admin.model.Menu;
import com.sinochem.yunlian.upm.admin.model.User;
import com.sinochem.yunlian.upm.admin.service.AuthService;
import com.sinochem.yunlian.upm.admin.service.UpmService;
import com.sinochem.yunlian.upm.admin.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * @created 13-2-28
 * @mtapi.appkey mtupm
 */
@Controller
@RequestMapping(value = "/api/")
public class APIController {
    private static final Logger LOG = LoggerFactory.getLogger(APIController.class);

    @Resource
    private AuthService authService;
    @Resource
    private UserService userService;
    @Resource
    private UpmService upmService;

    @RequestMapping(value = "/monitor/alive")
    @ResponseBody
    public Map<String, Object> monitorAlive() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", "ok");
        return result;
    }

    /**
     * @mtapi.name 资源鉴权
     * @mtapi.description 验证用户是否可以访问某个资源
     * @mtapi.category 鉴权
     * @mtapi.param appkey 应用标识
     * @mtapi.param userId 用户ID
     * @mtapi.param resource 资源URL
     * @mtapi.result 鉴权结果，以及有权限的角色code列表 AuthResult
     * @mtapi.requestExample /api/auth?appkey=mtsg&userId=7655&resource=/cfg
     * @mtapi.responseExample {"data":{"auth":true,"roles":["mtsg-admin"]}}
     * @mtapi.errorExample {"data":{"auth":false,"roles":null}}
     */
    @RequestMapping(value = "authResource", method = RequestMethod.GET)
    @ResponseBody
    public Response auth(String appkey, String userId, String resource) {
        if (StringUtils.isBlank(appkey)) {
            return ApiUtil.errorResponse(MtError.paramNeedRequired, "appkey");
        }
        if (userId == null) {
            return ApiUtil.errorResponse(MtError.paramNeedRequired, "userId");
        }
        if (StringUtils.isBlank(resource)) {
            return ApiUtil.errorResponse(MtError.paramNeedRequired, "resource");
        }
        AuthResult result = new AuthResult();
        if (resource != null && !resource.trim().isEmpty()) {
            String ret = authService.auth(appkey, userId, resource);
            if (ret.length() > 0) {
                result.setAuth(true);
            } else {
                result.setAuth(false);
            }
        }

        LOG.info(Joiner.on(",").useForNull("null").join(appkey, userId, resource, result));
        return ApiUtil.response(result);
    }

    /**
     * @mtapi.name 权限标识鉴权
     * @mtapi.description 验证用户是否可以匹配某个权限标识
     * @mtapi.category 鉴权
     * @mtapi.param appkey 应用标识
     * @mtapi.param userId 用户ID
     * @mtapi.param code 权限Code
     * @mtapi.result 鉴权结果，以及有权限的角色code列表 AuthResult
     * @mtapi.requestExample /api/auth/code?appkey=mtsg&userId=7655&code=sg.admin
     * @mtapi.responseExample {"data":{"auth":true,"roles":["mtsg-admin"]}}
     * @mtapi.errorExample {"data":{"auth":false,"roles":null}}
     */
    @RequestMapping(value = "authPerm", method = RequestMethod.GET)
    @ResponseBody
    public Response authPerm(String appkey, String userId, String perm) {
        AuthResult result = new AuthResult();
        if (perm != null && !perm.trim().isEmpty()) {
            result.setAuth(authService.authPermission(appkey, userId, perm));
        }
        LOG.info(Joiner.on(",").useForNull("null").join(appkey, userId, perm, result));
        return ApiUtil.response(result);
    }

    @RequestMapping(value = "permissions/{userId}/{appkey}", method = RequestMethod.GET)
    @ResponseBody
    public Response authPerm(@PathVariable String userId, @PathVariable String appkey) {
        List<String> permissions = authService.getPermission(appkey, userId);
        LOG.info(Joiner.on(",").useForNull("null").join(appkey, userId, permissions));
        return ApiUtil.response(permissions);
    }

    /**
     * @mtapi.name 应用所有资源列表
     * @mtapi.description 获取应用所有资源列表
     * @mtapi.category 资源
     * @mtapi.param appkey 应用标识
     * @mtapi.requestExample /api/urls?appkey=mtsg
     * @mtapi.responseExample {"data":["/serverapi/delete","/app/**","/cfg**","/**"]}
     */
    @RequestMapping(value = "urls", method = RequestMethod.GET)
    public
    @ResponseBody
    Response resource(String appkey) {
        List<String> urls = authService.urls(appkey);
        SortUtil.sortUrlList(urls);
        LOG.info(Joiner.on(",").useForNull("null").join(appkey, urls));
        return ApiUtil.response(urls);
    }


    /**
     * @mtapi.name 菜单列表
     * @mtapi.description 获取用户有权限的菜单列表。不填用户，返回应用所有菜单
     * @mtapi.category 菜单
     * @mtapi.param appkey 应用标识
     * @mtapi.param userId 用户ID，可选
     * @mtapi.result 返回用户有权限的菜单列表，userId为空返回应用所有菜单 Menu
     * @mtapi.requestExample /api/menus?appkey=mtsg&userId=7655
     * @mtapi.responseExample {"data":[{"title":"权限管理","id":100226,"type":1,"url":"/test","code":"mtupm_42fa8c6fbe25e7f6a53607404c71abeb","sort":0,"menus":[{"title":"授予用户角色","id":100380,"type":1,"url":"/userRole/singleConf","code":"upm-userRole","sort":0,"menus":null,"createTime":1375350097},{"title":"授予多用户角色","id":102209,"type":1,"url":"/userRole/multiConf","code":"upm-userRole","sort":1,"menus":null,"createTime":1403839718}]}]}
     */
    @RequestMapping(value = "menus", method = RequestMethod.GET)
    public
    @ResponseBody
    Response menus(String appkey, String userId) {
        if (StringUtils.isBlank(appkey)) {
            return ApiUtil.errorResponse(MtError.paramError.getCode(), "appkey is blank");
        }
        List<Menu> menus;
        if (userId == null) {
            menus = authService.appMenus(appkey);
        } else {
            menus = authService.menus(appkey, userId);
        }
        LOG.info(Joiner.on(",").useForNull("null").join(appkey, userId, menus.size()));
        return ApiUtil.response(menus);
    }

    /**
     * @mtapi.name 获取用户应用内角色code
     * @mtapi.description 获得应用内用户所有角色code
     * @mtapi.category 角色
     * @mtapi.param appkey 应用标识
     * @mtapi.param userId 用户ID
     * @mtapi.requestExample /api/roles/7655/mtgis
     * @mtapi.responseExample {"data":[{"roleId":100023,"roleName":"商圈编辑","orgIds":[1012]}]}
     * @mtapi.errorExample {"data":[]}
     */
    @RequestMapping(value = "roles/{userId}/{appkey}", method = RequestMethod.GET)
    @ResponseBody
    public Response userRoles(@PathVariable String userId, @PathVariable String appkey) {
        List<String> roles = authService.selectRoleCodes(appkey, userId);
        LOG.info(Joiner.on(",").useForNull("null").join(appkey, userId));
        return ApiUtil.response(roles);
    }

    @RequestMapping(value = "user/getUserById", method = RequestMethod.GET)
    @ResponseBody
    public Response getUserById(String id) {
        AclUser aclUser = userService.getById(id);
        User user = new User();
        user.setId(aclUser.getId());
        user.setLogin(aclUser.getLoginName());
        user.setName(aclUser.getName());
        user.setType(aclUser.getType());
        return ApiUtil.response(user);
    }

    @RequestMapping(value = "user/getUserByLoginName", method = RequestMethod.GET)
    @ResponseBody
    public Response getUserByLoginName(String loginName) {
        AclUser aclUser = userService.getByLoginName(loginName);
        User user = new User();
        user.setId(aclUser.getId());
        user.setLogin(aclUser.getLoginName());
        user.setName(aclUser.getName());
        user.setType(aclUser.getType());
        return ApiUtil.response(user);
    }

    @RequestMapping(value = "user/getAllUser", method = RequestMethod.GET)
    @ResponseBody
    public Response getUserByLoginName(Integer type, Long lastUpdateTime) {
        List<AclUser> aclUsers = userService.getAllUserList(type, lastUpdateTime);
        List<User> users = new ArrayList<User>();
        for(AclUser aclUser: aclUsers){
            User user = new User();
            user.setId(aclUser.getId());
            user.setLogin(aclUser.getLoginName());
            user.setName(aclUser.getName());
            user.setType(aclUser.getType());
            users.add(user);
        }

        return ApiUtil.response(users);
    }

    @RequestMapping(value = "userApps", method = RequestMethod.GET)
    @ResponseBody
    public Response userApps(String userId) {
        List<AclApplication> aclApplications = upmService.selectAppsByUser(userId);
        return ApiUtil.response(aclApplications);
    }
}
