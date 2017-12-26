package com.sinochem.yunlian.upm.common.rpc;

/**
 * @author zhangxi
 * @created 13-3-5
 */
public enum MtError {

    /**
     * 服务错误,10***
     * 服务器内部错误(自定义的异常)
     */
    serviceUnknownError(10000, Type.service, "未知原因"),
    serviceUnavailable(10001, Type.service, "服务不可用"),
    serviceInternelException(10002, Type.service, "服务内部异常"),
    serviceTooBusy(10003, Type.service, "服务繁忙"),
    serviceTimeout(10004, Type.service, "服务超时"),
    serviceNotFound(10005, Type.service, "服务不存在"),
    serviceUnsupportCall(10006, Type.service, "不支持的调用方式,请参考API文档"),
    serviceDeprecated(10007, Type.service, "服务已弃用"),

    /**
     * 参数错误,11***
     * 请求参数错误（对GET和POST请求中携带的参数的校验结果）
     */

    //api文档相关,110**
    paramError(11000, Type.param, "请参考API文档"),
    paramInvalid(11001, Type.param, "非法参数(%s),请参考API文档"),
    paramUnsupportMediaType(11002, Type.param, "不支持的MediaType(%s)"),
    paramInvalidFormat(11003, Type.param, "无效数据格式,请参考API文档"),
    paramInvalidEncode(11004, Type.param, "编码错误,请参考API文档"),

    //缺少参数,111**
    paramNeedAppkey(11100, Type.param, "缺少appkey"),
    paramNeedApikey(11101, Type.param, "缺少apikey"),
    paramNeedSign(11102, Type.param, "缺少sign"),
    paramNeedTimestamp(11103, Type.param, "缺少时间戳timestamp"),
    paramNeedRequired(11104, Type.param, "缺少必选参数(%s),请参考API文档"),
    paramNeedToken(11105, Type.param, "缺少token"),

    //无效参数,112**
    paramInvalidAppkey(11200, Type.param, "无效appkey"),
    paramInvalidApikey(11201, Type.param, "无效apikey"),
    paramInvalidSign(11202, Type.param, "无效签名,请参考API文档"),
    paramInvalidTimestamp(11203, Type.param, "无效时间戳,请参考API文档"),
    paramInvalidMobile(11204, Type.param, "无效的手机号"),
    paramInvalidCaptcha(11205, Type.param, "无效的图片验证码"),
    paramInvalidssmCaptcha(11206, Type.param, "无效的短信验证码"),

    //失败,113**
    paramFailedSendssmCaptcha(11300, Type.param, "短信验证码发送失败"),




    /**
     * 权限安全,12***
     */
    accessForbidden(12000, Type.access, "非法访问"),
    accessIpForbidden(12001, Type.access, "IP访问受限"),
    accessAppForbidden(12002, Type.access, "APP访问受限"),
    accessAppOverQuota(12003, Type.access, "APP访问配额超限"),
    accessAppTooFrequently(12004, Type.access, "APP访问过于频繁"),
    accessSessionForbidden(12005, Type.access, "会话访问受限"),
    accessSessionOverQuota(12006, Type.access, "会话访问配额超限"),
    accessSessionTooFrequently(12007, Type.access, "会话访问过于频繁"),
    accessUserInactive(12008, Type.access, "用户未启用,请联系管理员"),
    /**
     * 其它
     */
    other(19000, Type.other, "请参考API文档");

    public enum Type {
        service("服务错误"), param("参数错误"), access("权限安全"), application("应用错误"), other("其它");
        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private int code;
    private Type type;
    private String message;

    MtError(int code, Type type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
