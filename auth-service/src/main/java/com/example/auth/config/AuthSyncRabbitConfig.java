package com.example.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthSyncRabbitConfig {

    @Value("${sync.exchange.name}")
    private String syncExchangeName;

    @Value("${sync.queue.user-events}")
    private String userEventsQueueName;

    @Value("${sync.routing.user-events}")
    private String userEventsRoutingKey;

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(syncExchangeName, true, false);
    }

    @Bean
    public Queue userEventsQueue() {

        return new Queue(userEventsQueueName, true);
    }

    @Bean
    public Binding userEventsBinding() {
        return BindingBuilder
                .bind(userEventsQueue())
                .to(syncExchange())
                .with(userEventsRoutingKey);
    }
}
