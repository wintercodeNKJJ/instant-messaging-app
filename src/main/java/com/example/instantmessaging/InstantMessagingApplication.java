package com.example.instantmessaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InstantMessagingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstantMessagingApplication.class, args);
	}

}
