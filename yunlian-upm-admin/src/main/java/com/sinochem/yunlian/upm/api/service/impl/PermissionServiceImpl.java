package com.sinochem.yunlian.upm.api.service.impl;

import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.AclMenuMapper;
import com.sinochem.yunlian.upm.admin.mapper.AclRolePermissionRltMapper;
import com.sinochem.yunlian.upm.api.service.PermissionService;
import com.sinochem.yunlian.upm.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/08 下午5:55
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private static final short STATUS = 0;

    @Autowired
    private RoleService roleService;
    @Autowired
    private AclRolePermissionRltMapper rolePermissionDao;
    @Autowired
    private AclMenuMapper menuDao;

    /**
     * 查询用户权限
     *
     * @param appKey
     * @param userId
     * @return
     */
    @Override
    public List<AclMenu> getByUserIdAndAppKey(String appKey, String userId) {

        List<String> menuIds = getMenuIds(appKey, userId);

        AclMenuExample menuExample = new AclMenuExample();
        AclMenuExample.Criteria menuExampleCriteria = menuExample.createCriteria();
        menuExampleCriteria.andStatusEqualTo(STATUS).andIdIn(menuIds);
        //todo 排序
        return menuDao.selectByExample(menuExample);
    }


    /**
     * 用户在某个应用中的顶级权限资源
     * @param appKey
     * @param userId
     * @return
     */
    @Override
    public List<AclMenu> getFirstLevelByUserIdAndAppKey(String appKey, String userId) {

        List<String> menuIds = getMenuIds(appKey, userId);
        AclMenuExample menuExample = new AclMenuExample();
        AclMenuExample.Criteria menuExampleCriteria = menuExample.createCriteria();
        menuExampleCriteria.andStatusEqualTo(STATUS).andIdIn(menuIds).andParentIdIsNull();
        menuExample.setOrderByClause("sort_num desc");
        //todo 排序
        return menuDao.selectByExample(menuExample);

    }

    private List<String> getMenuIds(String appKey, String userId) {

        List<AclRole> roles = roleService.getByUserIdAndAppKey(appKey, userId);
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.EMPTY_LIST;
        }
        List<String> roleIds = roles.stream().map(r -> r.getId()).collect(Collectors.toList());
        AclRolePermissionRltExample example = new AclRolePermissionRltExample();
        AclRolePermissionRltExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(STATUS).andRoleIdIn(roleIds);
        List<AclRolePermissionRlt> rolePermission = rolePermissionDao.selectByExample(example);
        if (CollectionUtils.isEmpty(rolePermission)) {
            return Collections.EMPTY_LIST;
        }
        return rolePermission.stream().map(r -> r.getPermissionId()).collect(Collectors.toList());

    }
}
