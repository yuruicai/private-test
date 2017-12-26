package com.sinochem.yunlian.upm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.security.GeneralSecurityException;

@Slf4j
public class CommonConvert {

    private static TextCipher cipher;
    //手机号码校验
    public static final String MOBILE_PATTERN = "\\d{11}";
    //身份证号码校验
    public static final String IDNUMBER_PATTERN = "\\d{18}|\\d{17}X|\\d{17}x";

    static{
        String salt= SecurityUtils.readSaltFile();
        cipher = new DESTextCipher();
        cipher.init(salt);
    }


    /**
     * 对手机号码进行加密
     *
     * @param target
     * @return
     */
    public static String encryptMobile(String target) {
        if (isHasEncryptionMobile(target)) {
            return target;
        }
        return encrypt(target);
    }

    /**
     * 对身份证进行加密
     *
     * @param target
     * @return
     */
    public static String encryptIdNumber(String target) {
        if (isHasEncryptionIdNumber(target)) {
            return target;
        }
        return encrypt(target);
    }

    /**
     * 对身份证进行解密
     *
     * @param target
     * @return
     */
    public static String decryptIdNumber(String target) {
        if (!isHasEncryptionIdNumber(target)) {
            return target;
        }
        return decrypt(target);
    }

    /**
     * 对手机号码进行解密
     *
     * @param target
     * @return
     */
    public static String decryptMobile(String target) {
        if (!isHasEncryptionMobile(target)) {
            return target;
        }
        return decrypt(target);
    }

    /**
     * 对字段进行加密
     *
     * @param target
     * @return
     */
    private static String encrypt(String target) {
        try {
            return cipher.encrypt(target);
        } catch (GeneralSecurityException ex) {
            log.error("Can't encrypt objectValue.[objectValue={}]", target, ex);
            return target;
        }
    }

    /**
     * 对字段进行解密
     *
     * @param target
     * @return
     */
    private static String decrypt(String target) {
        try {
            return cipher.decrypt(target);
        } catch (Exception ex) {
            log.error("Can't encrypt objectValue.[objectValue={}]", target, ex);
            System.out.println(ex.getMessage());
            return target;
        }
    }


    /**
     * 手机号码是否已加密
     *
     * @param mobile
     * @return
     */
    public static boolean isHasEncryptionMobile(String mobile) {
        return isHasEncryption(mobile, MOBILE_PATTERN);
    }

    /**
     * 身份证是否已加密
     *
     * @param idnumber
     * @return
     */
    public static boolean isHasEncryptionIdNumber(String idnumber) {
        return isHasEncryption(idnumber, IDNUMBER_PATTERN);
    }

    private static boolean isHasEncryption(String target, String pattern) {
        if (StringUtils.isBlank(target)) {
            return true;
        }
        return !target.matches(pattern);
    }

    public static void main(String[] args) throws Exception{
        String idNumber = "372502198311102779";
        String salt = "MkMwMzExNTItNjFvMnctbTFzNXMgUOROuEeKZuVkjXeIUzFpMU86cTEz";
        CommonConvert commonConvert = new CommonConvert();

        String mobile = "18910270829";
        System.out.println("salt:" + salt);
        System.out.println(commonConvert.encryptMobile(mobile));
        System.out.println(commonConvert.decryptMobile("zkUxgaSz8Piae8xNuE7rVA=="));
        System.out.println(commonConvert.decryptIdNumber("6ILwf+6S4h8FU5IIb1qNEwwLgOAlh6uZ"));
    }
}
