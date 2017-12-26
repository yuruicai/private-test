package com.sinochem.yunlian.upm.common.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author zhangxi
 * @created 13-1-25
 */
public class TypeReference<T> {

    private final Type type;

    public TypeReference() {
        Type superClass = getClass().getGenericSuperclass();

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    public static final Type LIST_STRING = new TypeReference<List<String>>() {
    }.getType();
}
