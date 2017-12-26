package com.sinochem.yunlian.upm.admin.constant;

/**
 * 菜单状态
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-10
 */
public enum MenuStatus {


    ACTIVE("启用", 0),

    DELETE("删除", 128);


    private String name;

    private int index;

    private MenuStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (MenuStatus menuStatus : MenuStatus.values()) {
            if (menuStatus.getIndex() == index) {
                return menuStatus.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (MenuStatus menuStatus : MenuStatus.values()) {
            if (menuStatus.getName().equals(name)) {
                return menuStatus.index;
            }
        }
        return null;
    }

    public static MenuStatus getMenuStatus(int index) {
        for (MenuStatus menuStatus : MenuStatus.values()) {
            if (menuStatus.getIndex() == index) {
                return menuStatus;
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