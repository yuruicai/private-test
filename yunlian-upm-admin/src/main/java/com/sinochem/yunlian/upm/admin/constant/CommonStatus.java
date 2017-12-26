package com.sinochem.yunlian.upm.admin.constant;

/**
 * 通用状态，只包含启用和删除
 * 
 * @author wangwei
 * @version 1.0
 * @created 2013-05-10
 */
public enum CommonStatus {

    ACTIVE("启用", 0),

    MODIFY("修改", 64),

    DELETE("删除", 128);

    private String name;

    private int index;

    private CommonStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (CommonStatus menuStatus : CommonStatus.values()) {
            if (menuStatus.getIndex() == index) {
                return menuStatus.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (CommonStatus menuStatus : CommonStatus.values()) {
            if (menuStatus.getName().equals(name)) {
                return menuStatus.index;
            }
        }
        return null;
    }

    public static CommonStatus getMenuStatus(int index) {
        for (CommonStatus menuStatus : CommonStatus.values()) {
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
