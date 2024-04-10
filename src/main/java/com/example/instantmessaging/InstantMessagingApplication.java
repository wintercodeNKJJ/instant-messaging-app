package com.example.instantmessaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableCaching
@Slf4j
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class })

public class InstantMessagingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstantMessagingApplication.class, args);
		log.info("\nEndpoint docs at: http://localhost:8082/docs \nServer running at http://localhost:8082");
	}

}
