package com.example.instantmessaging.models;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotBlank
  private String username;

  @NotBlank
  @JsonIgnore
  private String password;

  @NotBlank
  @Email
  private String email;
  private String profilepicture;
  private Boolean verified;
  private String verificationCode;

  @ManyToAny(fetch = FetchType.EAGER)
  private Set<UserRole> roles = new HashSet<>();

  public Boolean isVerified() {
    return verified;
  }
}
