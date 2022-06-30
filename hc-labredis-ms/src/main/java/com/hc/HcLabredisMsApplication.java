package com.hc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.hc.clickhouse.mapper")
public class HcLabredisMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HcLabredisMsApplication.class, args);
    }

}
