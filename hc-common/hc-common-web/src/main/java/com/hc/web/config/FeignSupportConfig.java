package com.hc.web.config;

import com.hc.web.handler.FeignBasicAuthRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiuZhiHao
 * @date 2019/10/18 9:36
 * 描述:
 **/
@Configuration
public class FeignSupportConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new FeignBasicAuthRequestInterceptor();
    }
}
