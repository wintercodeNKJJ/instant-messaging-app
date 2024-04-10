package com.example.instantmessaging.config.SocketConfig;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.instantmessaging.models.ChatMessage;
import com.example.instantmessaging.models.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListner {

  private final SimpMessageSendingOperations messageTemplate;

  @SuppressWarnings("null")
  @EventListener
  public void handleWebSocketDisconnectListner(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String username = (String) headerAccessor.getSessionAttributes().get("username");

    if (username != null) {
      log.info("User disconnected: {}", username);
      var chatMessage = ChatMessage.builder().Type(MessageType.LEAVE).sender(username).build();

      messageTemplate.convertAndSend("/topic/public", chatMessage);
    }
  }
}
