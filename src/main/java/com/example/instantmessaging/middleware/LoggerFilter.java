package com.example.instantmessaging.middleware;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class LoggerFilter extends OncePerRequestFilter {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    filterChain.doFilter(request, response);
    logger.trace("{} {} {}", request.getMethod(), request.getRequestURI(), response.getStatus());
    // logger.info("Response sent: {}", response.getStatus());
  }
}
