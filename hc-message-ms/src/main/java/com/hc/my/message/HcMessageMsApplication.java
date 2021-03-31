package com.hc.my.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.hc.my.message.mapper")
public class HcMessageMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HcMessageMsApplication.class, args);
    }

}
