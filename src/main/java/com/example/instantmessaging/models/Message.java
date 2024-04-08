package com.example.instantmessaging.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private Long senderId;

  @NotBlank
  private Long receiverId;

  @NotBlank
  private String content;
  private LocalDateTime timestamp;

  Message(Long senderId, Long receiverId, String content) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.content = content;
    this.timestamp = LocalDateTime.now();
  }
}
