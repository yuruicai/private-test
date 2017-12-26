package com.sinochem.yunlian.upm.admin.bean;

import java.util.List;

/**
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-14
 */
public class MenuListBean {

    private List<String> parentIds;

    private String parentId;

    private String currentPermName;

    private String msg;

    private String errMsg;

    private List<String> ids;

    private String applicationId;

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getCurrentPermName() {
        return currentPermName;
    }

    public void setCurrentPermName(String currentPermName) {
        this.currentPermName = currentPermName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}