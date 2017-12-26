package com.sinochem.yunlian.upm.sso.util;

/**
 * @author yanzhouqing
 * @created 2017/12/7
 * @Description 检查Request请求是否来自移动端
 */
public class CheckUaUtil {
    public static boolean  isMobileDevice(String ua){
        /**
         * android : 所有android设备
         * mac os : iphone,ipad,iphone
         * windows phone:Nokia等windows系统的手机
         */
        String[] deviceArray = new String[]{"android","mac os","windows phone","iphone","ipod","ipad"};
        if(ua == null){return false;}
        ua = ua.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(ua.indexOf(deviceArray[i])!=-1){
                return true;
            }
        }
        return false;
    }
}
