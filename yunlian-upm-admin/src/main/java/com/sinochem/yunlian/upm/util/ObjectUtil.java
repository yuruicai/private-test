package com.sinochem.yunlian.upm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class ObjectUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectUtil.class);

    /**
     * 根据source更新目标target，当且仅当字段存在且非null时更新目标值
     * 
     * @param source
     * @param target
     */
    public static void copy(Object source, Object target) {
        try {
            BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    /**
     * @param obj
     * @return
     */
    public static <T> T clone(T obj) {
        try {
            @SuppressWarnings("unchecked")
            T clone = (T) obj.getClass().newInstance();
            BeanUtils.copyProperties(obj, clone);
            return clone;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 动态组装对象.被组装的对象(toObject)中名称为assembleTrigger()方法可以被 触发.assembleTrigger不能有参数
     * <p/>
     * 注意:如果源对象中有需要延迟加载的get方法,当get时,如果session已经关闭 了,会有session关闭错误.这在关联对象之间比较常见
     * 
     * @param fromObj
     *            源对象
     * @param toObj
     *            目标对象
     * @param excludeFields
     *            toObj中不组装的属性.不跟属性直接打交道.只和get,set方法的名称直接关联
     * @return
     * @author zhaolei
     * @created 2011-4-28
     */
    @Deprecated
    public static <T> T assemble(Object fromObj, T toObj, String... excludeFields) {
        if (fromObj == null) {
            return toObj;
        }

        Method[] toMethods = toObj.getClass().getMethods();
        Method[] fromMethods = fromObj.getClass().getMethods();
        Map<String, Method> fromMethodMap = new HashMap<String, Method>();
        for (int i = 0; i < fromMethods.length; i++)
            fromMethodMap.put(fromMethods[i].getName(), fromMethods[i]);

        String regex = "";
        for (int i = 0; i < excludeFields.length; i++) {
            regex = regex + excludeFields[i];
            if (i < excludeFields.length - 1)
                regex = regex + "|";
        }
        for (int i = 0; i < toMethods.length; i++) {
            Method m = toMethods[i];
            String mName = m.getName();
            try {
                if (mName.startsWith("set") && !mName.equals("set")) {
                    Type paramType = m.getGenericParameterTypes()[0];
                    String fName = mName.replaceFirst("set", "");
                    fName = fName.replaceFirst(fName.substring(0, 1), fName.substring(0, 1)
                            .toLowerCase());
                    if (!fName.matches(regex)) {
                        Method fm = fromMethodMap.get(mName.replaceFirst("set", "get"));
                        if (fm != null) {
                            if (fm.getReturnType().equals(paramType)) {
                                m.invoke(toObj, fm.invoke(fromObj));
                            }
                        }
                    }
                } else if (mName.equals("assembleTrigger")) {
                    m.invoke(toObj);
                }
            } catch (Exception e) {
                System.out.println(ObjectUtil.class + " error!");
                e.printStackTrace();
            }
        }
        return toObj;
    }

    /**
     * 动态组装对象,源对象为空(null)的值忽略,可以触发toObject的名称为 assembleTrigger的方法
     * 
     * @param fromObj
     *            源对象
     * @param toObj
     *            目标对象
     * @param excludeFields
     *            toObj中不组装的属性.不跟属性直接打交道.只和get,set方法的名称直接关联
     * @return
     * @author zhaolei
     * @created 2011-4-28
     */
    @Deprecated
    public static <T> T assembleNotNull(Object fromObj, T toObj, String... excludeFields) {
        if (fromObj == null) {
            return toObj;
        }

        Method[] toMethods = toObj.getClass().getMethods();
        Method[] fromMethods = fromObj.getClass().getMethods();
        Map<String, Method> fromMethodMap = new HashMap<String, Method>();
        for (int i = 0; i < fromMethods.length; i++)
            fromMethodMap.put(fromMethods[i].getName(), fromMethods[i]);
        String regex = "";
        for (int i = 0; i < excludeFields.length; i++) {
            regex = regex + excludeFields[i];
            if (i < excludeFields.length - 1)
                regex = regex + "|";
        }
        for (int i = 0; i < toMethods.length; i++) {
            Method m = toMethods[i];
            String mName = m.getName();

            try {
                if (mName.startsWith("set") && !mName.equals("set")) {
                    Type paramType = m.getGenericParameterTypes()[0];
                    String fName = mName.replaceFirst("set", "");
                    fName = fName.replaceFirst(fName.substring(0, 1), fName.substring(0, 1)
                            .toLowerCase());
                    if (!fName.matches(regex)) {
                        Method fm = fromMethodMap.get(mName.replaceFirst("set", "get"));
                        if (fm != null) {
                            if (fm.getReturnType().equals(paramType)) {
                                Object value = fm.invoke(fromObj);
                                if (value != null)
                                    m.invoke(toObj, value);
                            }
                        }
                    }
                } else if (mName.equals("assembleTrigger")) {
                    m.invoke(toObj);
                }
            } catch (Exception e) {
                System.out.println(ObjectUtil.class + " error!");
                e.printStackTrace();
            }
        }
        return toObj;
    }

    /**
     * 从源List装配一个符合目标class类型的List.可以触发toObject的名称为 assembleTrigger的方法
     * 
     * @param fromList
     *            源list
     * @param toClass
     *            目标class
     * @param excludeFields
     *            不装配的属性名称
     * @return
     * @author zhaolei
     * @created 2011-4-28
     */
    public static <T> List<T> assembleList2NewList(List<?> fromList, Class<T> toClass,
            String... excludeFields) {
        List<T> toList = new ArrayList<T>(fromList.size());
        try {
            for (Iterator<?> iterator = fromList.iterator(); iterator.hasNext();) {
                Object fromObject = iterator.next();
                T toObject = toClass.newInstance();
                if (excludeFields.length > 0)
                    toObject = assemble(fromObject, toObject, excludeFields);
                else
                    toObject = assemble(fromObject, toObject);
                toList.add(toObject);
            }
        } catch (Exception e) {
            System.out.println(ObjectUtil.class + " error!");
            e.printStackTrace();
        }
        return toList;
    }

    /**
     * 从源List装配一个符合目标class类型的List,可以触发toObject的名称为 assembleTrigger的方法
     * 
     * @param fromList
     *            源List
     * @param toList
     *            目标List
     * @param excludeFields
     *            不需要装配的属性
     * @return
     * @author zhaolei
     * @created 2011-4-28
     */
    public static <T> List<T> assembleList2List(List<?> fromList, List<T> toList, Class<T> toClass,
            String... excludeFields) {
        try {
            for (int i = 0; i < fromList.size(); i++) {
                Object fromObject = fromList.get(i);
                T toObject = i >= toList.size() ? toClass.newInstance() : toList.get(i);
                if (excludeFields.length > 0)
                    toObject = assemble(fromObject, toObject, excludeFields);
                else
                    toObject = assemble(fromObject, toObject);
                toList.add(toObject);
            }
        } catch (Exception e) {
            System.out.println(ObjectUtil.class + " error!");
            e.printStackTrace();
        }
        return toList;
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
}
