package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.service.RoleService;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/01/05 下午5:13
 */
@RestController
@RequestMapping("inner/api")
public class UserV2Controller {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "users/list", method = RequestMethod.GET)
    public Response userList(
            String name,
            @RequestParam(defaultValue = "1", required = true) Integer page,
            @RequestParam(defaultValue = "20", required = true) int rows) {
        PageInfo pageInfo = userService.getUserListByCriteria(name, page, rows);
        return Response.succeed(pageInfo);
    }
    @RequestMapping(value = "user/{id}",method = RequestMethod.GET)
    public Response getUser(@PathVariable(value = "id") String id){
        UserByIdVo user = userService.getUserById(id);
        if( !StringUtils.isEmpty(user)){
            return Response.succeed(user);
        }
        return Response.fail("查询无结果");
    }
    @RequestMapping(value = "user/update" , method = RequestMethod.POST)
    public Response updateUser(@RequestBody AclUser aclUser){
        int a = userService.updateUser(aclUser);
        if(a>0){
            return Response.succeed();
        }
        return Response.fail("修改失败！");
    }


    /**
     * 获取角色下的用户
     * @param roleId
     * @return
     */
    @RequestMapping(value = "role/user", method = RequestMethod.GET)
    public Response getUserByRole(String roleId) {

        List<String> userIds = roleService.getUserIds(roleId);
        List<UserVo> vos = userService.getByIds(userIds);
        return Response.succeed().put("users", vos);

    }
    @RequestMapping(value = "user/mobile/check" , method = RequestMethod.POST)
    public Response userMobileCheck( String id , String newMobile){
        boolean b=userService.mobileCheck(id , newMobile);
        if(b){
            return  Response.succeed();
        }
        return Response.fail("您输入的号码不符");
    }


}
