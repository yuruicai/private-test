package com.sinochem.yunlian.upm.sso.model;


public class UserForSeesion {
    protected Long userId;
    protected String loginName;

    public String getUserId() {
        return String.valueOf(userId);
    }

    public void setUserId(String longId) {
        this.userId = Long.parseLong(longId);
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long toLongId() {
        return userId;
    }


}
