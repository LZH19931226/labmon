package com.hc.my.common.core.annotation;

/**
 * @author LiuZhiHao
 * @date 2019/10/23 14:32
 * 描述:
 **/
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface Configure {

    /** xxx.xxx.xxx */
    String value();

    /** Generic type of configuration, use field/class/method type if not set. */
    Class<?> type() default Object.class;

    /** refreshable if modify. */
    boolean refreshable() default true;
}