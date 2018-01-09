package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.api.service.RoleService;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.Response;
import com.sinochem.yunlian.upm.api.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}
