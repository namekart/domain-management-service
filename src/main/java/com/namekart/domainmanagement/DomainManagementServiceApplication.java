package com.namekart.domainmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableAspectJAutoProxy
@EnableScheduling
public class DomainManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomainManagementServiceApplication.class, args);
	}
}
