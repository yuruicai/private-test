package com.sinochem.yunlian.upm.sso.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangxi
 * @created 14-1-19
 */
public class Session implements Serializable {
    private String userId;
    private String userLogin;
    private String userName;
    private Short userType;
    private Date createTime;
    private String createIp;
    private String createUA;
    private boolean isMobileCreate;
    private String createOs;
    private String createScreen;
    private String createService;
    private String uuid;
    private Date updateTime;
    private String lastIp;
    private String lastUA;
    private String lastOs;
    private String lastScreen;
    private String lastService;
    private String lastUuid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUA() {
        return createUA;
    }

    public void setCreateUA(String createUA) {
        this.createUA = createUA;
    }

    public boolean isMobileCreate() {
        return isMobileCreate;
    }

    public void setMobileCreate(boolean mobileCreate) {
        isMobileCreate = mobileCreate;
    }

    public String getCreateOs() {
        return createOs;
    }

    public void setCreateOs(String createOs) {
        this.createOs = createOs;
    }

    public String getCreateScreen() {
        return createScreen;
    }

    public void setCreateScreen(String createScreen) {
        this.createScreen = createScreen;
    }

    public String getCreateService() {
        return createService;
    }

    public void setCreateService(String createService) {
        this.createService = createService;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getLastUA() {
        return lastUA;
    }

    public void setLastUA(String lastUA) {
        this.lastUA = lastUA;
    }

    public String getLastOs() {
        return lastOs;
    }

    public void setLastOs(String lastOs) {
        this.lastOs = lastOs;
    }

    public String getLastScreen() {
        return lastScreen;
    }

    public void setLastScreen(String lastScreen) {
        this.lastScreen = lastScreen;
    }

    public String getLastService() {
        return lastService;
    }

    public void setLastService(String lastService) {
        this.lastService = lastService;
    }

    public String getLastUuid() {
        return lastUuid;
    }

    public void setLastUuid(String lastUuid) {
        this.lastUuid = lastUuid;
    }

    public User toUser() {
        User user = new User();
        user.setId(getUserId());
        user.setLogin(getUserLogin());
        user.setName(getUserName());
        user.setType(getUserType());
        return user;
    }

    public UserForSeesion toUserForSession() {
        UserForSeesion userForSession = new UserForSeesion();
        userForSession.setUserId(getUserId());
        userForSession.setLoginName(getUserLogin());
        return userForSession;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Session");
        sb.append("{userId=").append(userId);
        sb.append(", userLogin='").append(userLogin).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userType='").append(userType).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", createIp='").append(createIp).append('\'');
        sb.append(", createUA='").append(createUA).append('\'');
        sb.append(", isMobileCreate=").append(isMobileCreate);
        sb.append(", createOs='").append(createOs).append('\'');
        sb.append(", createScreen='").append(createScreen).append('\'');
        sb.append(", createService='").append(createService).append('\'');
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append(", updateTime=").append(updateTime);
        sb.append(", lastIp='").append(lastIp).append('\'');
        sb.append(", lastUA='").append(lastUA).append('\'');
        sb.append(", lastOs='").append(lastOs).append('\'');
        sb.append(", lastScreen='").append(lastScreen).append('\'');
        sb.append(", lastService='").append(lastService).append('\'');
        sb.append(", lastUuid='").append(lastUuid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
