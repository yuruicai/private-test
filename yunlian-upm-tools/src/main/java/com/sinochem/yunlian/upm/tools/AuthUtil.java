package com.sinochem.yunlian.upm.tools;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zhangxi
 * @created 13-3-7
 */
class AuthUtil {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * @param uri
     * @param method
     * @param date     时间格式"EEE, d MMM yyyy HH:mm:ss z"
     * @param clientId
     * @param secret
     * @return
     */
    public static String getAuthorization(String uri, String method, String date, String clientId, String secret) {
        String stringToSign = method + " " + uri + "\n" + date;
        String signature = getSignature(stringToSign, secret);
        String authorization = "MWS" + " " + clientId + ":" + signature;
        return authorization;
    }

    public static String getSignature(String data, String secret) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            BASE64Encoder base64 = new BASE64Encoder();
            result = base64.encode(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
}
