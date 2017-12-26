package com.sinochem.yunlian.upm.sso.model;

import org.apache.shiro.web.servlet.SimpleCookie;

/**
 * @author zhangxi
 * @created 13-5-23
 */
public class MtCookie extends SimpleCookie {
    public static final String DEFAULT_SESSION_ID_NAME = "skmtusid";

    public MtCookie() {
        super(DEFAULT_SESSION_ID_NAME);
        setHttpOnly(true);
    }

    public MtCookie(String name) {
        super(name);
        setHttpOnly(true);
    }

    public static MtCookie newCookie() {
        return new MtCookie();
    }

    public MtCookie value(String value) {
        setValue(value);
        return this;
    }
}
