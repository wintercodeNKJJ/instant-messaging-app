package com.example.instantmessaging.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthResponse {
  private User user;
  private String token;

  public AuthResponse(User user, String token) {
    this.token = token;
    this.user = user;
  }

}
