package com.sinochem.yunlian.upm.admin.domain;

import java.util.Date;

public class MenuItem {
    private String id;

    private Integer permissionId;

    private String code;

    private String parentId;

    private String title;

    private String url;

    private Short sortNum;

    private Short showType;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Short getSortNum() {
        return sortNum;
    }

    public void setSortNum(Short sortNum) {
        this.sortNum = sortNum;
    }

    public Short getShowType() {
        return showType;
    }

    public void setShowType(Short showType) {
        this.showType = showType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}