package com.example.instantmessaging.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.instantmessaging.models.User;

@Component
public class EmailSender {
  private JavaMailSender javaMailSender;

  public void sendEmailVerification(User user) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setTo(user.getEmail());
    message.setSubject("Email Verification");
    message.setText("Dear " + user.getUsername() + ",\n\n"
        + "Please click the following link to verify your email address:\n\n"
        + "http://yourdomain.com/api/users/verify/" + user.getVerificationCode() + "\n\n"
        + "Thank you.");

    javaMailSender.send(message);
  }
}
