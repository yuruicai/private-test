package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.Response;
import com.sinochem.yunlian.upm.api.vo.UserByIdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangyang
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/01/05 下午5:13
 */
@Controller
@RequestMapping("inner/api/users")
public class UserV2Controller {
    @Autowired
    private UserService userService;

    @RequestMapping("list")
    @ResponseBody
    public Response userList(
             String name,
            @RequestParam(defaultValue = "1",required = true) Integer  page,
            @RequestParam(defaultValue = "20",required = true) int rows ){
        PageInfo pageInfo = userService.getUserListByCriteria( name , page,  rows);
        return Response.succeed(pageInfo);
    }
    @RequestMapping("getUser/{id}")
    @ResponseBody
    public UserByIdVo getUser(@PathVariable(value = "id") String id){
        UserByIdVo user = userService.getUserById(id);
        return  user;
    }

}
