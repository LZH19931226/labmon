package com.hc;

import com.hc.web.authorization.EnableAuthorizationX;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.hc.infrastructure.dao")
@EnableFeignClients
@EnableAuthorizationX
public class HcLabManagementMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HcLabManagementMsApplication.class, args);
	}

}

