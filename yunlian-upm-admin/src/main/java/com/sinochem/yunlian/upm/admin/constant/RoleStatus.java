package com.sinochem.yunlian.upm.admin.constant;

/**
 * 角色状态枚举
 */
public enum RoleStatus {

    ACTIVE("启用", 0),

    DELETE("停用", 128);

    private String name;

    private int index;

    private RoleStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (RoleStatus roleStatus : RoleStatus.values()) {
            if (roleStatus.getIndex() == index) {
                return roleStatus.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (RoleStatus roleStatus : RoleStatus.values()) {
            if (roleStatus.getName().equals(name)) {
                return roleStatus.index;
            }
        }
        return null;
    }

    public static RoleStatus getRoleStatus(int index) {
        for (RoleStatus roleStatus : RoleStatus.values()) {
            if (roleStatus.getIndex() == index) {
                return roleStatus;
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