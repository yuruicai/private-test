package com.sinochem.yunlian.upm.filter.exception;

/**
 * @author zhangxi
 * @created 13-6-24
 */
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super();
    }

    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UnAuthorizedException(Throwable throwable) {
        super(throwable);
    }
}
