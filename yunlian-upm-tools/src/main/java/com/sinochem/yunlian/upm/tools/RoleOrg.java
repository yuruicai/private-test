package com.sinochem.yunlian.upm.tools;

import java.lang.Override;
import java.lang.String;
import java.util.Set;

/**
 * @author zhangxi
 * @created 13-7-26
 */
public class RoleOrg {
    private String roleId;
    private String roleName;
    private Set<String> orgIds;
    private String custom;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<String> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(Set<String> orgIds) {
        this.orgIds = orgIds;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    @Override
    public String toString() {
        return "RoleOrg{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", orgIds=" + orgIds +
                ", custom='" + custom + '\'' +
                '}';
    }
}
