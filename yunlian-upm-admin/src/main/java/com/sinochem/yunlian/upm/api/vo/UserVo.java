package com.sinochem.yunlian.upm.api.vo;


import com.sinochem.yunlian.upm.admin.domain.AclUser;


public class UserVo {

    private AclUser aclUser;
/*
    private String id;

    private Short type;

    private String loginName;

    private String email;

    private String mobile;

    private String name;

    private Short status;
    */

    public UserVo(AclUser aclUser) {
        this.aclUser = aclUser;
    }


    public String getId() {
        return aclUser.getId();
    }

    public Short getType() {
        return aclUser.getType();
    }

    public String getLoginName() {
        return aclUser.getLoginName();
    }

    public String getEmail() {
        return aclUser.getEmail();
    }

    public String getMobile() {
        return aclUser.getMobile();
    }

    public String getName() {
        return aclUser.getName();
    }

    public Short getStatus() {
        return aclUser.getStatus();
    }
}
