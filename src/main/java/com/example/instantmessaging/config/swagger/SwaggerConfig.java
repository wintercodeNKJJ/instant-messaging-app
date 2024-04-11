package com.example.instantmessaging.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi customOpi() {
    return GroupedOpenApi.builder().group("my-api").pathsToMatch("/api/**").build();
  }

  @SuppressWarnings("unused")
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Instant Chat API")
        .description("RESTful API documentation for My Awesome Application")
        .version("1.0")
        .contact(
            new Contact("Kenfack Jordan Junior", "https://github.com/wintercodeNKJJ", "kenfackjordanjunior@gmail.com"))
        .build();
  }
}
