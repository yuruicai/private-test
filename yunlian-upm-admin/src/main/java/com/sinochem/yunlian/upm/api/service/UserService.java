package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.UserByIdVo;

public interface UserService {
    public PageInfo getUserListByCriteria(String name , int page, int rows);
    public int getUserCount(String name);
    public UserByIdVo getUserById(String id);
}
