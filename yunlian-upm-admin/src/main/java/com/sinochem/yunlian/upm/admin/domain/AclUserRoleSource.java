package com.sinochem.yunlian.upm.admin.domain;

public class AclUserRoleSource {
    private String id;

    private String aclUserRoleRltid;

    private Short sourceType;

    private String sourceRltId;

    private Short status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAclUserRoleRltid() {
        return aclUserRoleRltid;
    }

    public void setAclUserRoleRltid(String aclUserRoleRltid) {
        this.aclUserRoleRltid = aclUserRoleRltid == null ? null : aclUserRoleRltid.trim();
    }

    public Short getSourceType() {
        return sourceType;
    }

    public void setSourceType(Short sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceRltId() {
        return sourceRltId;
    }

    public void setSourceRltId(String sourceRltId) {
        this.sourceRltId = sourceRltId == null ? null : sourceRltId.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}