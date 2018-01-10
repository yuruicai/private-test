package com.sinochem.yunlian.upm.api.vo;

import com.sinochem.yunlian.upm.admin.domain.AclApplication;

/**
 * @author gaowei
 * @Description:
 * @date 2018/1/8.16:06
 */
public class ApplicationVo {
    private AclApplication application;

    public ApplicationVo(AclApplication application){
        this.application=application;
    }
    public String getId(){
        return application.getId();
    }
    public String getName(){
        return application.getName();
    }
    public String getRoleId(){
        return application.getRoleId();
    }
    public String getAppkey(){
        return application.getAppkey();
    }
    public String getSecret(){
        return application.getSecret();
    }
    public String getUrl(){
        return application.getUrl();
    }
    public int getStatus(){
        return application.getStatus();
    }
    public String getImage1(){
        return application.getImage1();
    }
    public String getImage2(){
        return application.getImage2();
    }
    public int getUseUpm(){
       return application.getUseUpm();
    }
}
