package com.sinochem.yunlian.upm.sso.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangxi
 * @created 14-1-16
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAuth {

    /**
     * 需要哪些角色名
     *
     * @return
     */
    String[] permissions() default {};

    /**
     * 需要是哪些用户
     *
     * @return
     */
    int[] users() default {};

    /**
     * 认证级别
     *
     * @return
     */
    Level level() default Level.ANON;

    public static enum Level {
        ANON(0),
        LOGIN(1),
        AUTH(2);

        private int value;

        Level(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
