package com.sinochem.yunlian.upm.api.service.impl;

import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.AclMenuMapper;
import com.sinochem.yunlian.upm.admin.mapper.AclRolePermissionRltMapper;
import com.sinochem.yunlian.upm.admin.mapper.OperationMapper;
import com.sinochem.yunlian.upm.admin.mapper.ResourceDataMapper;
import com.sinochem.yunlian.upm.api.service.PermissionService;
import com.sinochem.yunlian.upm.api.service.RoleService;
import com.sinochem.yunlian.upm.api.util.StringUtils;
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
    @Autowired
    private OperationMapper operationDao;
    @Autowired
    private ResourceDataMapper resourceDataDao;

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

    @Override
    public List<Operation> selectElement(String appId,String typeId) {
        OperationExample example = new OperationExample();
        OperationExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        criteria.andTypeEqualTo(new Byte(typeId));
        return operationDao.selectByExample(example);
    }

    @Override
    public String randomStr() {
        return StringUtils.uuid();
    }

    @Override
    public int saveOperation(Operation operation) {
        try{
            //如果数据库有对应的数据id执行更新操作
            if(!org.springframework.util.StringUtils.isEmpty(operation.getId())){
                operationDao.updateByPrimaryKey(operation);
            }else{
                //如果数据库没有对应的数据id执行插入操作
                operationDao.insert(operation);
            }
        }catch(Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public int updateStatus(String id) {
        try{
            Operation operation = operationDao.selectByPrimaryKey(Integer.parseInt(id));
            operation.setStatus(new Byte(0+""));
            operationDao.updateByPrimaryKey(operation);
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public Operation getOne(String id) {
        return operationDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<ResourceData> selectData(String appId) {
        ResourceDataExample example = new ResourceDataExample();
        ResourceDataExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        criteria.andStatusEqualTo(new Byte(1+""));
        return resourceDataDao.selectByExample(example);
    }

    @Override
    public int updateStatusOfData(String id) {
        try{
            ResourceData resourceData = resourceDataDao.selectByPrimaryKey(Integer.parseInt(id));
            resourceData.setStatus(new Byte(0+""));
            resourceDataDao.updateByPrimaryKey(resourceData);
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public int saveData(ResourceData resourceData) {

        try{
            //如果数据库有对应的数据id执行更新操作
            if(!org.springframework.util.StringUtils.isEmpty(resourceData.getId())){
                resourceDataDao.updateByPrimaryKey(resourceData);
            }else{
                //如果数据库没有对应的数据id执行插入操作
                resourceDataDao.insert(resourceData);
            }
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public ResourceData getDataOne(String code) {
        ResourceDataExample example = new ResourceDataExample();
        ResourceDataExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code);
        List<ResourceData> resourceDatas = resourceDataDao.selectByExample(example);
        return resourceDatas.get(0);
    }
}
