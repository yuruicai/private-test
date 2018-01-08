package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.api.vo.PageInfo;

public interface UserService {
    public PageInfo getUserListByCriteria(String name , int page, int rows);
    public int getUserCount(String name);
}
