package com.example.instantmessaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.instantmessaging.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return new AuthenticationManager() {
      @Override
      public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService().loadUserByUsername(username);

        if (passwordEncoder().matches(password, userDetails.getPassword())) {
          // Authentication successful
          return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
          // Authentication failed
          throw new CustomAuthenticationException("Invalid credentials");
        }
      }
    };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        authorizeRequests -> authorizeRequests.requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()
            .requestMatchers("/docs", "/swagger-ui/**", "/api/users/login", "/api/users/**", "/v3/**").permitAll()
            .anyRequest().authenticated())
        .formLogin(formLogin -> formLogin
            .loginPage("/api/users/login")
        // .defaultSuccessUrl("/rooms")
        ).exceptionHandling(exceptionHandling -> exceptionHandling
            .accessDeniedPage("/access-denied"));
    return http.build();
  }
}
