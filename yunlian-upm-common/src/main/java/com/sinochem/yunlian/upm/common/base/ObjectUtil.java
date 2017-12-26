package com.sinochem.yunlian.upm.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhangxi
 * @created 13-2-7
 */
public class ObjectUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectUtil.class);

    private ObjectUtil() {
    }

    public static <T> T defaults(T obj, T defaultValue) {
        if (obj == null) {
            return defaultValue;
        } else {
            return obj;
        }
    }

    /**
     * 设置对象field为null的默认值
     * <p/>
     * String = "", Integer=0, Long = 0, Boolean = false, Date = now()
     *
     * @param obj
     * @param excludes
     */
    public static <T> T defaultValue(T obj, String... excludes) {
        HashSet<String> excludeFields = new HashSet<String>();
        if (excludes != null) {
            excludeFields.addAll(Arrays.asList(excludes));
        }
        if (obj == null) {
            return null;
        }
        Object value = defaults(obj.getClass());
        if (value != null) {
            return (T) value;
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                if (excludeFields.contains(field.getName())) {
                    continue;
                } else {
                    field.setAccessible(true);
                    Object object = field.get(obj);
                    if (object == null) {
                        Class<?> type = field.getType();
                        value = defaults(type);
                        if (value != null) {
                            field.set(obj, value);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return obj;
    }

    private static Object defaults(Class<?> type) {
        if (type == boolean.class || type == Boolean.class) {
            return false;
        } else if (type == char.class || type == Character.class) {
            return '\u0000';
        } else if (type == byte.class || type == Byte.class) {
            byte b = 0;
            return Byte.valueOf(b);
        } else if (type == short.class || type == Short.class) {
            return 0;
        } else if (type == int.class || type == Integer.class) {
            return 0;
        } else if (type == long.class || type == Long.class) {
            return 0L;
        } else if (type == float.class || type == Float.class) {
            return 0.f;
        } else if (type == double.class || type == Double.class) {
            return 0.d;
        } else if (String.class.isAssignableFrom(type)) {
            return "";
        } else if (type.isAssignableFrom(Date.class)) {
            return new Date();
        } else if (type.isArray()) {
            Class<?> real = type;
            return Array.newInstance(real.getComponentType(), 0);
        } else if (List.class.isAssignableFrom(type)) {
            return Collections.emptyList();
        } else if (Set.class.isAssignableFrom(type)) {
            return Collections.emptySet();
        } else if (Map.class.isAssignableFrom(type)) {
            return Collections.emptyMap();
        }
        return null;
    }

    /**
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 转换为字符串
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Object obj) {
        // 先拿反射实现
        StringBuilder sb = new StringBuilder();
        if (obj == null) {
            return "null";
        }
        try {
            if (obj.getClass().isPrimitive() || obj instanceof String || obj instanceof Integer
                    || obj instanceof Long || obj instanceof Boolean) {
                return obj.toString();
            }
            if (obj instanceof Map) {
                Map map = (Map) obj;
                return toString(map);
            } else if (obj instanceof Collection) {
                Collection cls = (Collection) obj;
                return toString(cls);
            } else if (obj instanceof Object[]) {
                for (Object o : (Object[]) obj) {
                    if (o.getClass().isPrimitive() || o instanceof String || o instanceof Integer
                            || o instanceof Long || o instanceof Boolean) {
                        sb.append(o + ",");
                    } else {
                        sb.append(toString(o) + ",");
                    }
                }
            }
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object object = field.get(obj);
                if (object != null) {
                    sb.append("\"" + field.getName() + "\":");
                    if (object instanceof String) {
                        sb.append("\"" + object + "\",");
                    } else {
                        sb.append(object + ",");
                    }
                }
            }
            return sb.length() == 0 ? "{}" : "{" + sb.substring(0, sb.length() - 1) + "}";
        } catch (Exception e) {
            LOG.warn("can't toString for " + obj);
            return obj.toString();
        }
    }

    /**
     * 集合
     *
     * @param objs
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Collection objs) {
        // 先拿反射实现
        StringBuilder sb = new StringBuilder();
        if (objs == null) {
            return "null";
        }
        for (Object obj : objs) {
            if (obj.getClass().isPrimitive()) {
                sb.append(obj + ",");
            } else {
                sb.append(toString(obj) + ",");
            }
        }
        return sb.length() == 0 ? "[]" : "[" + sb.substring(0, sb.length() - 1) + "]";
    }

    /**
     * Map
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Map map) {
        // 先拿反射实现
        StringBuilder sb = new StringBuilder();
        if (map == null) {
            return "null";
        }
        for (Object entry : map.entrySet()) {
            if (entry instanceof Map.Entry) {
                sb.append("\"" + toString(((Map.Entry) entry).getKey()) + "\":" + toString(((Map.Entry) entry).getValue()) + ",");
            }
        }
        return sb.length() == 0 ? "{}" : "{" + sb.substring(0, sb.length() - 1) + "}";
    }
}
