package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.Response;
import com.sinochem.yunlian.upm.api.vo.UserByIdVo;

public interface UserService {
    public PageInfo getUserListByCriteria(String name , int page, int rows);
    public UserByIdVo getUserById(String id);
    public int updateUser(AclUser aclUser);
}
