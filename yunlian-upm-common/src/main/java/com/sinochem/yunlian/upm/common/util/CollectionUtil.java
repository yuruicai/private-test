package com.sinochem.yunlian.upm.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static <T> List<T> generatePropertyList(Collection<?> collection, String property) {
        assert property != null;
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<T>(0);
        }
        List<T> list = new ArrayList<T>(collection.size());
        try {
            for (Object obj : collection) {
                Field field = obj.getClass().getDeclaredField(property);
                field.setAccessible(true);
                Object object = field.get(obj);
                list.add((T) object);
            }
            return list;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
