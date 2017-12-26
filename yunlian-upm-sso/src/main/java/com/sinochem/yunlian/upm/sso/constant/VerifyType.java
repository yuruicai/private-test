package com.sinochem.yunlian.upm.sso.constant;

/**
 *
 *
 * @author wangwei
 * @version 1.0
 * @created 2013-05-02
 */
public enum VerifyType {

    OTP("大象动态口令", "otp"),
    MOBILE("短信验证", "mobile");

    private String name;

    private String index;

    private VerifyType(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(String index) {
        for (VerifyType type : VerifyType.values()) {
            if (type.getIndex().equals(index)) {
                return type.name;
            }
        }
        return null;
    }

    public static VerifyType getLoginCaptchaType(String index) {
        for (VerifyType type : VerifyType.values()) {
            if (type.getIndex().equals(index)) {
                return type;
            }
        }
        return null;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}