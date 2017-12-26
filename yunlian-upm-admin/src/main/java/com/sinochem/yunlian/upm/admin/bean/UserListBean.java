package com.sinochem.yunlian.upm.admin.bean;

import java.io.Serializable;

/**
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-03
 */
public class UserListBean  {

    private Integer pageNo = 1;

    private Integer limit = 20;

    private String orderName;

    private String sortType;

    private String name;

    private String loginName;

    private String mobile;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getName() {
        return name != null ?name.trim():null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName != null ? loginName.trim() : null;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile != null ? mobile.trim() : null;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

}