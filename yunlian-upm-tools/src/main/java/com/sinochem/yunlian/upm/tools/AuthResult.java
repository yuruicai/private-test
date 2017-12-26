package com.sinochem.yunlian.upm.tools;

import java.util.List;

/**
 * @author zhangxi
 * @created 13-7-11
 */
public class AuthResult {
    private boolean auth;
    private List<String> roles;

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
