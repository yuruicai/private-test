package com.sinochem.yunlian.upm.sso.constant;

/**
 * 用户状态枚举
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-02
 */
public enum  UserStatus {

    INACTIVE("待激活", 0),

    ACTIVE("启用", 1),

    FREEZE("冻结", 2),

    DELETE("停用", 3);



    private String name;

    private int index;

    private UserStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.getIndex() == index) {
                return userStatus.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.getName().equals(name)) {
                return userStatus.index;
            }
        }
        return null;
    }

    public static UserStatus getUserStatus(int index) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.getIndex() == index) {
                return userStatus;
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