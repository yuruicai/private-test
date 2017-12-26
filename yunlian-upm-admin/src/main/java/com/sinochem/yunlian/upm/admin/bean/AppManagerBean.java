package com.sinochem.yunlian.upm.admin.bean;

/**
 * @author yangguo03
 * @version 1.0
 * @created 14-12-31
 */
public class AppManagerBean {
    private String id;
    private String name;
    private String login;
    private String appId;
    private String appkey;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
}
