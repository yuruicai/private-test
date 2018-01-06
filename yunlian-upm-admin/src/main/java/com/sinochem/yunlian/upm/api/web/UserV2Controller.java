package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.bean.UserListBean;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.Response;
import com.sinochem.yunlian.upm.api.vo.UserVo;
import com.sinochem.yunlian.upm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

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
            @RequestParam(defaultValue = "",required = false) String name,
            @RequestParam(defaultValue = "1",required = true) Integer  page,
            @RequestParam(defaultValue = "20",required = true) int rows ){
        UserVo userVo = userService.getUserListByCriteria( name , page,  rows);
        return Response.succeed(userVo);
    }

}
