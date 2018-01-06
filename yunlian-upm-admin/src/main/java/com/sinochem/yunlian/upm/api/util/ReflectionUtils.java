package com.sinochem.yunlian.upm.api.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangyang
 * @Description: 反射帮助类
 * @date 2017/12/29 下午3:14
 */
public class ReflectionUtils {


    public static final <T> Map<String, Object> toMap(T obj) {

        if (obj == null) {
            return Collections.emptyMap();
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        try {
            Map<String, Object> objectMap = PropertyUtils.describe(obj);
            objectMap.remove("class");
            return objectMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把target中的非空属性，复制到dest对象中
     *
     * @param target 数据来源对象
     * @param dest
     */
    public static final <T> void copyNonNullProperties(T target, T dest) {

        if (target != null && dest != null) {
            try {
                Map<String, Object> objMap = toMap(target);
                Map<String, Object> newMap = new HashMap<>();
                objMap.forEach((k, v) -> {
                    if (v != null) {
                        newMap.put(k, v);
                    }
                });
                PropertyUtils.copyProperties(dest, newMap);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 判断对象所有的属性是否为null
     *
     * @param object
     * @return
     */
    public static final boolean isNullObject(Object object) {
        if (object == null) {
            return true;
        }
        try {
            Map<String, Object> objectMap = toMap(object);
            if (objectMap.isEmpty()) {
                return true;
            }
            for (Object obj : objectMap.values()) {
                if (obj != null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param object
     * @param propertyName set方法对应的属性名
     * @param arg
     */
    public static void invokeSetMethod(Object object, String propertyName, Object arg) {
        if (object == null || StringUtils.isEmpty(propertyName)) {
            return;
        }
        try {
            Method[] methods = object.getClass().getMethods();
            String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            for (Method method : methods) {
                if(method.getName().equals(methodName)){
                   method.invoke(object,arg);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
