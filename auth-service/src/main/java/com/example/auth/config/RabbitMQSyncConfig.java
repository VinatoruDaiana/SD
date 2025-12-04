package com.example.auth.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSyncConfig {

    @Bean
    public Queue authSyncQueue(@Value("${sync.auth.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Jackson2JsonMessageConverter authMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate authRabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter authMessageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(authMessageConverter);
        return template;
    }
}