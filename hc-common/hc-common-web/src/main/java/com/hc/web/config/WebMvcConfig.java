package com.hc.web.config;

import com.hc.web.handler.TokenHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LiuZhiHao
 * @date 2019/10/16 9:28
 * 描述: 动态拦截路径,由网关决定, 拦截信息暂存请求头里面
 **/
public class WebMvcConfig implements WebMvcConfigurer {




    @Bean
    public WebMvcConfig getWebMvcConfig(){
        return  new WebMvcConfig();
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenHandle()).addPathPatterns("/**");
    }




}
