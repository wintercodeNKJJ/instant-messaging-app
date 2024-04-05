package com.example.instantmessaging.models;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

  public Long getId() {
    return id;
  }

  public String getRoomName() {
    return roomName;
  }

  public List<Long> getUserIds() {
    return userIds;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public void setUserIds(List<Long> userIds) {
    this.userIds = userIds;
  }

  public void addUserId(Long id) {
    this.userIds.add(id);
  }

  public void removeUserId(Long id) {
    this.userIds.remove(id);
  }
}
