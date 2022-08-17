package com.hc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@MapperScan(value = {"com.hc.mapper","com.hc.clickhouse.mapper"})
@EnableFeignClients
public class SpringMSCTApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringMSCTApplication.class, args);
	}
}
