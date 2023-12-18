package com.ikn.ums.department;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UMSDepartmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UMSDepartmentServiceApplication.class, args);
		
		System.out.println("UMSDepartmentServiceApplication.main()");
	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
