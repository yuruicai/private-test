package com.sinochem.yunlian.upm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangxi
 * @created 13-1-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Export {
    String apikey();

    Status status() default Status.DEVELOP_STATUS_USING;

    Access access() default Access.ACCESS_LEVEL_PUBLIC;

    public static enum Status {
        DEVELOP_STATUS_WAIT(0),
        DEVELOP_STATUS_DEVELOGING(1),
        DEVELOP_STATUS_USING(2),
        DEVELOP_STATUS_DEPRECATED(3),
        DEVELOP_STATUS_DEAD(9);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static enum Access {
        ACCESS_LEVEL_PUBLIC(0),
        ACCESS_LEVEL_INNER(1),
        ACCESS_LEVEL_AUTHENTICATED(2);

        private int value;

        Access(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
