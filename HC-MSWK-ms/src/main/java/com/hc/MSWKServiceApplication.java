package com.hc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(value = {"com.hc.mapper","com.hc.clickhouse.mapper"})
@EnableFeignClients
@EnableScheduling
public class MSWKServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MSWKServiceApplication.class, args);
	}
}
