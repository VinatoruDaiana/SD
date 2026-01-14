package com.example.device.config;

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
    public Queue userSyncQueue(@Value("${sync.user.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Jackson2JsonMessageConverter syncMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate syncRabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter syncMessageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(syncMessageConverter);
        return template;
    }

    @Bean
    public Queue deviceSyncQueue(@Value("${sync.device.queue}") String queueName) {

        return QueueBuilder.durable(queueName).build();
    }

}
