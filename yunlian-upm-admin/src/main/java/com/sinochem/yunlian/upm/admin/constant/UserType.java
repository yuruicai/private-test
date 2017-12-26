package com.sinochem.yunlian.upm.admin.constant;

/**
 * 用户类型枚举
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-02
 */
public enum UserType {

    EMPLOYEE("员工", 1),

    CORPORATIONUSER("企业用户", 2);

    private String name;

    private int index;

    private UserType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (UserType userType : UserType.values()) {
            if (userType.getIndex() == index) {
                return userType.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (UserType userType : UserType.values()) {
            if (userType.getName().equals(name)) {
                return userType.index;
            }
        }
        return null;
    }

    public static UserType getUserType(int index) {
        for (UserType userType : UserType.values()) {
            if (userType.getIndex() == index) {
                return userType;
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

    public static void main(String[] args) {
        getName(0);
    }
}