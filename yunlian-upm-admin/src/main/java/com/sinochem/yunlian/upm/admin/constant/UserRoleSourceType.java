package com.sinochem.yunlian.upm.admin.constant;

/**
 * 菜单类型
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-10
 *
 * 不同来源的sourceRltId设置不同：
 *      type            sourceRltId
 *      POSAUTO         posId
 *      MANUAL          userId
 *      APP             roleId
 *      ORGAUTO         orgId
 *      ORGPOSAUTO      orgPositionRoleRltId
 */
public enum UserRoleSourceType {


    POSAUTO("岗位自动授权", 1),

    MANUAL("手动授权", 2),

    APP("应用默认角色", 3),

    ORGAUTO("组织自动授权", 4),

    ORGPOSAUTO("组织下岗位自动授权", 5),

    PHOTO("摄影师", 6), // 摄影师：UPM-327，sourceRltId=327

    CONTACT_CHARGE("联络点负责人", 7); // UPM-370 sourceRltId=contactPointOrgId


    private String name;

    private int index;

    private UserRoleSourceType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (UserRoleSourceType userRoleSourceType : UserRoleSourceType.values()) {
            if (userRoleSourceType.getIndex() == index) {
                return userRoleSourceType.name;
            }
        }
        return null;
    }

    public static Integer getIndex(String name) {
        for (UserRoleSourceType userRoleSourceType : UserRoleSourceType.values()) {
            if (userRoleSourceType.getName().equals(name)) {
                return userRoleSourceType.index;
            }
        }
        return null;
    }

    public static UserRoleSourceType getUserRoleSourceType(int index) {
        for (UserRoleSourceType userRoleSourceType : UserRoleSourceType.values()) {
            if (userRoleSourceType.getIndex() == index) {
                return userRoleSourceType;
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