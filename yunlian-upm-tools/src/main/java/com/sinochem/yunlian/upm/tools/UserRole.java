package com.sinochem.yunlian.upm.tools;

import java.util.List;

/**
 * @author yangguo03
 * @version 1.0
 * @created 14-11-11
 */
public class UserRole {
    private String userId;
    private List<Role> roles;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roles=" + roles +
                '}';
    }
}
