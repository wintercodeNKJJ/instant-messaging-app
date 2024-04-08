package com.example.instantmessaging.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.example.instantmessaging.models.AuthRequestsDTO;
import com.example.instantmessaging.models.AuthResponse;
import com.example.instantmessaging.services.RabbitMQ.RabbitMQSender;
import com.example.instantmessaging.services.dataservice.UserDataService;
import com.example.instantmessaging.utils.JwtTokenUtil;

import ch.qos.logback.core.boolex.EvaluationException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RestControllerAdvice
@RequestMapping("/api/users")
@Tag(name = "User Management Endpoints", description = "All the endpoints needed to perform crud operations")
public class UserController {

  @Autowired
  private RabbitMQSender rabbitMQSender;

  @Autowired
  private UserDataService userDataService;

  @Autowired
  public AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @PostMapping("/register")
  public ResponseEntity<User> registerUser(@RequestBody User user) throws RuntimeException {

    userDataService.isUserInDataBase(user.getUsername(), user.getEmail());
    User newUser = userDataService.createNewUserDate(user);

    rabbitMQSender.send(newUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @PostMapping("/signin")
  public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequestsDTO authRequestsDTO,
      HttpServletResponse response)
      throws UsernameNotFoundException {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequestsDTO.getUsername(), authRequestsDTO.getPassword()));

    if (authentication.isAuthenticated()) {
      User user = userDataService.findByUsername(authRequestsDTO.getUsername());

      String token = jwtTokenUtil.generateToken(user.getUsername());

      Cookie cookie = new Cookie("token", token);
      cookie.setHttpOnly(true);
      cookie.setMaxAge(jwtTokenUtil.expiration);
      cookie.setPath("/");
      response.addCookie(cookie);

      return ResponseEntity.ok(new AuthResponse(user, token));
    } else {
      throw new UsernameNotFoundException("Invalid request...!!");
    }

  }

  @GetMapping("/login")
  public ResponseEntity<String> loginMessage() {
    return ResponseEntity.ok("login first thing first");
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
  @GetMapping("/user/{id}")
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
