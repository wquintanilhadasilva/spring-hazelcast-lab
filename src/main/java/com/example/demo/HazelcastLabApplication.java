package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HazelcastLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(HazelcastLabApplication.class, args);
	}

}
