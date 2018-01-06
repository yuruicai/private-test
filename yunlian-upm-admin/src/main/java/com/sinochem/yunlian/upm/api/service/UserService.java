package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.bean.UserListBean;
import com.sinochem.yunlian.upm.admin.domain.AclUser;
import com.sinochem.yunlian.upm.api.vo.UserVo;
import com.sinochem.yunlian.upm.util.Page;

import java.util.List;

public interface UserService {
    public UserVo getUserListByCriteria(String name , int page, int rows);
    public int getUserCount(String name);
}
