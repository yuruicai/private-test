package com.sinochem.yunlian.upm.admin.service;

import com.sinochem.yunlian.upm.admin.constant.RolePermissionRltStatus;
import com.sinochem.yunlian.upm.admin.domain.AclRolePermissionRltExample;
import com.sinochem.yunlian.upm.admin.mapper.AclRolePermissionRltMapper;
import com.sinochem.yunlian.upm.util.UpmCacheUtil;
import com.sinochem.yunlian.upm.admin.domain.AclRolePermissionRlt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-05-13
 */
@Service
public class RolePermissionRltService {

    private static final Logger logger = LoggerFactory.getLogger(RolePermissionRltService.class);

    @Resource
    private AclRolePermissionRltMapper aclRolePermissionRltMapper;

    public void deleteRltByRoleId(String id) {
        AclRolePermissionRltExample example = new AclRolePermissionRltExample();
        example.or().andRoleIdEqualTo(id)
                .andStatusEqualTo((short) RolePermissionRltStatus.ACTIVE.getIndex());
        AclRolePermissionRlt rlt = new AclRolePermissionRlt();
        rlt.setUpdateTime(new Date());
        rlt.setStatus((short) RolePermissionRltStatus.DELETE.getIndex());
        rlt.setOperatorId(UpmCacheUtil.getCurrentUserId());
        aclRolePermissionRltMapper.updateByExampleSelective(rlt, example);
    }

    public AclRolePermissionRlt getByRoleIdAndPermissionId(String roleId, String permissionId) {
        AclRolePermissionRltExample aclRolePermissionRltExample = new AclRolePermissionRltExample();
        aclRolePermissionRltExample.or().andRoleIdEqualTo(roleId).andPermissionIdEqualTo(permissionId)
                .andStatusEqualTo((short) RolePermissionRltStatus.ACTIVE.getIndex());
        List<AclRolePermissionRlt> rlts = aclRolePermissionRltMapper.selectByExample(aclRolePermissionRltExample);
        return rlts.size() > 0 ? rlts.get(0) : null;
    }

    public void addPermission(String roleId, String[] ids) {
        AclRolePermissionRltExample example =new AclRolePermissionRltExample();
        example.or().andRoleIdEqualTo(roleId);
        aclRolePermissionRltMapper.deleteByExample(example);
        for (String id : ids) {
            addRlt(roleId, id);
        }
    }

    public void addRlt(String roleId, String permissionId) {
        AclRolePermissionRlt rlt = getByRoleIdAndPermissionId(roleId, permissionId);
        if(rlt != null) {      //check exists before insert
           return;
        }
        rlt = new AclRolePermissionRlt();
        rlt.setId(UUID.randomUUID().toString());
        rlt.setRoleId(roleId);
        rlt.setPermissionId(permissionId);
        rlt.setStatus((short) RolePermissionRltStatus.ACTIVE.getIndex());
        Date now = new Date();
        rlt.setCreateTime(now);
        rlt.setUpdateTime(now);
        rlt.setOperatorId(UpmCacheUtil.getCurrentUserId());
        aclRolePermissionRltMapper.insert(rlt);
    }

    public void deleteBy(String roleId, String permissionId) {
        AclRolePermissionRltExample arpre = new AclRolePermissionRltExample();
        arpre.or().andPermissionIdEqualTo(permissionId).andRoleIdEqualTo(roleId)
                .andStatusEqualTo((short) RolePermissionRltStatus.ACTIVE.getIndex());
        AclRolePermissionRlt aclRolePermissionRlt = new AclRolePermissionRlt();
        aclRolePermissionRlt.setUpdateTime(new Date());
        aclRolePermissionRlt.setStatus((short) RolePermissionRltStatus.DELETE.getIndex());
        aclRolePermissionRlt.setOperatorId(UpmCacheUtil.getCurrentUserId());
        aclRolePermissionRltMapper.updateByExampleSelective(aclRolePermissionRlt, arpre);
    }

    public AclRolePermissionRlt getById(String id) {
        if(id == null) {
            return null;
        }
        return aclRolePermissionRltMapper.selectByPrimaryKey(id);
    }
}
