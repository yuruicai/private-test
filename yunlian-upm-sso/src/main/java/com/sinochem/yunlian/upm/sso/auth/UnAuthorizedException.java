package com.sinochem.yunlian.upm.sso.auth;

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
