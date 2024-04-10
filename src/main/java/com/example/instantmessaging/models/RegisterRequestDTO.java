package com.example.instantmessaging.models;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.FetchType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
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

  @ManyToAny(fetch = FetchType.EAGER)
  private Set<UserRole> roles = new HashSet<>();

  public User toUser() {
    return new User(null, this.username, this.password, this.email, this.profilepicture, this.verified,
        this.verificationCode, this.roles);
  }
}
