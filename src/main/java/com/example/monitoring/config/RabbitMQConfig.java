package com.example.monitoring.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DEVICE_DATA_QUEUE = "device.data.queue";
    public static final String DEVICE_DATA_EXCHANGE = "device.data.exchange";
    public static final String DEVICE_DATA_ROUTING_KEY = "device.data.routing";

    @Bean
    public Queue deviceDataQueue() {
        return QueueBuilder.durable(DEVICE_DATA_QUEUE).build();
    }

    @Bean
    public TopicExchange deviceDataExchange() {
        return new TopicExchange(DEVICE_DATA_EXCHANGE);
    }

    @Bean
    public Binding deviceDataBinding() {
        return BindingBuilder
                .bind(deviceDataQueue())
                .to(deviceDataExchange())
                .with(DEVICE_DATA_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
