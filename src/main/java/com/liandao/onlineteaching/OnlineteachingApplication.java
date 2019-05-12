package com.liandao.onlineteaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlineteachingApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlineteachingApplication.class, args);
	}

}

