package com.example.instantmessaging.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String roomName;
  private List<Long> userIds;

  Room() {
    this.roomName = "new room" + this.id;
    this.userIds = List.of();
  }

  Room(String roomName) {
    this.roomName = roomName;
    this.userIds = List.of();
  };

  Room(String roomName, List<Long> userIds) {
    this.roomName = roomName;
    this.userIds = userIds;
  }

  public void addUserId(Long id) {
    this.userIds.add(id);
  }

  public void removeUserId(Long id) {
    this.userIds.remove(id);
  }
}
