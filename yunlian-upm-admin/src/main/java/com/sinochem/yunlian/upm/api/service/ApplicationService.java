package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.admin.domain.AclApplication;
import com.sinochem.yunlian.upm.api.vo.PageInfo;


/**
 * @author gaowei
 * @Description:
 * @date 2018/1/6.17:41
 */
public interface ApplicationService {
    //新增记录
    String insert(AclApplication application);

    //删除复选框勾选纪录
    int delete(String[] ids);

    //更新角色状态
    String updateStatus(String id);

    //更改记录
    int update(AclApplication application);

    //查询记录
    AclApplication select(String id);

    //条件搜索
    PageInfo search(String appkey,String name,int page,int rows);



}

