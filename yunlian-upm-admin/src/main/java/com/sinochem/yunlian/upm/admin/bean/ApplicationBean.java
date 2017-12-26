package com.sinochem.yunlian.upm.admin.bean;

import java.util.List;

/**
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-07
 */
public class ApplicationBean {

    private String id;

    private String name;

    private String roleId;

    private String roleName;

    private String appKey;

    private String secret;

    private String url;

    private String image1;

    private String image2;

    private Short useUpm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public Short getUseUpm() {
        return useUpm;
    }

    public void setUseUpm(Short useUpm) {
        this.useUpm = useUpm;
    }
}