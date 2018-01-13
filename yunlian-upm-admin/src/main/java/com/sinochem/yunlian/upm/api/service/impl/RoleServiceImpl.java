package com.sinochem.yunlian.upm.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.sinochem.yunlian.upm.admin.domain.*;
import com.sinochem.yunlian.upm.admin.mapper.AclApplicationMapper;
import com.sinochem.yunlian.upm.admin.mapper.AclRoleMapper;
import com.sinochem.yunlian.upm.admin.mapper.AclUserRoleRltMapper;
import com.sinochem.yunlian.upm.api.exception.ApiException;
import com.sinochem.yunlian.upm.api.service.RoleService;
import com.sinochem.yunlian.upm.api.util.StringUtils;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.RoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/08 下午2:40
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private static final short STATUS = 0;

    @Autowired
    private AclRoleMapper roleDao;
    @Autowired
    private AclUserRoleRltMapper userRoleDao;
    @Autowired
    private AclApplicationMapper applicationDao;


    @Override
    public String insert(AclRole role) {
        if (log.isInfoEnabled()) {
            log.info("创建角色,角色内容：" + JSON.toJSONString(role));
        }
        if (role.getCode() == null || role.getName() == null) {
            throw ApiException.of("角色编码或角色名不能为空");
        }
        AclRole oldRole = getByCode(role.getCode());
        if (null != oldRole) {
            throw ApiException.of("角色编码已存在");
        }
        List<AclRole> roles = getByNameAndApp(role.getName(), role.getApplicationId());
        if (roles != null && !roles.isEmpty()) {
            throw ApiException.of("应用内已存在同名角色");
        }
        role.setId(StringUtils.uuid());
        Date now = new Date();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        role.setStatus(STATUS);
        roleDao.insert(role);
        return role.getId();
    }

    @Override
    public void modifyById(AclRole role) {
        if (log.isInfoEnabled()) {
            log.info("更新角色内容：" + JSON.toJSONString(role));
        }
        if (org.springframework.util.StringUtils.isEmpty(role.getId())) {
            throw ApiException.of("角色ID参数为空");
        }
        if (role.getCode() == null || role.getName() == null) {
            throw ApiException.of("角色编码或角色名不能为空");
        }
        roleDao.updateByPrimaryKeySelective(role);
    }

    @Override
    public AclRole getByCode(String code) {
        AclRoleExample example = new AclRoleExample();
        example.or().andCodeEqualTo(code);
        List<AclRole> list = roleDao.selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public AclRole getById(String id) {
        AclRoleExample example = new AclRoleExample();
        example.or().andIdEqualTo(id);
        List<AclRole> list = roleDao.selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public PageInfo<RoleVo> getByCodeOrName(String param, int curPage, int pageSize) {
        PageHelper.startPage(curPage, pageSize);
        AclRoleExample example = new AclRoleExample();
        if (!org.springframework.util.StringUtils.isEmpty(param)) {
            example.or().andCodeLike("%"+param+"%").andNameLike("%"+param+"%");
        }
        List<AclRole> roles = roleDao.selectByExample(example);
        List<RoleVo> roleVos = roles.stream().map(r -> new RoleVo(r)).collect(Collectors.toList());
        com.github.pagehelper.PageInfo<RoleVo> pageInfo = new com.github.pagehelper.PageInfo(roleVos);
        return new PageInfo(pageInfo.getPageNum(), pageSize, pageInfo.getPages(), (int) pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<AclRole> getByUserIdAndAppKey(String appKey, String userId) {
        //查询用户拥有的角色
        AclUserRoleRltExample example = new AclUserRoleRltExample();
        AclUserRoleRltExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId).andStatusEqualTo(STATUS);

//        example.or().andStatusEqualTo((short)1).andUserIdEqualTo(userId);
        List<AclUserRoleRlt> userRoleRltList = userRoleDao.selectByExample(example);
        if (CollectionUtils.isEmpty(userRoleRltList)) {
            return Collections.EMPTY_LIST;
        }

        List<String> roleIds = userRoleRltList.stream().map(r -> r.getRoleId()).collect(Collectors.toList());
        AclRoleExample roleExample = new AclRoleExample();
        AclRoleExample.Criteria criteria1 = roleExample.createCriteria();
        if (org.springframework.util.StringUtils.isEmpty(appKey)){
            criteria1.andApplicationIdEqualTo(getAppId(appKey));
        }
        criteria1.andIdIn(roleIds);
        return roleDao.selectByExample(roleExample);

    }

    @Override
    public List<String> getUserIds(String roleId) {
        AclUserRoleRltExample example = new AclUserRoleRltExample();
        example.createCriteria().andStatusEqualTo(STATUS).andRoleIdEqualTo(roleId);
        List<AclUserRoleRlt> userIds = userRoleDao.selectByExample(example);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.EMPTY_LIST;
        }
        return userIds.stream().map(u -> u.getUserId()).collect(Collectors.toList());
    }

    private String getAppId(String appKey) {
        AclApplicationExample example = new AclApplicationExample();
        example.or().andAppkeyEqualTo(appKey);
        List<AclApplication> aclApplications = applicationDao.selectByExample(example);
        if (CollectionUtils.isEmpty(aclApplications)) {
            throw ApiException.of("应用不存在：appKey=" + appKey);
        }
        return aclApplications.get(0).getId();
    }

    public AclRole getByApplicationIdAndCode(String appId, String code) {
        AclRoleExample example = new AclRoleExample();
        example.or().andApplicationIdEqualTo(appId).andCodeEqualTo(code);
        List<AclRole> list = roleDao.selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public List<AclRole> getByNameAndApp(String name, String applicationId) {
        AclRoleExample example = new AclRoleExample();
        example.or().andNameEqualTo(name).andApplicationIdEqualTo(applicationId).andStatusEqualTo(STATUS);
        List<AclRole> list = roleDao.selectByExample(example);
        return list;
    }
}
