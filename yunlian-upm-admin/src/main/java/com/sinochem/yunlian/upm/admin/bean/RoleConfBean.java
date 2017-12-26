package com.sinochem.yunlian.upm.admin.bean;

import java.util.Map;

/**
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-21
 */
public class RoleConfBean {

    private String id;

    private String code;

    private Integer status;

    private String roleName;

    private String roleSource;

    private Map<Short, Map<String, ContextBean>> typeContext;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Map<Short, Map<String, ContextBean>> getTypeContext() {
        return typeContext;
    }

    public void setTypeContext(Map<Short, Map<String, ContextBean>> typeContext) {
        this.typeContext = typeContext;
    }

    public String getRoleSource() {
        return roleSource;
    }

    public void setRoleSource(String roleSource) {
        this.roleSource = roleSource;
    }
}
