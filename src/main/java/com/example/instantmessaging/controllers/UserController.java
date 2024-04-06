package com.example.instantmessaging.controllers;

import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.instantmessaging.config.SecurityConfig;
import com.example.instantmessaging.models.User;
import com.example.instantmessaging.repositories.UserRepository;
import com.example.instantmessaging.services.EmailSender;

import ch.qos.logback.core.boolex.EvaluationException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RestControllerAdvice
@RequestMapping("/api/users")
@Tag(name = "User Management Endpoints", description = "All the endpoints needed to perform crud operations")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailSender emailSender;

  @Autowired
  private SecurityConfig passwordEncoder = new SecurityConfig();

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody User user) {
    System.out.println("Hello");
    if (userRepository.findByUsername(user.getUsername()) != null) {
      throw new RuntimeErrorException(null, "Can not Register user");
    }
    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new RuntimeErrorException(null, "Can not Register user");
    }

    user.setPassword(passwordEncoder.passwordEncoder().encode(user.getPassword()));

    user.setVerified(false);
    user.setVerificationCode(UUID.randomUUID().toString());
    User newUser = userRepository.save(user);

    emailSender.sendEmailVerification(newUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestBody User loginUser) throws EvaluationException {
    User user = userRepository.findByUsername(loginUser.getUsername());

    if (user == null) {
      throw new EvaluationException("Invalid username or password");
    }

    if (passwordEncoder.passwordEncoder().matches(loginUser.getPassword(), user.getPassword())) {
      throw new EvaluationException("Invalid username or password");
    }

    if (!user.isVerified()) {
      throw new EvaluationException("Account not verified");
    }

    return ResponseEntity.ok("Login successfull");
  }

  @GetMapping("/verify/{verificationCode}")
  public ResponseEntity<String> verifyEmail(@PathVariable String verificationCode) throws EvaluationException {
    User user = userRepository.findByVerificationCode(verificationCode);

    if (user == null) {
      throw new EvaluationException("Invalid verification code");
    }

    user.setVerified(true);
    userRepository.save(user);

    return ResponseEntity.ok("Email verified successfully");
  }

  @SecurityRequirement(name = "bearerAuth")
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatUser) {
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

    if (updatUser.getPassword() != null && !updatUser.getPassword().isEmpty()) {
      existingUser.setPassword(passwordEncoder.passwordEncoder().encode(updatUser.getPassword()));
    }

    existingUser.setPassword(updatUser.getPassword());
    existingUser.setEmail(updatUser.getEmail());

    User savedUser = userRepository.save(existingUser);
    return ResponseEntity.ok(savedUser);
  }

  @SecurityRequirement(name = "bearerAuth")
  @GetMapping
  public ResponseEntity<Iterable<User>> getAllUsers() {
    Iterable<User> users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }

  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

    return ResponseEntity.ok(user);
  }

  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleUnAuthorizedExeption(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(EvaluationException.class)
  public ResponseEntity<String> handleEvaluationException(EvaluationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
  }
}
