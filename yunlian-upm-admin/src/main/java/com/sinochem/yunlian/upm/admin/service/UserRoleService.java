package com.sinochem.yunlian.upm.admin.service;

import com.sinochem.yunlian.upm.admin.bean.UserBean;
import com.sinochem.yunlian.upm.admin.constant.CommonStatus;
import com.sinochem.yunlian.upm.admin.constant.UserRoleType;
import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.AclRoleInstanceContextMapper;
import com.sinochem.yunlian.upm.util.ObjectUtil;
import com.sinochem.yunlian.upm.util.StringUtil;
import com.sinochem.yunlian.upm.util.ThreadCacheUtil;
import com.sinochem.yunlian.upm.util.UpmCacheUtil;
import com.google.common.collect.Lists;
import com.sinochem.yunlian.upm.admin.mapper.AclUserRoleRltMapper;
import com.sinochem.yunlian.upm.admin.mapper.AclUserRoleSourceMapper;
import com.sinochem.yunlian.upm.admin.mapper.UpmMapper;
import com.sinochem.yunlian.upm.admin.bean.RoleConfBean;
import com.sinochem.yunlian.upm.admin.bean.UserRoleBean;
import com.sinochem.yunlian.upm.admin.constant.RoleStatus;
import com.sinochem.yunlian.upm.admin.constant.UserRoleSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户授权
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-06
 */
@Service
public class UserRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(UserRoleService.class);

    @Resource
    private AclUserRoleRltMapper aclUserRoleRltMapper;

    @Resource
    private AclUserRoleSourceMapper aclUserRoleSourceMapper;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private RoleInstanceService roleInstanceService;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private UpmMapper upmMapper;

    @Resource
    private AclRoleInstanceContextMapper ricMapper;

    public String addRltByUserAndRole(String userId, String roleId, String sourceId,
                                      Integer sourceType) {
        AclUserRoleRlt rlt = getRltByUserAndRole(userId, roleId);
        if (rlt == null) {
            String contextId = addRltAndSource(userId, roleId, sourceId, sourceType);
            return contextId;
        } else {
            AclUserRoleSource source = getSourceByRlt(rlt.getId(), sourceId, sourceType);
            if (source == null) {
                addSource(rlt.getId(), sourceId, sourceType);
            }
        }
        return rlt.getId();
    }

    public void deleteRltByUserAndRole(String userId, String roleId) {
        deleteRltByUserAndRole(userId, roleId, null, null);
    }

    public void deleteRltByUserAndRole(String userId, String roleId, String sourceId,
                                       Integer sourceType) {
        AclUserRoleRlt rlt = getRltByUserAndRole(userId, roleId);

        if (rlt == null) {
            return;
        } else {
            if (StringUtil.isBlank(sourceId)) {
                deleteAllSourceByRltId(rlt.getId());
                deleteRltByRltId(rlt.getId());
                roleInstanceService.deleteByRltId(rlt.getId());
                return;
            }
            AclUserRoleSource source = getSourceByRlt(rlt.getId(), sourceId, sourceType);
            if (source == null) {
                return;
            } else {
                deleteSource(rlt.getId(), sourceId, sourceType);
            }
        }
    }

    public AclUserRoleRlt getRltByUserAndRole(String userId, String roleId) {
        AclUserRoleRltExample rltExample = new AclUserRoleRltExample();
        AclUserRoleRltExample.Criteria criteria = rltExample.or();
        criteria.andUserIdEqualTo(userId).andRoleIdEqualTo(roleId)
                .andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        List<AclUserRoleRlt> rlts = aclUserRoleRltMapper.selectByExample(rltExample);
        return rlts.size() > 0 ? rlts.get(0) : null;
    }

    public String getRltIdByUserAndRole(String userId, String roleId) {
        AclUserRoleRlt rlt = getRltByUserAndRole(userId, roleId);
        return null == rlt ? null : rlt.getId();
    }

    public AclUserRoleSource getSourceByRlt(String rltId, String sourceId, Integer sourceType) {
        AclUserRoleSourceExample sourceExample = new AclUserRoleSourceExample();
        AclUserRoleSourceExample.Criteria criteria = sourceExample.or();
        criteria.andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex())
                .andAclUserRoleRltidEqualTo(rltId);
        criteria.andSourceTypeEqualTo(sourceType.shortValue()).andSourceRltIdEqualTo(sourceId);
        List<AclUserRoleSource> sources = aclUserRoleSourceMapper.selectByExample(sourceExample);
        return sources.size() > 0 ? sources.get(0) : null;
    }

    public String addRltAndSource(String userId, String roleId, String sourceId,
                                  Integer sourceType) {
        AclUserRoleRlt aclUserRoleRlt = new AclUserRoleRlt();
        aclUserRoleRlt.setId(UUID.randomUUID().toString());
        aclUserRoleRlt.setRoleId(roleId);
        aclUserRoleRlt.setUserId(userId);
        aclUserRoleRlt.setStatus((short) CommonStatus.ACTIVE.getIndex());
        aclUserRoleRlt.setCreateTime(new Date());
        aclUserRoleRlt.setUpdateTime(new Date());
        aclUserRoleRlt.setRltType((short) UserRoleType.OWN.getIndex());
        aclUserRoleRlt.setOperatorId(sourceId);
        aclUserRoleRltMapper.insertSelective(aclUserRoleRlt);
        addSource(aclUserRoleRlt.getId(), sourceId, sourceType);

        return aclUserRoleRlt.getId();
    }

    public void addSource(String rltId, String sourceId, Integer sourceType) {
        AclUserRoleSource aclUserRoleSource = new AclUserRoleSource();
        aclUserRoleSource.setId(UUID.randomUUID().toString());
        aclUserRoleSource.setAclUserRoleRltid(rltId);
        aclUserRoleSource.setStatus((short) CommonStatus.ACTIVE.getIndex());
        aclUserRoleSource.setSourceType(sourceType.shortValue());
        aclUserRoleSource.setSourceRltId(sourceId);
        aclUserRoleSourceMapper.insertSelective(aclUserRoleSource);
    }

    public void deleteSource(String rltId, String sourceId, Integer sourceType) {
        AclUserRoleSource source = getSourceByRlt(rltId, sourceId, sourceType);
        source.setStatus((short) CommonStatus.DELETE.getIndex());
        aclUserRoleSourceMapper.updateByPrimaryKeySelective(source);
        // 删除没有任何来源的关系
        if (getAllSourceByRlt(rltId).size() == 0) {
            deleteRltByRltId(rltId);
        }
    }

    public List<AclUserRoleSource> getAllSourceByRlt(String rltId) {
        AclUserRoleSourceExample sourceExample = new AclUserRoleSourceExample();
        sourceExample.or().andAclUserRoleRltidEqualTo(rltId)
                .andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        return aclUserRoleSourceMapper.selectByExample(sourceExample);
    }

    public void deleteRltByRltId(String rltId) {
        AclUserRoleRltExample rltExample = new AclUserRoleRltExample();
        rltExample.or().andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex())
                .andIdEqualTo(rltId);
        AclUserRoleRlt aclUserRoleRlt = new AclUserRoleRlt();
        aclUserRoleRlt.setUpdateTime(new Date());
        aclUserRoleRlt.setStatus((short) CommonStatus.DELETE.getIndex());
        aclUserRoleRlt.setOperatorId(UpmCacheUtil.getCurrentUserId());
        aclUserRoleRltMapper.updateByExampleSelective(aclUserRoleRlt, rltExample);
    }

    // replaced by getRltsListByUserId
    @Deprecated
    public List<AclUserRoleRlt> getRltsListByUserIdAndType(String userId, Integer type) {
        AclUserRoleRltExample rltExample = new AclUserRoleRltExample();
        AclUserRoleRltExample.Criteria criteria = rltExample.or();
        criteria.andUserIdEqualTo(userId).andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        if (type != null) {
            criteria.andRltTypeEqualTo(type.shortValue());
        }
        return aclUserRoleRltMapper.selectByExample(rltExample);
    }

    public List<AclUserRoleRlt> getRltsListByUserId(String userId) {
        AclUserRoleRltExample example = new AclUserRoleRltExample();
        example.or().andUserIdEqualTo(userId).andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        return aclUserRoleRltMapper.selectByExample(example);
    }

    public Boolean isHaveRole(String userId, String code) {
        AclRole role = roleService.getByCode(code);
        if (null == role) {
            return Boolean.FALSE;
        }
        String roleId = role.getId();
        AclUserRoleRlt rlt = getRltByUserAndRole(userId, roleId);
        return null == rlt ? Boolean.FALSE : Boolean.TRUE;
    }

    public void saveUserRole(UserRoleBean userRoleBean) {
        if (userRoleBean.getUserIds() == null) {
            return;
        }
        for (String userId : userRoleBean.getUserIds()) {
            for (RoleConfBean roleConfBean : userRoleBean.getRoleConfBeans()) {
                if (roleConfBean.getStatus().equals(CommonStatus.ACTIVE.getIndex())
                        || roleConfBean.getStatus().equals(CommonStatus.MODIFY.getIndex())) {
                    addRltByUserAndRole(userId, roleConfBean.getId(),
                            UpmCacheUtil.getCurrentUserId(),
                            UserRoleSourceType.MANUAL.getIndex());
                }
            }
        }
    }

    public Collection<RoleConfBean> getRoleConfsByUserId(String userId, String appId) {
        List<AclUserRoleRlt> rlts = getRltsListByUserIdAndType(userId, null);
        Map<String, RoleConfBean> confMap = new HashMap<String, RoleConfBean>();
        if (appId == null) {
            appId = (String) ThreadCacheUtil.getData("appId");
        }
        for (AclUserRoleRlt rlt : rlts) {
            AclRole aclRole = roleService.getById(rlt.getRoleId());
            if (aclRole == null || !aclRole.getApplicationId().equals(appId)
                    || !aclRole.getStatus().equals((short) RoleStatus.ACTIVE.getIndex())) {
                continue;
            }
            if (!confMap.containsKey(rlt.getRoleId())) {
                RoleConfBean conf = new RoleConfBean();
                conf.setId(rlt.getRoleId());
                conf.setCode(aclRole.getCode());
                conf.setStatus(0);
                String name = aclRole.getName();
                if (name.isEmpty()) {
                    continue;
                }
                conf.setRoleName(name);
                confMap.put(rlt.getRoleId(), conf);
            }
            confMap.get(rlt.getRoleId()).setRoleSource(getSourceDescByRltId(rlt.getId()));
        }
        return confMap.values();
    }

    public String getSourceDescByRltId(String rltId) {
        StringBuilder sb = new StringBuilder();
        String sourceName = "";
        List<AclUserRoleSource> sources = getAllSourceByRlt(rltId);
        for (AclUserRoleSource source : sources) {
            if (source.getSourceType().equals((short) UserRoleSourceType.ORGAUTO.getIndex())) {
                sb.append(UserRoleSourceType.ORGAUTO.getName());
                sb.append("<br />");
            }
            if (source.getSourceType().equals((short) UserRoleSourceType.APP.getIndex())) {
                sb.append(UserRoleSourceType.APP.getName()).append("<br />");
            }
            if (source.getSourceType().equals((short) UserRoleSourceType.PHOTO.getIndex())) {
                sb.append(UserRoleSourceType.PHOTO.getName()).append("<br />");
            }
            if (source.getSourceType().equals((short) UserRoleSourceType.CONTACT_CHARGE.getIndex())) {
                sb.append(UserRoleSourceType.CONTACT_CHARGE.getName());
                sb.append("<br />");
            }
            if (source.getSourceType().equals((short) UserRoleSourceType.MANUAL.getIndex())) {
                sb.append(UserRoleSourceType.MANUAL.getName());
                sb.append("<br />");
            }
        }
        String desc = sb.toString();
        if (desc.equals("")) {
            desc = "数据导入";
        }
        return desc;
    }

    public void deleteAllSourceByRltId(String rltId) {
        AclUserRoleSourceExample sourceExample = new AclUserRoleSourceExample();
        sourceExample.or().andAclUserRoleRltidEqualTo(rltId)
                .andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        AclUserRoleSource source = new AclUserRoleSource();
        source.setStatus((short) CommonStatus.DELETE.getIndex());
        aclUserRoleSourceMapper.updateByExampleSelective(source, sourceExample);
    }

    public void deleteAllByRoleId(String roleId) {
        List<AclUserRoleRlt> rlts = getRltsListByRoleId(roleId);
        for (AclUserRoleRlt rlt : rlts) {
            deleteAllSourceByRltId(rlt.getId());
            deleteRlt(rlt);
        }
    }

    public List<AclUserRoleRlt> getRltsListByRoleId(String roleId) {
        AclUserRoleRltExample aclUserRoleRltExample = new AclUserRoleRltExample();
        aclUserRoleRltExample.or().andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex())
                .andRoleIdEqualTo(roleId);
        return aclUserRoleRltMapper.selectByExample(aclUserRoleRltExample);
    }

    public void deleteRlt(AclUserRoleRlt rlt) {
        rlt.setStatus((short) CommonStatus.DELETE.getIndex());
        rlt.setUpdateTime(new Date());
        aclUserRoleRltMapper.updateByPrimaryKeySelective(rlt);
    }

    public List<AclUser> getUsersHaveRole(String roleId) {
        AclUserRoleRltExample rltExample = new AclUserRoleRltExample();
        AclUserRoleRltExample.Criteria criteria = rltExample.or();
        criteria.andRoleIdEqualTo(roleId).andRltTypeEqualTo((short) UserRoleType.OWN.getIndex())
                .andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        List<AclUserRoleRlt> rlts = aclUserRoleRltMapper.selectByExample(rltExample);
        Set<String> ids = new HashSet<String>();
        for (AclUserRoleRlt rlt : rlts) {
            ids.add(rlt.getUserId());
        }
        return userService.getByIds(Lists.newArrayList(ids));
    }

    public AclUserRoleRlt getById(String id) {
        if (id == null) {
            return null;
        }
        return aclUserRoleRltMapper.selectByPrimaryKey(id);
    }

    public List<UserBean> makeUserBean(List<AclUser> aclUsers) {
        List<UserBean> userBeans = ObjectUtil.assembleList2NewList(aclUsers, UserBean.class);
        return userBeans;
    }

    public void addByAppAuto(String userId) {
        Set<String> ids = applicationService.getAllDefaultRoles();
        for (String id : ids) {
             addRltByUserAndRole(userId, id, id,UserRoleSourceType.APP.getIndex());
        }
    }
}
