package com.sinochem.yunlian.upm.admin.constant;

/**
 * @author yangguo03
 * @version 1.0
 * @created 13-11-21
 */
public enum ContextAttrType {
    ORGID("orgId"), // 上下文，组织结构ID
    APP("appId"),   // 权限管理员，应用ID
    CUSTOM("custom");      // 自定义，具体含义由业务方定义

    private String code;

    private ContextAttrType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ContextAttrType getType(String code) {
        for (ContextAttrType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
