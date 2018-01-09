package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.Response;
import com.sinochem.yunlian.upm.api.vo.UserByIdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/01/05 下午5:13
 */
@RestController
@RequestMapping("inner/api/users")
public class UserV2Controller {
    @Autowired
    private UserService userService;

    @RequestMapping("list")
    public Response userList(
             String name,
            @RequestParam(defaultValue = "1",required = true) Integer  page,
            @RequestParam(defaultValue = "20",required = true) int rows ){
        PageInfo pageInfo = userService.getUserListByCriteria( name , page,  rows);
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


}
