package com.hc.web.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

/**
 * Created by yangzhou-he on 2017-08-18.
 */
@Lazy
@Configuration
public class BaseConfiguration {
    @Value("${system.publicPath}")
    private String publicPath;

    @Value("${system.tempFilePath}")
    private String tempFilePath;

    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
