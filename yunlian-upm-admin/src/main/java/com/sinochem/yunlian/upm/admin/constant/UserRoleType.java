package com.sinochem.yunlian.upm.admin.constant;

/**
 * 菜单类型
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-10
 */
public enum UserRoleType {


    OWN("拥有角色", 1),

    AUTH("可以授予角色", 2);


    private String name;

    private int index;

    private UserRoleType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.getIndex() == index) {
                return userRoleType.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.getName().equals(name)) {
                return userRoleType.index;
            }
        }
        return null;
    }

    public static UserRoleType getUserRoleType(int index) {
        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.getIndex() == index) {
                return userRoleType;
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