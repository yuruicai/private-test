package com.sinochem.yunlian.upm.common.rpc;

import com.google.common.base.Objects;

/**
 * 服务调用异常及出错信息
 */
public class InvokeException extends RuntimeException {
    private int code;
    private String type;
    private String message;

    public InvokeException(String message) {
        super(message);
        this.code = MtError.other.getCode();
        this.type = MtError.Type.other.getName();
        this.message = message;
    }

    public InvokeException(ErrorCode error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.type = error.getType();
        this.message = error.getMessage();
    }

    public InvokeException(ErrorCode error, Throwable throwable) {
        super(error.getMessage(), throwable);
        this.code = error.getCode();
        this.type = error.getType();
        this.message = error.getMessage();
    }

    public InvokeException(String message, Throwable throwable) {
        super(message, throwable);
        this.code = MtError.other.getCode();
        this.type = MtError.Type.other.getName();
        this.message = message;
    }

    public InvokeException(Throwable throwable) {
        super(throwable);
        this.code = MtError.other.getCode();
        this.type = MtError.Type.other.getName();
        this.message = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("code", code).add("type", type).add("message", message).toString();
    }
}