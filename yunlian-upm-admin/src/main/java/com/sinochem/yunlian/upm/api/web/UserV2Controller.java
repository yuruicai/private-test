package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.service.RoleService;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.util.EncryptionUtils;
import com.sinochem.yunlian.upm.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/01/05 下午5:13
 */
@RestController
@RequestMapping("inner/api")
@Slf4j
public class UserV2Controller {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    private String roleId;
    private String name;

    /**
     * 获取用户列表的方法
     * @param name
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "users/list", method = RequestMethod.GET)
    public Response userList(
            String name,
            @RequestParam(defaultValue = "1", required = true) Integer page,
            @RequestParam(defaultValue = "20", required = true) int rows) {
        PageInfo pageInfo = userService.getUserListByCriteria(name, page, rows);
        return Response.succeed(pageInfo);
    }

    /**
     * 查询一个用户
     * @param id
     * @return
     */
    @RequestMapping(value = "user/{id}",method = RequestMethod.GET)
    public Response getUser(@PathVariable(value = "id") String id){
        UserByIdVo user = userService.getUserById(id);
        if( !StringUtils.isEmpty(user)){
            return Response.succeed(user);
        }
        return Response.fail("查询无结果");
    }

    /**
     * 修改用户信息
     * @param aclUser
     * @return
     */
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

    /**
     * 电话号码校验
     * @param id
     * @param newMobile
     * @return
     */
    @RequestMapping(value = "user/mobile/check" , method = RequestMethod.POST)
    public Response userMobileCheck( String id , String newMobile){
        boolean b=userService.mobileCheck(id , newMobile);
        if(b){
            return  Response.succeed();
        }
        return Response.fail("您输入的号码不符");
    }

    /**
     * 密码重置
     * @param id
     * @param oldPassword
     * @param newPassword1
     * @param newPassword2
     * @return
     */
    @RequestMapping(value = "user/password/rest",method = RequestMethod.POST)
    public Response modPassword(String id ,String oldPassword, String newPassword1, String newPassword2){
        if(StringUtils.isEmpty(id)){
            log.info("用户id不能为空！  ， id={}",id);
            return Response.fail("用户id不能为空！");
        }
        if(StringUtils.isEmpty(oldPassword)){
            log.info("用户原密码不能为空！  ， oldPassword={}",oldPassword);
            return Response.fail("用户原密码不能为空！");
        }
        if(StringUtils.isEmpty(newPassword1)){
            log.info("用户新密码不能为空！  ， newPassword1={}",newPassword1);
            return  Response.fail("用户新密码不能为空！");
        }
        if(StringUtils.isEmpty(newPassword2)){
            log.info("用户确认密码不能为空！  ， newPassword2={}",newPassword2);
            return Response.fail("用户确认密码不能为空！");
        }
        if( !newPassword1.equals(newPassword2)){
            log.info("两次新密码不一致！");
            return   Response.fail("两次新密码不一致！");
        }
        oldPassword = EncryptionUtils.getBase64(oldPassword);
        newPassword1 = EncryptionUtils.getBase64(newPassword1);
        newPassword2 = EncryptionUtils.getBase64(newPassword2);
        boolean a = userService.restPassword(id,oldPassword,newPassword1,newPassword2);
        if(!a){
          return  Response.fail("请您按要求输入！");
        }
        return Response.succeed();
    }
    @RequestMapping(value = "user/role",method = RequestMethod.GET)
    public Response getRoleList( String roleId ,String name){
        if(StringUtils.isEmpty(roleId)){
            log.info("角色id為空");
            return Response.fail("参数异常，角色id為空！");
        }
        List<String> userIds = roleService.getUserIds(roleId);
        List<UserVo> userVo=userService.selectUserBaseUserIds(userIds ,name);
    return Response.succeed().put("users",userVo);
}

}
