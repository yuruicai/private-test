package com.sinochem.yunlian.upm.api.service;

import com.sinochem.yunlian.upm.api.vo.PageInfo;

/**
 * @author gaowei
 * @Description:
 * @date 2018/1/9.10:45
 */
public interface CompanyUserManageService {

    //条件查询公司列表
    PageInfo getListOfCompany(String companyName,int page,int rows);

    //更新企业状态
    int updateCompanyStatus(String id);

    //根据指定公司查询当前公司所有成员
    PageInfo getListOfUser(String id, Integer page, int rows);

    //根据userId删除企业成员：更新显示状态
    int updateStatus(String id);

    //设置当前用户为管理员
    int updateAdmin(String id);

    //条件查询所有企业用户成员
    PageInfo getAllListOfUser(String id,String loginName, String mobile, String name, Integer page, int rows);

    //根据userId更新成员状态
    int updateStatusOfUser(String id);

    //将勾选成员列表添加到当前企业
    int add(String[] userIds, String companyId);
}
