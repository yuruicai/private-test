package com.sinochem.yunlian.upm.tools;

import java.io.Serializable;

/**
 * Created by zhanghongze on 2015/12/24.
 */
public class Application implements Serializable {

    private String id;

    private String name;

    private String roleId;

    private String appkey;

    private String url;

    private Short status;

    private String image1;

    private String image2;

    private Short useUpm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey == null ? null : appkey.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1 == null ? null : image1.trim();
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2 == null ? null : image2.trim();
    }

    public Short getUseUpm() {
        return useUpm;
    }

    public void setUseUpm(Short useUpm) {
        this.useUpm = useUpm;
    }
}
