package com.hc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.hc.mapper")
@EnableFeignClients
public class MSWKServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MSWKServiceApplication.class, args);
	}
}
