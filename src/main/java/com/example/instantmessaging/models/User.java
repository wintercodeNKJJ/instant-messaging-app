package com.example.instantmessaging.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;
  private String email;
  private String profilepicture;

  User(String username, String password, String email, String profilepicture) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.profilepicture = profilepicture;
  };

  public String getEmail() {
    return email;
  }

  public String getProfilepicture() {
    return profilepicture;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Long getId() {
    return id;
  }
}
