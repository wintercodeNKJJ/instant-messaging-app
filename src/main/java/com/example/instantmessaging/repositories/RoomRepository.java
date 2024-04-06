package com.example.instantmessaging.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.instantmessaging.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
  Room findByRoomName(String name);

  Optional<Room> findById(Long id);

  void deleteById(Long id);

  @SuppressWarnings("unchecked")
  Room save(Room room);

  List<Room> findAll();
}
