package com.sinochem.yunlian.upm.sso.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhanghongze
 * Date: 14-7-25
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class TimesLimitInfo  implements Serializable{

    private int successLoginTimes;//登录成功次数

    private int failLoginTimes;//失败登录次数

    private long loginTimeStamp; //登录时间戳

    private long failLoginTimeStamp; //失败登录时间

    private int  failLoginTimes5m; //5分钟的失败次数

    public int getSuccessLoginTimes() {
        return successLoginTimes;
    }

    public void setSuccessLoginTimes(int successLoginTimes) {
        this.successLoginTimes = successLoginTimes;
    }

    public int getFailLoginTimes() {
        return failLoginTimes;
    }

    public void setFailLoginTimes(int failLoginTimes) {
        this.failLoginTimes = failLoginTimes;
    }

    public long getLoginTimeStamp() {
        return loginTimeStamp;
    }

    public void setLoginTimeStamp(long loginTimeStamp) {
        this.loginTimeStamp = loginTimeStamp;
    }

    public long getFailLoginTimeStamp() {
        return failLoginTimeStamp;
    }

    public void setFailLoginTimeStamp(long failLoginTimeStamp) {
        this.failLoginTimeStamp = failLoginTimeStamp;
    }

    public int getFailLoginTimes5m() {
        return failLoginTimes5m;
    }

    public void setFailLoginTimes5m(int failLoginTimes5m) {
        this.failLoginTimes5m = failLoginTimes5m;
    }
}