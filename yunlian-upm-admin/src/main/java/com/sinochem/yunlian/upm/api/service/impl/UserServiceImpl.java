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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private AclUserMapper aclUserMapper;

    @Override
    public PageInfo getUserListByCriteria(String name, int page, int rows) {
        PageHelper.startPage(page, rows);
        AclUserExample example = new AclUserExample();
        AclUserExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameEqualTo(name);
        }

        List<AclUser> list = aclUserMapper.selectByExample(example);
        List<UserVo> userVos = list.stream().map(u -> new UserVo(u)).collect(Collectors.toList());
        com.github.pagehelper.PageInfo info = new com.github.pagehelper.PageInfo(userVos);
        return new PageInfo(info.getPageNum(), info.getPageSize(), info.getPages(), (int) info.getTotal(), info.getList());
    }

    @Override
    public List<UserVo> getByIds(List<String> ids) {
        AclUserExample example = new AclUserExample();
        AclUserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        List<AclUser> users = aclUserMapper.selectByExample(example);
        return users.stream().map(u -> new UserVo(u)).collect(Collectors.toList());
    }

    /**
     * 查看用户的方法
     * @param id
     * @return
     */
    @Override
    public UserByIdVo getUserById(String id) {
        AclUser user = aclUserMapper.selectByPrimaryKey(id);
        UserByIdVo userByIdVo = null;
        if (!StringUtils.isEmpty(user)) {
            userByIdVo = new UserByIdVo(user);
        }
        return userByIdVo;
    }

    /**
     * user修改的方法
     * @param aclUser
     * @return
     */
    @Override
    public int updateUser(AclUser aclUser) {
        int i = 0;
        if (aclUser.getId() == null) {
            throw ApiException.of("用户ID不能为空");
        }
        return aclUserMapper.updateByPrimaryKeySelective(aclUser);
    }

    /**
     * 电话号码验证
     * @param id
     * @param newMobile
     * @return
     */
    @Override
    public boolean mobileCheck(String id , String newMobile) {
            if(id == null ){
                return false;
            }
            AclUser   user = aclUserMapper.selectByPrimaryKey( id);
            if (user != null ){
                String mobile = user.getDecryptMobile();
                if(!StringUtils.isEmpty(mobile)){
                    log.info("原号码片段，subMobile={}",mobile.substring(3,7));
                    log.info("新号码片段，newMobile={}",newMobile);
                   return (mobile.substring(3,7)).equals(newMobile);
                }
                return false;
            }
            return false;
        }
    }


