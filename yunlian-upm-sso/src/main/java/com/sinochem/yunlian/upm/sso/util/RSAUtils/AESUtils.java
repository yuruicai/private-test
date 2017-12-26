package com.sinochem.yunlian.upm.sso.util.RSAUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

/**
 * AES工具类，密钥必须是16位字符串
 */
public class AESUtils {

	/**偏移量,必须是16位字符串*/
    private static final String IV_STRING = "16-Bytes--String";

    /**
     * 默认的密钥
     */
    public static final String DEFAULT_KEY = "1bd83b249a414036";

    /**
     * 产生随机密钥(这里产生密钥必须是16位)
     */
    public static String generateKey() {
        String key = UUID.randomUUID().toString();
        key = key.replace("-", "").substring(0, 16);// 替换掉-号
        return key;
    }

    public static String encryptData(String key, String content) {
        byte[] encryptedBytes = new byte[0];
        try {
            byte[] byteContent = content.getBytes("UTF-8");
            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = key.getBytes();

            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
            // 同样对加密后数据进行 base64 编码
            return Base64Utils.encode(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptData(String key, String content) {
        try {
            // base64 解码
            byte[] encryptedBytes = Base64Utils.decode(content);
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
		/*String plainText = AESUtils.decryptData("5005AD054E4EB075", "EVydDv5Sxg8uPfw2GuZ/bQ==");
		System.out.println("aes加密后: " + plainText);*/
        /*String s = encryptData("D1111B111111111A", "测试");
        System.out.println(s);
        String plainText = decryptData("D1111B111111111A", s);
        System.out.println("aes加密后: " + plainText);*/


        //Android
        String randomKeyAZ =
                "CflcZsMOFwzI84jPAjmMcb1X8JPII6ROGpzyvCwmfOjwgBPOW9D9MaMpAR24HkYXP+nuF5XQNQKypPSQsxDApgJCsWQA/oOnE3MJxaWjpw8OkQsevapgAnrercgAHUb3u8XDHdcLKuWYFqMVbCt7+JwA7jb+7tW7ZGZcPRbAYHs=";
        String keyAZ = RSAUtils.decryptByPrivateKey(randomKeyAZ);
        System.out.println("AN加密key:"+keyAZ);
        String contentAZ = "IO8ZKqxaF4mYOZtIQlffJg==";
        String plainTextAZ = decryptData(keyAZ, contentAZ);
        //String encryptData = encryptData(keyAZ, "asdf1234");
        System.out.println("AN解密数据: " + plainTextAZ);
        //System.out.println("AN加密数据: " + encryptData);
        //密码,key
        //String password = AESUtils.decryptData(contentAZ, "8E6CDA64565E3718");
	}
    
}
