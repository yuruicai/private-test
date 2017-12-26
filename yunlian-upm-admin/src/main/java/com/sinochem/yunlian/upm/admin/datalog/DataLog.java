package com.sinochem.yunlian.upm.admin.datalog;

import java.lang.annotation.*;

/**
 *
 * @version 1.0
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DataLog {

    /**
     * 字段的名称.也可以当作是字段的解释
     * 
     * @author zhaolei
     * @created 2011-6-22
     * 
     * @return
     */
    String name();

}
