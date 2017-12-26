package com.sinochem.yunlian.upm.sso.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * aes 加密实现
 *
 * @author hexiufeng
 * @version 1.0
 * @created 2011-11-21
 */
public class AesCoder {
    static final String KEY_ALGORITHM = "AES";
    static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    static final int KEY_LEN = 128;


    /**
     * 转换base64编码的字符串key成标准的key
     *
     * @param base64Key
     * @return
     */
    private static Key toKey(String base64Key) {
        SecretKey key = new SecretKeySpec(Base64.decodeBase64(base64Key), KEY_ALGORITHM);
        return key;
    }

    /**
     * 生成aes加密key
     *
     * @return
     * @throws Exception
     * @throws Exception
     * @throws NoSuchProviderException
     */
    public static byte[] genKey() throws Exception {
        KeyGenerator keyGen;

        keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGen.init(KEY_LEN);
        Key desKey = keyGen.generateKey();
        byte[] keydata = desKey.getEncoded();
        return keydata;
    }

    /**
     * 生成向量
     *
     * @return
     * @throws Exception
     */
    public static byte[] genIV() throws Exception {
        int size = calIvSize();
        byte[] iv = new byte[size];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        return iv;
    }

    private static int calIvSize() {
        return KEY_LEN / 8;
    }

    /**
     * 加密
     *
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String k, String iv) throws Exception {
        Key key = toKey(k);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
//        AlgorithmParameters params = cipher.getParameters();
//        byte[] ivs = params.getParameterSpec(IvParameterSpec.class).getIV();
//        System.out.println("ivs: " + Base64.encodeBase64String(ivs));
        //IvParameterSpec ivSpec = new IvParameterSpec(ivs);
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.decodeBase64(iv));
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data
     * @param k
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String k, String iv) throws Exception {
        Key key = toKey(k);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.decodeBase64(iv));
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, byte[] k, byte[] iv) throws Exception {
        Key key = new SecretKeySpec(k, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal(data);
    }
}
