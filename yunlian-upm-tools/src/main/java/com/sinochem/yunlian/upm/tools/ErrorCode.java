package com.sinochem.yunlian.upm.tools;

/**
 * @author zhangxi
 * @created 13-1-16
 */
class ErrorCode {
    private int code;
    private String type;
    private String message;

    public ErrorCode() {
    }

    public ErrorCode(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "code=" + code + ",type=" + type + ",message=" + message;
    }
}
