package com.sinochem.yunlian.upm.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.admin.domain.AclUserExample;
import com.sinochem.yunlian.upm.admin.mapper.AclUserMapper;
import com.sinochem.yunlian.upm.api.exception.ApiException;
import com.sinochem.yunlian.upm.api.service.UserService;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.UserByIdVo;
import com.sinochem.yunlian.upm.api.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    /**
     * 查看用户的方法
     * @param id
     * @return
     */
    @Override
    public UserByIdVo getUserById(String id) {
        AclUser user = aclUserMapper.selectByPrimaryKey(id);
        UserByIdVo userByIdVo=null;
        if(!StringUtils.isEmpty(user)){
             userByIdVo = new UserByIdVo(user);
        }
        return userByIdVo;
    }

    @Override
    public int updateUser(AclUser aclUser) {
        int i = 0;
        if(aclUser.getId() == null){
            throw ApiException.of("用户ID不能为空");
        }
        return aclUserMapper.updateByPrimaryKeySelective(aclUser);
    }

}
