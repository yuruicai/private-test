package com.sinochem.yunlian.upm.tools;

import org.junit.Test;

/**
 * @author zhangxi
 * @created 13-7-26
 */
public class UpmAuthServiceTest {

    @Test
    public void testLogout() {
        UpmAuthService authService = new UpmAuthService();
        String host = "http://localhost:8080";
        authService.setLoginUrl(host);
        authService.logout("fsdfsdafds");
    }

}
