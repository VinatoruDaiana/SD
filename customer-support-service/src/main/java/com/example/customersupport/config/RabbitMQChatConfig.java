package com.example.customersupport.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQChatConfig {

    @Bean
    public Queue chatRequestQueue(@Value("${chat.request.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue chatResponseQueue(@Value("${chat.response.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Jackson2JsonMessageConverter chatMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate chatRabbitTemplate(ConnectionFactory connectionFactory,
                                            Jackson2JsonMessageConverter chatMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(chatMessageConverter);
        return template;
    }
}
