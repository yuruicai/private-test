package com.sinochem.yunlian.upm.admin.model;

import java.util.Set;

/**
 * @author yangguo
 * @version 1.0
 * @created 15-7-13.
 */
public class RoleContext {
    private String roleId;
    private String roleName;
    private Set<String> orgIds;
    private Set<String> wmCityIds;
    private Set<String> cityIds;
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

    public Set<String> getWmCityIds() {
        return wmCityIds;
    }

    public void setWmCityIds(Set<String> wmCityIds) {
        this.wmCityIds = wmCityIds;
    }

    public Set<String> getCityIds() {
        return cityIds;
    }

    public void setCityIds(Set<String> cityIds) {
        this.cityIds = cityIds;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    @Override
    public String toString() {
        return "RoleContext{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", orgIds=" + orgIds +
                ", wmCityIds=" + wmCityIds +
                ", cityIds=" + cityIds +
                ", custom='" + custom + '\'' +
                '}';
    }
}
