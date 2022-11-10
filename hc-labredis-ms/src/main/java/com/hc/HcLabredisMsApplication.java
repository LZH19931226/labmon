package com.hc;

import com.hc.web.authorization.EnableAuthorizationX;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableAuthorizationX
public class HcLabredisMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HcLabredisMsApplication.class, args);
    }

}
