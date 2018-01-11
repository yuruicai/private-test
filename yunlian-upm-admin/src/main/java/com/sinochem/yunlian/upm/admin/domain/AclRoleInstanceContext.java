package com.sinochem.yunlian.upm.admin.domain;

import java.util.Date;

public class AclRoleInstanceContext {
    private String id;

    private String userRoleRltId;

    private String applicationId;

    private Byte status;

    private String orgId;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserRoleRltId() {
        return userRoleRltId;
    }

    public void setUserRoleRltId(String userRoleRltId) {
        this.userRoleRltId = userRoleRltId == null ? null : userRoleRltId.trim();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId == null ? null : applicationId.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}