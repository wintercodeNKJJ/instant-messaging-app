package com.example.instantmessaging.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.instantmessaging.models.Message;

@Controller
public class MessageController {

  private SimpMessagingTemplate template;

  @MessageMapping("/greeting")
  public String handle(String greeting) {
    return "[" + new Date().getTime() + ": " + greeting;
  }

  @MessageMapping("/chat")
  @SendTo("/topic/messages")
  public void send(Message message) throws Exception {
    String time = new SimpleDateFormat("HH:mm").format(new Date());
    this.template.convertAndSend("/topic/messages", message + time);
  }
}
