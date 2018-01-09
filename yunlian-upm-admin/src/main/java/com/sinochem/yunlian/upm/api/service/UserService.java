package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.api.vo.PageInfo;
import com.sinochem.yunlian.upm.api.vo.UserVo;

import java.util.List;

public interface UserService {

    PageInfo getUserListByCriteria(String name, int page, int rows);

    List<UserVo> getByIds(List<String> ids);
}
