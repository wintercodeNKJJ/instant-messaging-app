package com.example.instantmessaging.controllers;

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

import com.example.instantmessaging.models.User;
import com.example.instantmessaging.services.EmailSender;
import com.example.instantmessaging.services.dataservice.UserDataService;

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
  private UserDataService userDataService;

  @Autowired
  private EmailSender emailSender;

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody User user) throws RuntimeException {

    userDataService.isUserInDataBase(user.getUsername(), user.getEmail());
    User newUser = userDataService.createNewUserDate(user);
    emailSender.sendEmailVerification(newUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@RequestBody User loginUser) throws EvaluationException {

    User user = userDataService.isRegistered(loginUser);
    userDataService.checkPass(loginUser, user);
    userDataService.isVerified(user);

    return ResponseEntity.ok("Login successfull");
  }

  @GetMapping("/verify/{verificationCode}")
  public ResponseEntity<String> verifyEmail(@PathVariable String verificationCode) throws EvaluationException {
    userDataService.verifyUser(verificationCode);
    return ResponseEntity.ok("Email verified successfully");
  }

  @SecurityRequirement(name = "bearerAuth")
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatUser) {
    User savedUser = userDataService.updateUser(id, updatUser);
    return ResponseEntity.ok(savedUser);
  }

  @SecurityRequirement(name = "bearerAuth")
  @GetMapping
  public ResponseEntity<Iterable<User>> getAllUsers() {
    return ResponseEntity.ok(userDataService.getAllUsers());
  }

  @SecurityRequirement(name = "bearerAuth")
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userDataService.getUserById(id));
  }

  @SecurityRequirement(name = "bearerAuth")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userDataService.deleteUserById(id);
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
