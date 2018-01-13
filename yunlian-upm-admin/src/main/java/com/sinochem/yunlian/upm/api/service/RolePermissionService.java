package com.sinochem.yunlian.upm.api.service;

import java.util.List;
import java.util.Map;

/**
 * @author gaowei
 * @Description:
 * @date 2018/1/11.10:52
 */
public interface RolePermissionService {

    //根据角色id查询对应角色下的所有的应用id，菜单id，元素id，操作id，数据id
    Map getAll(String roleId);

    //根据传入的数据id，和数据类型获取对应资源详情信息
    List getOne(String id, String typeId);

    //保存当前角色下勾选的资源
    int save(Map map);
}
