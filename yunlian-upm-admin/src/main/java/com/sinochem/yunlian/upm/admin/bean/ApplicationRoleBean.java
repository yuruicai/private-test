package com.sinochem.yunlian.upm.admin.bean;

/**
 * @author yangguo03
 * @version 1.0
 * @created 13-9-10
 */
public class ApplicationRoleBean {

    private String appId;

    private String oldRoleId;

    private String newRoleId;

    private String userId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOldRoleId() {
        return oldRoleId;
    }

    public void setOldRoleId(String oldRoleId) {
        this.oldRoleId = oldRoleId;
    }

    public String getNewRoleId() {
        return newRoleId;
    }

    public void setNewRoleId(String newRoleId) {
        this.newRoleId = newRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
