package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.api.service.RoleService;
import com.sinochem.yunlian.upm.api.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author huangyang
 * @Description: 对车，船，等应用的接口
 * @date 2018/01/08 下午4:54
 */

@RestController
@RequestMapping("api/user")
public class UserApiController {
    @Autowired
    private RoleService roleService;

    /**
     * 获取用户某应用下的角色
     * @param appKey
     * @param userId
     * @return
     */
    @RequestMapping(value = "role",method = RequestMethod.GET)
    public Response getRoles(String appKey,String userId){
        List<AclRole> roles = roleService.getByUserIdAndAppKey(appKey, userId);
        return Response.succeed().put("roles",roles);
    }

    @RequestMapping(value = "permission",method = RequestMethod.GET)
    public Response getPermissions(String appKey,String userId){
        List<AclRole> roles = roleService.getByUserIdAndAppKey(appKey, userId);
        return Response.succeed().put("permissions",roles);
    }






}
