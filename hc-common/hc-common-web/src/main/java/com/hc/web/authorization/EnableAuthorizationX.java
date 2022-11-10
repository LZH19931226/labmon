package com.hc.web.authorization;

import com.hc.web.config.WebMvcConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author LiuZhiHao
 * @date 2019/10/16 9:29
 * 描述: 全局拦截,校验请求头中是否包含token信息-
 **/
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Import({WebMvcConfig.class})
public @interface EnableAuthorizationX {}

