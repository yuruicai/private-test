package com.sinochem.yunlian.upm.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangxi
 * @created 13-5-27
 */
public class SSOUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SSOUtil.class);

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder();
        sb.append(RandomStringUtils.random(2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        sb.append(RandomStringUtils.random(4, "abcdefghijklmnopqrstuvwxyz"));
        sb.append(RandomStringUtils.random(1, "!@#$&*_+"));
        sb.append(RandomStringUtils.random(3, "23456789"));
        return sb.toString();
    }
}
