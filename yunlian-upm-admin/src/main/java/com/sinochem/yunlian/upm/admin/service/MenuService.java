/*
 * Copyright (c) 2010-2013 meituan.com
 * All rights reserved.
 * 
 */
package com.sinochem.yunlian.upm.admin.service;

import com.sinochem.yunlian.upm.admin.constant.CommonStatus;
import com.sinochem.yunlian.upm.admin.constant.MenuStatus;
import com.sinochem.yunlian.upm.admin.constant.MenuType;
import com.sinochem.yunlian.upm.admin.domain.AclMenu;
import com.sinochem.yunlian.upm.admin.domain.AclMenuExample;
import com.sinochem.yunlian.upm.admin.mapper.AclMenuMapper;
import com.sinochem.yunlian.upm.util.StringUtil;
import com.sinochem.yunlian.upm.util.UpmCacheUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-05-13
 */
@Service
public class MenuService {

    @Resource
    private AclMenuMapper aclMenuMapper;

    public List<AclMenu> select(Collection<String> ids) {
        AclMenuExample aclMenuExample = new AclMenuExample();
        List<String> list = Lists.newArrayList();
        list.addAll(ids);
        aclMenuExample.or().andIdIn(list).andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        aclMenuExample.setOrderByClause("sort_num asc");
        return aclMenuMapper.selectByExample(aclMenuExample);
    }

    public List<AclMenu> getAll(String appId) {
        if (null == appId) {
            return Lists.newArrayList();
        }
        AclMenuExample example = new AclMenuExample();
        example.or().andApplicationIdEqualTo(appId)
                .andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        example.setOrderByClause("sort_num asc");
        return aclMenuMapper.selectByExample(example);
    }


    public List<AclMenu> getSubListByParentId(String id) {
        AclMenuExample aclMenuExample = new AclMenuExample();
        aclMenuExample.or().andParentIdEqualTo(id)
                .andStatusEqualTo((short) CommonStatus.ACTIVE.getIndex());
        aclMenuExample.setOrderByClause("sort_num asc");
        return aclMenuMapper.selectByExample(aclMenuExample);
    }

    public AclMenu getById(String id) {
        return aclMenuMapper.selectByPrimaryKey(id);
    }

    public int save(AclMenu aclMenu) {
        if (StringUtil.isBlank(aclMenu.getId())) {
            aclMenu.setCreateTime(new Date());
        }
        aclMenu.setUpdateTime(new Date());
        aclMenu.setStatus((short) MenuStatus.ACTIVE.getIndex());
        if ("-1".equals(aclMenu.getId()) || StringUtil.isBlank(aclMenu.getId())) {
            return insert(aclMenu);
        } else {
            return update(aclMenu);
        }
    }

    private int insert(AclMenu aclMenu) {
        aclMenu.setId(UUID.randomUUID().toString());
        aclMenu.setOperatorId(UpmCacheUtil.getCurrentUserId());
        aclMenu.setSortNum((short)9999);
        return aclMenuMapper.insert(aclMenu);
    }

    public String deleteMenu(String id) {
        AclMenu aclMenu = getById(id);
        if (aclMenu == null) {
            return "菜单不存在";
        }
        if (getSubListByParentId(id).size() > 0) {
            return "该节点含有子节点，不能删除";
        }
        aclMenu.setStatus((short) CommonStatus.DELETE.getIndex());
        int a = update(aclMenu);
        return a == 1 ? null : "删除失败";
    }

    private int update(AclMenu aclMenu) {
        aclMenu.setOperatorId(UpmCacheUtil.getCurrentUserId());
        return aclMenuMapper.updateByPrimaryKeySelective(aclMenu);
    }

    public String updateMoveMenu(String id, String oldParentId, String targetId, String moveType) {
        AclMenu menu = getById(id);
        if (menu == null) {
            return "该菜单不存在";
        }

        AclMenu target = getById(targetId);
        if (target == null) {
            return "目标菜单不存在";
        }

        List<AclMenu> subList = getSubListByParentId(target.getParentId());
        List<AclMenu> newList = new ArrayList<AclMenu>();

        if (null != subList) {
            for (AclMenu c : subList) {
                if ("next".equals(moveType)) {
                    if (targetId.trim().equals(c.getId().trim())) {
                        newList.add(target);
                        newList.add(menu);
                        continue;
                    }
                }
                if ("prev".equals(moveType)) {
                    if (targetId.trim().equals(c.getId().trim())) {
                        newList.add(menu);
                        newList.add(target);
                        continue;
                    }
                }
                if (menu.getId().trim().equals(c.getId().trim())) {
                    continue;
                }
                newList.add(c);
            }
            for (short ss = 0; ss < newList.size(); ss++) {
                AclMenu nc = newList.get(ss);
                nc.setSortNum(ss);
                menu.setUpdateTime(new Date());
                menu.setOperatorId(UpmCacheUtil.getCurrentUserId());
                aclMenuMapper.updateByPrimaryKeySelective(nc);
            }
        }
        return null;
    }

    public void initAppTopMenu(String appId){
        AclMenu menu = new AclMenu();
        menu.setApplicationId(appId);
        menu.setShowType((short) MenuType.CURRENT.getIndex());
        menu.setTitle("功能菜单");
        menu.setIsShow((short) 1);
        menu.setStatus((short)CommonStatus.ACTIVE.getIndex());
        menu.setSortNum((short)0);
        save(menu);
    }
}
