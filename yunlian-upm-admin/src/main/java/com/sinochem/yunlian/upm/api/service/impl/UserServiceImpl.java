package com.sinochem.yunlian.upm.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.admin.domain.AclUserExample;
import com.sinochem.yunlian.upm.admin.mapper.AclUserMapper;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.acl.Acl;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private AclUserMapper aclUserMapper;

    public PageInfo getUserListByCriteria(String name , int page, int rows){
        PageHelper.startPage(page, rows);
        AclUserExample example = new AclUserExample();
        AclUserExample.Criteria criteria = example.createCriteria();
        if (! StringUtils.isEmpty(name)) {
            criteria.andNameEqualTo(name);
        }

        List<AclUser> list = aclUserMapper.selectByExample(example);
        List<UserVo> userVos = list.stream().map(u -> new UserVo(u)).collect(Collectors.toList());
        com.github.pagehelper.PageInfo info = new com.github.pagehelper.PageInfo(userVos);
        return new PageInfo(info.getPageNum(),info.getPageSize(),info.getPages(),(int) info.getTotal(),info.getList());
    }
    public int getUserCount(String name){
       int count = aclUserMapper.getCount(name);
       return count;
    }
}
