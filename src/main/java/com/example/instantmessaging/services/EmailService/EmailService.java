package com.example.instantmessaging.services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.example.instantmessaging.models.User;

@Component
public class EmailService {

  @Autowired
  private MailSender mailSender;

  @Value("$(spring.mail.username)")
  private String fromMail;

  public boolean sendEmailVerification(User user) throws RuntimeException {
    try {
      SimpleMailMessage message = new SimpleMailMessage();

      message.setFrom(fromMail);
      message.setTo(user.getEmail());
      message.setSubject("Email Verification");
      message.setText("Dear " + user.getUsername() + ",\n\n"
          + "Please click the following link to verify your email address:\n\n"
          + "http://yourdomain.com/api/users/verify/" + user.getVerificationCode() + "\n\n"
          + "Thank you.");

      mailSender.send(message);
      return true;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
