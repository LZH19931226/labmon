package com.hc.my.common.core.annotation;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 9:20
 * 描述:
 **/
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
@Inherited
@Target({TYPE, METHOD, TYPE_USE})
@Retention(RUNTIME)
public @interface Monitor {

    String value() default "异常";

    TYPE type() default Monitor.TYPE.WORKER;

    enum TYPE {
        API, MANAGER, WORKER, DAO, SCHEDULER
    }
}