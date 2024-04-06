package com.example.instantmessaging.config;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException {
  CustomAuthenticationException(String message) {
    super(message);
  }
}
