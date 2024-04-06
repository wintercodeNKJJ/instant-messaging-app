package com.example.instantmessaging.services.dataservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.instantmessaging.config.security.SecurityConfig;
import com.example.instantmessaging.models.User;
import com.example.instantmessaging.repositories.UserRepository;

import ch.qos.logback.core.boolex.EvaluationException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserDataService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SecurityConfig passwordEncoder = new SecurityConfig();

  public void isUserInDataBase(String username, String email) throws RuntimeException {
    if (userRepository.findByUsername(username) != null) {
      throw new RuntimeException("Can not Register user");
    }
    if (userRepository.findByEmail(email) != null) {
      throw new RuntimeException("Can not Register user");
    }
  }

  public User createNewUserDate(User user) {
    user.setPassword(passwordEncoder.passwordEncoder().encode(user.getPassword()));

    user.setVerified(false);
    user.setVerificationCode(UUID.randomUUID().toString());
    userRepository.save(user);

    return user;
  }

  public User isRegistered(User loginUser) throws EvaluationException {
    User user = userRepository.findByUsername(loginUser.getUsername());

    if (user == null) {
      throw new EvaluationException("Invalid username or password");
    }

    return user;
  }

  public void checkPass(User loginUser, User user) throws EvaluationException {
    if (passwordEncoder.passwordEncoder().matches(loginUser.getPassword(), user.getPassword())) {
      throw new EvaluationException("Invalid username or password");
    }
  }

  public void isVerified(User user) throws EvaluationException {
    if (!user.isVerified()) {
      throw new EvaluationException("Account not verified");
    }
  }

  public void verifyUser(String verificationCode) throws EvaluationException {
    User user = userRepository.findByVerificationCode(verificationCode);

    if (user == null) {
      throw new EvaluationException("Invalid verification code");
    }

    user.setVerified(true);
    userRepository.save(user);
  }

  public User updateUser(Long id, User updatUser) throws EntityNotFoundException {
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

    if (updatUser.getPassword() != null && !updatUser.getPassword().isEmpty()) {
      existingUser.setPassword(passwordEncoder.passwordEncoder().encode(updatUser.getPassword()));
    }

    existingUser.setPassword(updatUser.getPassword());
    existingUser.setEmail(updatUser.getEmail());

    return userRepository.save(existingUser);
  }

  @Cacheable("usersList")
  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Cacheable("userById")
  public User getUserById(Long id) throws EntityNotFoundException {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
  }

  public void deleteUserById(Long id) {
    userRepository.deleteById(id);
  }
}
