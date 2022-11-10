package com.hc.my.common.core.jwt;

import java.lang.annotation.*;

/**
 * @author LiuZhiHao
 * @date 2019/10/16 9:25
 * 描述: jwt集成
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtIgnore {
    boolean required() default true;
}
