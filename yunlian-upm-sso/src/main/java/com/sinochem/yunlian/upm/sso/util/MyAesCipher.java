package com.sinochem.yunlian.upm.sso.util;

import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.JcaCipherService;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author zhangxi
 * @created 14-2-20
 */
public class MyAesCipher extends AesCipherService {

    /**
     * use reflect to assign iv from outside,
     *
     * @param plaintext
     * @param key
     * @param iv
     * @return cipher no prependIv
     */
    public ByteSource encrypt(byte[] plaintext, byte[] key, byte[] iv) {
        Method method = ReflectionUtils.findMethod(JcaCipherService.class, "encrypt",
                byte[].class, byte[].class, byte[].class, boolean.class);
        method.setAccessible(true);
        return (ByteSource) ReflectionUtils.invokeMethod(method, this, plaintext, key, iv, false);
    }
}
