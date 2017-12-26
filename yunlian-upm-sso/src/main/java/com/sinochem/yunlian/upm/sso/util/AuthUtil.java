package com.sinochem.yunlian.upm.sso.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: zhanghongze
 * Date: 14-5-16
 * Time: 上午11:14
 * To change this template use File | Settings | File Templates.
 */
public class AuthUtil {

    public static boolean auth(HttpServletRequest request,String clientId,String secret){
        try {
            String uri = request.getRequestURI();
            String method = request.getMethod();
            String dateStr = request.getHeader("Date");
            String auth = request.getHeader("Authorization");

            String clientId2 = auth.substring(auth.indexOf(" ") + 1, auth.indexOf(":"));
            if(!clientId.equals(clientId2)){
                return false;
            }

            String auth2 = AuthUtil.getAuthorization(uri, method, dateStr, clientId, secret);
            if (auth.equals(auth2)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String getAuthorization(String uri, String method, String dateStr, String clientId,
                                   String secret) {
        String stringToSign = method + " " + uri + "\n" + dateStr;
        String signature = HMACSHA1.getSignature(stringToSign, secret);
        return "MWS" + " " + clientId + ":" + signature;
    }
}
