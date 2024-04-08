package com.example.instantmessaging.services.RabbitMQ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.instantmessaging.models.User;

@Service
public class RabbitMQSender {
  @Autowired
  private AmqpTemplate rabbiTemplate;

  @Autowired
  private Queue queue;
  private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());

  public void send(User user) {
    rabbiTemplate.convertAndSend(queue.getName(), user);
    logger.trace("Sending email to: " + user.getEmail());
  }
}
