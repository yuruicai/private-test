package com.sinochem.yunlian.upm.api.util;

import java.util.List;
import java.util.UUID;

/**
 * @author huangyang
 * @Description:
 * @date 2018/01/08 下午2:52
 */
public class StringUtils {

    public static final  String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String listToString(List list, String  separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }

}
