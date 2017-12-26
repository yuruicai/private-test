package com.sinochem.yunlian.upm.admin.service;

import com.alibaba.fastjson.JSON;
import com.sinochem.yunlian.upm.admin.bean.RoleBean;
import com.sinochem.yunlian.upm.admin.bean.RoleListBean;
import com.sinochem.yunlian.upm.admin.constant.CommonStatus;
import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.UpmMapper;
import com.google.common.collect.Lists;
import com.sinochem.yunlian.upm.admin.constant.RoleStatus;
import com.sinochem.yunlian.upm.admin.mapper.AclRoleMapper;
import com.sinochem.yunlian.upm.util.*;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-05-16
 */
@Service
public class RoleService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

    @Resource
    private AclRoleMapper aclRoleMapper;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RolePermissionRltService rolePermissionRltService;

    @Resource
    UpmService upmService;

    @Resource
    private UpmMapper upmMapper;

    public AclRole getById(String id) {
        return aclRoleMapper.selectByPrimaryKey(id);
    }

    public RoleBean insertOrUpdate(RoleBean bean) {
        AclRole role = null;
        if (StringUtil.isBlank(bean.getId())) {
            role = new AclRole();
            if (bean.getCode() == null || bean.getName() == null || bean.getRoleType() == null) {
                UpmCacheUtil.setErrMsg("必填项不能为空");
                return null;
            }
            role = getByCode(bean.getCode());
            if (null != role) {
                UpmCacheUtil.setErrMsg("角色编码已存在");
                return null;
            }
            List<AclRole> roles = getByNameAndApp(bean.getName(), bean.getApplicationId());
            if (roles != null && !roles.isEmpty()) {
                UpmCacheUtil.setErrMsg("应用内已存在同名角色");
                return null;
            }
            role = new AclRole();
            BeanUtils.copyProperties(bean, role);
            role.setId(UUID.randomUUID().toString());
            role.setCreateTime(new Date());
            role.setUpdateTime(new Date());
            aclRoleMapper.insert(role);
            bean.setId(role.getId());
        } else {
            role = getById(bean.getId());
            if (role == null) {
                UpmCacheUtil.setErrMsg("角色不存在");
                return null;
            }
            if (bean.getName() != null) {
                AclRoleExample example = new AclRoleExample();
                example.or().andNameEqualTo(bean.getName())
                        .andApplicationIdEqualTo(bean.getApplicationId()).andStatusEqualTo((short) 0)
                        .andIdNotEqualTo(role.getId());
                int count = aclRoleMapper.countByExample(example);
                if (count != 0) {
                    UpmCacheUtil.setErrMsg("应用内已存在同名角色");
                    return null;
                }
                role.setName(bean.getName());
            }
            if (bean.getCode() != null) {
                AclRoleExample example = new AclRoleExample();
                example.or().andCodeEqualTo(bean.getCode())
                        .andIdNotEqualTo(role.getId());
                int count = aclRoleMapper.countByExample(example);
                if (count != 0) {
                    UpmCacheUtil.setErrMsg("系统内已存在相同编码角色");
                    return null;
                }
                role.setCode(bean.getCode());
            }
            role.setComment(bean.getComment() != null ? bean.getComment() : role.getComment());
            role.setStatus(bean.getStatus() != null ? bean.getStatus() : role.getStatus());
            role.setRoleType(bean.getRoleType() != null ? bean.getRoleType() : role.getRoleType());
            role.setUpdateTime(new Date());
            aclRoleMapper.updateByPrimaryKey(role);
        }
        BeanUtils.copyProperties(role, bean);
        bean.setApplicationName(applicationService.getNameById(bean.getApplicationId()));
        return bean;
    }

    public AclRole getByCode(String code) {
        AclRoleExample example = new AclRoleExample();
        example.or().andCodeEqualTo(code);
        List<AclRole> list = aclRoleMapper.selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public AclRole getByApplicationIdAndCode(String appId, String code) {
        AclRoleExample example = new AclRoleExample();
        example.or().andApplicationIdEqualTo(appId).andCodeEqualTo(code);
        List<AclRole> list = aclRoleMapper.selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public List<AclRole> getByApplicationId(String applicationId) {
        AclRoleExample example = new AclRoleExample();
        example.or().andApplicationIdEqualTo(applicationId).andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        List<AclRole> list = aclRoleMapper.selectByExample(example);
        return list;
    }

    public List<AclRole>
    getByApp(String appId) {
        if (appId == null) {
            appId = (String) ThreadCacheUtil.getData("appId");
        }

        return getByApplicationId(appId);
    }

    public List<AclRole> getByIds(List<String> ids) {
        if (ids == null || ids.size() <= 0) {
            return Lists.newArrayList();
        }
        AclRoleExample example = new AclRoleExample();
        example.or().andIdIn(ids).andStatusEqualTo((short) RoleStatus.ACTIVE.getIndex());
        List<AclRole> list = aclRoleMapper.selectByExample(example);
        return list;
    }

    public List<AclRole> getByIds(List<String> ids, String appId) {
        if (null == ids || ids.isEmpty() || null == appId) {
            return Lists.newArrayList();
        }
        AclRoleExample example = new AclRoleExample();
        example.or().andIdIn(ids)
                .andApplicationIdEqualTo(appId)
                .andStatusEqualTo((short) RoleStatus.ACTIVE.getIndex());
        return aclRoleMapper.selectByExample(example);
    }

    public List<AclRole> getByNameAndApp(String name, String applicationId) {
        AclRoleExample example = new AclRoleExample();
        example.or().andNameEqualTo(name).andApplicationIdEqualTo(applicationId).andStatusEqualTo((short) 0);
        List<AclRole> list = aclRoleMapper.selectByExample(example);
        return list;
    }

    public String getMenuByRoleId(String appId, String roleId) {
        List<AclMenuForRole> meuns = upmMapper.getMenuByRoleId(appId, roleId);
        if (meuns == null) {
            return JsonUtil.toJsonString(Collections.emptyList());
        }

        List<AclMenuShow> aclMenuShowLis = new ArrayList<AclMenuShow>();
        for (AclMenuForRole menu : meuns) {
            AclMenuShow show = new AclMenuShow();
            show.setName(menu.getTitle());
            show.setId(menu.getMenuId());
            show.setPId(menu.getParentId());
            show.setOpen(true);
            show.setChecked(menu.getRoleId() != null && !menu.getRoleId().equals(""));
            aclMenuShowLis.add(show);
        }
        return JSON.toJSONString(aclMenuShowLis);
    }

    public List<AclRole> getList(RoleListBean roleListBean) {
        AclRoleExample example = new AclRoleExample();
        AclRoleExample.Criteria crit = example.or();
        if (!StringUtil.null2Trim(roleListBean.getName()).equals("")) {
            crit.andNameLike("%" + roleListBean.getName() + "%");
        }
        if (!StringUtil.null2Trim(roleListBean.getCode()).equals("")) {
            crit.andCodeLike("%" + roleListBean.getCode() + "%");
        }
        if (roleListBean.getApplicationId() != null) {
            crit.andApplicationIdEqualTo(roleListBean.getApplicationId());
        }
        crit.andStatusEqualTo((short) RoleStatus.ACTIVE.getIndex());
        List<AclRole> aclUsers = Lists.newArrayList();

        aclUsers = aclRoleMapper.selectByExample(example);
        return aclUsers;
    }

    public List<AclRole> getList(RoleListBean roleListBean, Page page) {
        AclRoleExample example = new AclRoleExample();
        AclRoleExample.Criteria crit = example.or();
        if (!StringUtil.null2Trim(roleListBean.getName()).equals("")) {
            crit.andNameLike("%" + roleListBean.getName() + "%");
        }
        if (!StringUtil.null2Trim(roleListBean.getCode()).equals("")) {
            crit.andCodeLike("%" + roleListBean.getCode() + "%");
        }
        if (roleListBean.getApplicationId() != null) {
            crit.andApplicationIdEqualTo(roleListBean.getApplicationId());
        }
        crit.andStatusEqualTo((short) RoleStatus.ACTIVE.getIndex());
        List<AclRole> aclUsers = Lists.newArrayList();
        if (page != null) {
            aclUsers = aclRoleMapper.selectByExampleWithRowbounds(example,
                    new RowBounds(page.getStart(), page.getPageSize()));
            page.setTotalCount(aclRoleMapper.countByExample(example));
        } else {
            aclUsers = aclRoleMapper.selectByExample(example);
        }
        return aclUsers;
    }

    public List<AclRole> getList(RoleListBean roleListBean, String userId, Page page) {
        //超级管理员或应用管理员
        return getList(roleListBean, page);
    }

    public List<AclRole> getListByApp(String appId) {
        if (appId == null) {
            appId = (String) ThreadCacheUtil.getData("appId");
        }
        AclRoleExample example = new AclRoleExample();
        example.or().andApplicationIdEqualTo(appId)
                .andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        return aclRoleMapper.selectByExample(example);
    }

    public String getNameById(String id) {
        AclRole aclRole = getById(id);
        return aclRole != null ? aclRole.getName() : "";
    }

    public List<AclRole> findNameLike(String name, String appId) {
        AclRoleExample example = new AclRoleExample();
        AclRoleExample.Criteria criteria = example.or();
        if (name != null && !name.isEmpty()) {
            criteria.andNameLike(name);
        }
        if (appId != null) {
            criteria.andApplicationIdEqualTo(appId);
        }
        criteria.andStatusEqualTo((short) RoleStatus.ACTIVE.getIndex());
        List<AclRole> aclRoles = aclRoleMapper.selectByExample(example);
        return aclRoles;
    }

    public List<AclRole> getByAppkey(String appkey) {
        return getByApp(applicationService.getIdByAppkey(appkey));
    }

    public String deleteRoleById(String id) {
        AclRole aclRole = getById(id);
        if (aclRole == null) {
            return "该角色不存在";
        }
        aclRole.setStatus((short) RoleStatus.DELETE.getIndex());
        aclRole.setUpdateTime(new Date());
        if (null == insertOrUpdate(makeRoleBean(aclRole))) {
            return UpmCacheUtil.getErrMsg();
        }
        userRoleService.deleteAllByRoleId(id);
        rolePermissionRltService.deleteRltByRoleId(id);
        return null;
    }

    public RoleBean makeRoleBean(AclRole aclRole) {
        RoleBean bean = new RoleBean();
        ObjectUtil.copy(aclRole, bean);
        return bean;
    }

    public List<RoleBean> makeRoleBean(List<AclRole> list) {
        List<RoleBean> beans = new ArrayList<RoleBean>();
        List<AclApplication> aclApplicationList = applicationService.getList();
        Map<String, String> appIdNameMap = new HashMap<String, String>();
        for (AclApplication aclApplication : aclApplicationList) {
            appIdNameMap.put(aclApplication.getId(), aclApplication.getName());
        }
        ;
        for (AclRole role : list) {
            RoleBean bean = new RoleBean();
            ObjectUtil.copy(role, bean);
            bean.setApplicationName(appIdNameMap.get(bean.getApplicationId()));
            beans.add(bean);
        }
        return beans;
    }
}
