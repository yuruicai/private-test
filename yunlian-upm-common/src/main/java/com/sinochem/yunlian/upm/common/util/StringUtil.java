package com.sinochem.yunlian.upm.common.util;

import java.util.List;

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
}
