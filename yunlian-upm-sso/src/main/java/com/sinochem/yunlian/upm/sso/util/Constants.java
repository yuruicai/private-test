package com.sinochem.yunlian.upm.sso.util;

/**
 * @author zhangxi
 * @created 13-12-20
 */
public class Constants {
    public static final String key = "1234567890-=qwertyuiop[]asdfghjkl;'zxcvbnm,./!@#$%^&*()_+QWERTYUIOP{}ASDFGHJKL:ZXCVBNM<>?";

    public static final String UUID_KEY = "skmtuuid";
    public static final String SEC_UTC_KEY = "skmtutc";

    public static int DAY_SECOND = 3600 * 24;
    public static int UUID_MAX_AGE = 3600 * 24 * 365 * 5;
    public static int SEC_UTC_MAX_AGE = 3600 * 24 * 365 * 5;

    public static final String CONTEXT_USER_ID = "uid";
    public static final String CONTEXT_TOKEN = "token";
    public static final String CONTEXT_USER_NAME = "username";
    public static final String CONTEXT_UUID_KEY = "uuid";
    public static final String CONTEXT_IP_KEY = "ip";
    public static final String CONTEXT_UA_KEY = "ua";
    public static final String CONTEXT_REFERER_KEY = "Referer";
    public static final String CONTEXT_ISMOBILE_KEY = "isMobile";
    public static final String CONTEXT_OS_KEY = "os";
    public static final String CONTEXT_SCREEN_KEY = "screen";
    public static final String CONTEXT_SERVICE_KEY = "service";
    public static final String CONTEXT_LOGIN_TYPE_KEY = "loginType";
    public static final String CONTEXT_CLIENT_ID = "clientId";


    public static final Integer SESSION_TIMEOUT = 3600*24*7;
    public static final Integer PC_SESSION_TIMEOUT = 3600*24*7;
    public static final Integer APP_SESSION_TIMEOUT = 3600*2;
    public static final Integer TICKET_TIMEOUT = 300;
    public static final Integer SESSION_COOKIE_TIMEOUT = SESSION_TIMEOUT * 2;

    public static final String TICKET_KEY_PREFIX = "sso.ticket.";

    public static final String DEFAULT_SALT = "";

}
