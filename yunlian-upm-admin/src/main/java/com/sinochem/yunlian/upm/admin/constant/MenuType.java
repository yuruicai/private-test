package com.sinochem.yunlian.upm.admin.constant;

/**
 * 菜单类型
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-10
 */
public enum  MenuType {


    CURRENT("在原页面打开", 1),

    NEW("在新页面打开", 2);


    private String name;

    private int index;

    private MenuType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (MenuType menuType : MenuType.values()) {
            if (menuType.getIndex() == index) {
                return menuType.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (MenuType menuType : MenuType.values()) {
            if (menuType.getName().equals(name)) {
                return menuType.index;
            }
        }
        return null;
    }

    public static MenuType getMenuType(int index) {
        for (MenuType menuType : MenuType.values()) {
            if (menuType.getIndex() == index) {
                return menuType;
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