package com.sinochem.yunlian.upm.admin.model;

import java.util.List;

/**
 * @author zhangxi
 * @created 13-7-11
 */
public class AuthResult {
    /**
     * 鉴权结果：true有权限，false没有权限
      */
    private boolean auth;
    /**
     * 有权限的角色Code列表
      */
    private List<String> roles;

    /**
     * 资源匹配的权限的Id
     */
    private String permissionId;

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AuthResult");
        sb.append("{auth=").append(auth);
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }
}
