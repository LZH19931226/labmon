package com.hc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableEurekaClient
//@EnableFeignClients
@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
//@MapperScan("com.hc.mapper")
public class SpringServiceApplication {

	public static void main(String[] args) {


		SpringApplication.run(SpringServiceApplication.class, args);

	}
}
