package com.example.instantmessaging.models;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long senderId;
  private Long receiverId;
  private String content;
  private LocalDateTime timestamp;

  Message(Long senderId, Long receiverId, String content) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.content = content;
    this.timestamp = LocalDateTime.now();
  }

  public String getContent() {
    return content;
  }

  public Long getReceiverId() {
    return receiverId;
  }

  public Long getSenderId() {
    return senderId;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Long getId() {
    return id;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setReceiverId(Long receiverId) {
    this.receiverId = receiverId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
