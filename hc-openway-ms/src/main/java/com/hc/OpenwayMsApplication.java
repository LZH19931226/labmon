package com.hc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OpenwayMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenwayMsApplication.class, args);
    }

}
