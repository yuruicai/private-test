package com.sinochem.yunlian.upm.api.domain;

/**
 * 用户类型
 * 1：货主、2：物流公司、3：车主'
 */
public enum UserType {

    EMPLOYEE("货主", 1),
    CORPORATIONUSER("物流公司", 2),
    CAROWNER("车主", 3);


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
