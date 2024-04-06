package com.example.instantmessaging.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  @Email
  private String email;
  private String profilepicture;
  private Boolean verified;
  private String verificationCode;

  User(String username, String password, String email, String profilepicture) {
    System.out.println(username + " " + password + " " + profilepicture + " " + email);
    this.username = username;
    this.password = password;
    this.email = email;
    this.profilepicture = profilepicture;
    this.verified = false;
    this.verificationCode = "";
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

  public Boolean isVerified() {
    return verified;
  }

  public String getVerificationCode() {
    return verificationCode;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setProfilepicture(String profilepicture) {
    this.profilepicture = profilepicture;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }
}
