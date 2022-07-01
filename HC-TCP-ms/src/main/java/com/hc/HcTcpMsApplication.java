package com.hc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HcTcpMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HcTcpMsApplication.class, args);

	}
}
