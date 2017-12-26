package com.sinochem.yunlian.upm.admin.bean;

import java.util.Date;
import java.util.Map;

/**
 * @author wangwei
 * @version 1.0
 * @created 2013-05-17
 */
public class RoleBean {

    private String id;

    private String code;

    private String name;

    private Short status;

    private String comment;

    private Date createTime;

    private Date updateTime;

    private Integer roleType;

    private String typeName;

    private String applicationId;

    private String applicationName;

    /**
     * 是否影响旧数据
     */
    private Integer affect;

    /**
     * 是否添加默认上下文
     */
    private Integer context;

    /**
     * 删除或者新增
     */
    private Integer isNew;

    /**
     * 传播
     */
    private Integer prop;


    private Integer orgId;

    private String orgName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getAffect() {
        return affect;
    }

    public void setAffect(Integer affect) {
        this.affect = affect;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getContext() {
        return context;
    }

    public void setContext(Integer context) {
        this.context = context;
    }

    public Integer getNew() {
        return isNew;
    }

    public void setNew(Integer aNew) {
        isNew = aNew;
    }

    public Integer getProp() {
        return prop;
    }

    public void setProp(Integer prop) {
        this.prop = prop;
    }


    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
