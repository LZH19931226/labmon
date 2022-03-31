package com.hc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableScheduling
public class SpringMSCTApplication {

	public static void main(String[] args) {


		SpringApplication.run(SpringMSCTApplication.class, args);

	}
}
