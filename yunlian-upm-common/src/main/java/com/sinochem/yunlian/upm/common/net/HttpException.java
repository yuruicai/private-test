package com.sinochem.yunlian.upm.common.net;

public class HttpException extends RuntimeException {

    public HttpException(String s) {
        super(s);
    }

    public HttpException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }
}