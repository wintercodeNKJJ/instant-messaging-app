package com.example.instantmessaging.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.instantmessaging.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  User findByVerificationCode(String verificationCode);

  User findByEmail(String email);

  Optional<User> findById(Long id);

  void deleteById(Long id);

  @SuppressWarnings("unchecked")
  User save(User user);

  List<User> findAll();
}
