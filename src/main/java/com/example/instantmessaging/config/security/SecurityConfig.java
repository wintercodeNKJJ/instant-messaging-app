package com.example.instantmessaging.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.instantmessaging.middleware.AuthMiddleware.JwtAuthFilter;
import com.example.instantmessaging.services.userDetailsService.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired
  JwtAuthFilter jwtAuthFilter;

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        authorizeRequests -> authorizeRequests
            .requestMatchers("/docs", "/swagger-ui/**", "/v3/**").permitAll()
            .requestMatchers("/api/users/signin", "/api/users/verify/**", "/api/users/register").permitAll()
            .anyRequest().authenticated())
        .formLogin(formLogin -> formLogin
            .loginPage("/api/users/login")
        // .defaultSuccessUrl("/rooms")
        ).csrf(csrf -> csrf.disable()).exceptionHandling(exceptionHandling -> exceptionHandling
            .accessDeniedPage("/access-denied"));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
