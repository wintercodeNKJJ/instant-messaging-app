package com.example.instantmessaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class })

public class InstantMessagingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstantMessagingApplication.class, args);
	}

}
