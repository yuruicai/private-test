package com.yunlian.upm;

import com.sinochem.yunlian.upm.sso.util.SecurityUtils;
import org.junit.Test;

/**
 * Created by zhanghongze on 2015/11/18.
 */
public class PasswordTest {

    @Test
    public void generatePassword(){
        System.out.println(SecurityUtils.getPassphrase("1111111111", "123456"));
    }
}
