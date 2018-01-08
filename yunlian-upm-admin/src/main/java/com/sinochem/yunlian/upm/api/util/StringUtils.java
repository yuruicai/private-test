package com.sinochem.yunlian.upm.api.util;

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


}
