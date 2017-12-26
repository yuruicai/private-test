package com.sinochem.yunlian.upm.admin.constant;

/**
 * 资源状态枚举
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-02
 */
public enum RolePermissionRltStatus {

    ACTIVE("启用", 0),

    DELETE("停用", 128);

    private String name;

    private int index;

    private RolePermissionRltStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (RolePermissionRltStatus rolePermissionRltStatus : RolePermissionRltStatus.values()) {
            if (rolePermissionRltStatus.getIndex() == index) {
                return rolePermissionRltStatus.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (RolePermissionRltStatus rolePermissionRltStatus : RolePermissionRltStatus.values()) {
            if (rolePermissionRltStatus.getName().equals(name)) {
                return rolePermissionRltStatus.index;
            }
        }
        return null;
    }

    public static RolePermissionRltStatus getRolePermissionRltStatus(int index) {
        for (RolePermissionRltStatus rolePermissionRltStatus : RolePermissionRltStatus.values()) {
            if (rolePermissionRltStatus.getIndex() == index) {
                return rolePermissionRltStatus;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}