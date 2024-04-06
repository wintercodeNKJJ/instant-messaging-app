package com.example.instantmessaging.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.instantmessaging.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Cacheable(value = "users-name", key = "#id")
  User findByUsername(String username);

  User findByVerificationCode(String verificationCode);

  @Cacheable(value = "users-email", key = "#email")
  User findByEmail(String email);

  @Cacheable(value = "users", key = "#id")
  Optional<User> findById(Long id);

  void deleteById(Long id);

  @SuppressWarnings("unchecked")
  User save(User user);

  List<User> findAll();
}
