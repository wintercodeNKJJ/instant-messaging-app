package com.example.instantmessaging.config.SocketConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@SuppressWarnings("null")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws");
    registry.addEndpoint("/ws").withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.setApplicationDestinationPrefixes("/app");
    config.enableStompBrokerRelay("/topic", "/queue").setRelayHost("127.0.0.1").setRelayPort(61613)
        .setClientLogin("guest").setClientPasscode("guest");

  }

  // @Override
  // public void configureClientInboundChannel(ChannelRegistration registration) {
  // registration.interceptors(new ChannelInterceptor() {
  // @Override
  // public Message<?> preSend(Message<?> message, MessageChannel channel) {
  // StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
  // StompHeaderAccessor.class);
  // if (StompCommand.CONNECT.equals(accessor.getCommand())) {
  // Authentication user = (Authentication) accessor.getUser(); // access
  // authentication header(s)
  // accessor.setUser(user);
  // }
  // return message;
  // }
  // });
  // }
}

// @Override
// public void configureMessageBroker(MessageBrokerRegistry config) {
// config.setApplicationDestinationPrefixes("/api");
// config.enableSimpleBroker("/topic", "/queue").setHeartbeatValue(new long[] {
// 10000, 20000 });
// }

// private ReactorNettyTcpClient<byte[]> createTcpClient(){
// return new ReactorNettyTcpClient<>(client ->client.addressSupplier(()-> ),
// new StompReactorNettyCodec());
// }

// .setTcpClient(createTcpClient());