package com.sinochem.yunlian.upm.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * @author zhangxi
 * @created 13-3-28
 */
public class StringUtil {

    private StringUtil() {
    }

    public static String list2SqlString(List<?> list) {
        if (list.size() == 0) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0, n = list.size(); i < n; i++) {
            Object obj = list.get(i);
            if (obj instanceof Integer) {
                out.append(obj + ",");
            } else if (obj instanceof String) {
                out.append("'" + obj + "',");
            } else {
                out.append("'" + obj.toString() + "',");
            }
        }
        return out.substring(0, out.length() - 1);
    }

    public final static String getUuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("http://1.203.115.214:8888/yunlian-ku/yunlian-upm","utf-8"));
    }
}
