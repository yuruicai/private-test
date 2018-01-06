package com.sinochem.yunlian.upm.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.admin.domain.AclUserExample;
import com.sinochem.yunlian.upm.admin.mapper.AclUserMapper;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.UserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private AclUserMapper aclUserMapper;

    public UserVo getUserListByCriteria(String name , int page, int rows){
        PageHelper.startPage(page, rows);
        AclUserExample example = new AclUserExample();
        AclUserExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<AclUser> list = aclUserMapper.selectByExample(example);
        PageInfo<AclUser> info = new PageInfo<>();
        return new UserVo(info.getTotal(),info.getList());
    }
    public int getUserCount(String name){
       int count = aclUserMapper.getCount(name);
       return count;
    }
}
