package com.example.instantmessaging.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.instantmessaging.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findBySenderId(Long senderId);

  List<Message> findByReceiverId(Long receiverId);

  Optional<Message> findById(Long id);

  void deleteById(Long id);

  @SuppressWarnings("unchecked")
  @Override
  Message save(Message message);

  List<Message> findAll();
}
