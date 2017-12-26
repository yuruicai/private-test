package com.sinochem.yunlian.upm.sso.constant;

/**
 * Created by yzq on 2017/11/19.
 */
public enum RedisKey {

    USER_ID("yumlian_upm_user_id"),
    USER_FIELD("yumlian_upm_user_field");

    private final String key;

    private RedisKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


}
