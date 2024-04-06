package com.example.instantmessaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import jakarta.servlet.ServletContext;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfig {

  @Bean
  public Docket api(ServletContext servletContext) {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("com.example.instantmessaging.controllers"))
        .paths(PathSelectors.any()).build();
  }

  @SuppressWarnings("unused")
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("My Awesome API")
        .description("RESTful API documentation for My Awesome Application")
        .version("1.0")
        .contact(new Contact("Your Name", "https://github.com/yourusername", "your@email.com"))
        .build();
  }
}
