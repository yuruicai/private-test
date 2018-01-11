package com.sinochem.yunlian.upm.sso.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IpLog {
    private Long id;

    private String ip;

    private String loginName;

    private String application;

    private Date loginTime;

    private String userAgent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application == null ? null : application.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }
}