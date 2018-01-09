package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclRole;
import com.sinochem.yunlian.upm.api.service.RoleService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.Response;
import com.sinochem.yunlian.upm.api.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangyang 对运营平台开放的接口
 * @Description:
 * @date 2018/01/08 下午4:07
 */
@RestController
@RequestMapping("/inner/api/role")
public class RoleInnerController {

    @Autowired
    private RoleService roleService;


    @RequestMapping(value = "", method = RequestMethod.POST)
    public Response add(AclRole role) {
        roleService.insert(role);
        return Response.succeed();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Response list(String name, @RequestParam(value = "curPage", defaultValue = "1") Integer curPage,
                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        PageInfo<RoleVo> pageInfo = roleService.getByCodeOrName(name, curPage, pageSize);
        return Response.succeed(pageInfo);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Response getOne(@PathVariable("id") String id) {
        AclRole role = roleService.getById(id);
        return Response.succeed(role);
    }

    @RequestMapping(value = "modification", method = RequestMethod.POST)
    public Response modify(AclRole role) {
        roleService.modifyById(role);
        return Response.succeed();
    }
}
