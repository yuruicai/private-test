package com.sinochem.yunlian.upm.admin.bean;

import java.util.List;

/**
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-21
 */
public class UserRoleBean {

    private List<String> userIds;

    private List<RoleConfBean> roleConfBeans;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<RoleConfBean> getRoleConfBeans() {
        return roleConfBeans;
    }

    public void setRoleConfBeans(List<RoleConfBean> roleConfBeans) {
        this.roleConfBeans = roleConfBeans;
    }
}
