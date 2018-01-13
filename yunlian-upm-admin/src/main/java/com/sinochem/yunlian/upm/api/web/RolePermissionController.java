package com.sinochem.yunlian.upm.api.web;

import com.sinochem.yunlian.upm.admin.domain.AclRoleInstanceContext;
import com.sinochem.yunlian.upm.admin.domain.AclRoleInstanceContextExample;
import com.sinochem.yunlian.upm.admin.domain.RoleMenu;
import com.sinochem.yunlian.upm.admin.domain.RoleMenuExample;
import com.sinochem.yunlian.upm.admin.mapper.AclRoleInstanceContextMapper;
import com.sinochem.yunlian.upm.admin.mapper.RoleMenuMapper;
import com.sinochem.yunlian.upm.admin.mapper.RoleOperationMapper;
import com.sinochem.yunlian.upm.api.service.RolePermissionService;
import com.sinochem.yunlian.upm.api.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaowei
 * @Description: 角色权限分配 controller
 * @date 2018/1/11.10:52
 */
@Slf4j
@RestController
@RequestMapping("inner/api")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 根据角色id查询对应角色下的所有的应用id，菜单id，元素id，操作id，数据id
     * @param roleId 角色id
     * @return
     */
    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    public Map getAll(String roleId) {
        Map map = rolePermissionService.getAll(roleId);
        return map;
    }

    /**
     * 获取资源(菜单、元素、操作、数据)详情
     * 根据传入的数据id，和数据类型获取对应资源详情信息
     * @param id        数据id
     * @param typeId    类型：0：菜单  1：元素  2：操作  3：数据
     * @return
     */
    @RequestMapping(value = "getDetail", method = RequestMethod.GET)
    public List getOne(String id,String typeId) {
        List list = rolePermissionService.getOne(id,typeId);
        return list;
    }

    /**
     * 保存当前角色下勾选的资源
     * map封装格式：
     *  <"roleid":"">
     *  <"applicationIdList":["1","2"]>
     *  <"menuIdList":["1","2"]>
     *  <"operationIdList":["1","2"]>
     *  <"dataIdList":["1","2"]>
     * @param map
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Response save(@RequestBody Map map){
        int i = rolePermissionService.save(map);
        return i==1?Response.succeed():Response.fail("保存失败");
    }

}
