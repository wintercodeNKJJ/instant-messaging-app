package com.example.instantmessaging.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi customOpi() {
    return GroupedOpenApi.builder().group("my-api").pathsToMatch("/api/**").build();
  }
}
