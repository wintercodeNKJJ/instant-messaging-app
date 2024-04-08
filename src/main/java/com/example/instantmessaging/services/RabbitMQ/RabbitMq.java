package com.example.instantmessaging.services.RabbitMQ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableRabbit
public class RabbitMq {

  @Value("${spring.rabbitmq.queue}")
  private String queueName;

  @Value("${spring.rabbitmq.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.routingkey}")
  private String routingkey;

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;

  @Value("${spring.rabbitmq.host}")
  private String host;

  @Bean
  public Queue queue() {
    return new Queue(queueName);
  }

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(exchange);
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingkey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(host);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    return connectionFactory;
  }

  @Bean
  public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setDefaultReceiveQueue(queueName);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    rabbitTemplate.setReplyAddress(queue().getName());

    return rabbitTemplate;
  }

  @Bean
  public AmqpAdmin amqpAdmin() {
    return new RabbitAdmin(connectionFactory());
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
    final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

    factory.setConnectionFactory(connectionFactory());
    factory.setMessageConverter(jsonMessageConverter());
    factory.setErrorHandler(errorHandler());

    return factory;
  }

  @Bean
  public ErrorHandler errorHandler() {
    return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
  }

  public static class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
    private final Logger logger = LogManager.getLogger(getClass());

    @Override
    public boolean isFatal(@SuppressWarnings("null") Throwable t) {
      if (t instanceof ListenerExecutionFailedException) {
        ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
        logger.error("Failed to process inbound message from queue "
            + lefe.getFailedMessage().getMessageProperties().getConsumerQueue()
            + "; failed message: " + lefe.getFailedMessage(), t);
      }
      return super.isFatal(t);
    }
  }

}
