package com.sinochem.yunlian.upm.api.exception;

/**
 * @author huangyang
 * @Description: 通用异常
 * @date 2017/12/28 下午2:19
 */
public class ApiException extends RuntimeException {

    private ApiException(String msg) {
        super(msg);
    }

    public static ApiException of(String msg) {
        return new ApiException(msg);
    }
}
