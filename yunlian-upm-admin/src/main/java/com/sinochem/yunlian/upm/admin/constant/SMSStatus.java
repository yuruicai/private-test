package com.sinochem.yunlian.upm.admin.constant;

/**
 * Created by huaijin.gao on 2015/8/26.
 */
public enum SMSStatus {
    SUCCESS("s001"),//发送成功
    CAPTCHA_OK("s002"),//校验码匹配成功
    FAILURE("f001"),//发送失败
    FREQUENTLY("f002"),//发送过于频繁(1分钟内同一业务，同一目标对象（ext参数控制）只能发送1-n次)
    CAPTCHA_NOT_MATCH("f003");//码校验不匹配

    private final String key;

    private SMSStatus(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static SMSStatus getSMSStatus(String key) {
        for(SMSStatus smsStatus : SMSStatus.values()){
            if(smsStatus.getKey().equals(key)){
                return smsStatus;
            }
        }
        return null;
    }
}
