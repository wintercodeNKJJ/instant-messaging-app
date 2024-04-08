package com.example.instantmessaging.services.RabbitMQ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.instantmessaging.models.User;
import com.example.instantmessaging.services.EmailService.EmailService;

@Component
@RabbitListener(queues = "mail_queue", id = "listener")
public class RabbitMQReceiver {
  @Autowired
  private EmailService emailService;

  private static Logger logger = LogManager.getLogger(RabbitMQReceiver.class.toString());

  @RabbitHandler
  public void receiver(User user) {
    logger.info("mail sender called");
    emailService.sendEmailVerification(user);
  }
}
