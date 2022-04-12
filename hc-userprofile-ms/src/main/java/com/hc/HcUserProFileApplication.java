package com.hc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动类
 * @author user
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.hc.infrastructure.dao")
@EnableSwagger2
public class HcUserProFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(HcUserProFileApplication.class, args);
	}

}
