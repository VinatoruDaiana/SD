package com.example.user.services;

import com.example.user.dtos.UserSyncEvent;
import com.example.user.entities.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserSyncProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${sync.exchange.name}")
    private String syncExchangeName;

    @Value("${sync.routing.user-events}")
    private String userEventsRoutingKey;

    public UserSyncProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserCreated(User user) {
        sendEvent(user, "USER_CREATED");
    }

    public void sendUserUpdated(User user) {
        sendEvent(user, "USER_UPDATED");
    }

    public void sendUserDeleted(User user) {
        sendEvent(user, "USER_DELETED");
    }

    private void sendEvent(User user, String type) {
        UserSyncEvent event = new UserSyncEvent(
                user.getId(),
                user.getUsername(),
                type
        );
        rabbitTemplate.convertAndSend(syncExchangeName, userEventsRoutingKey, event);
        System.out.println("[SYNC→RABBITMQ] " + type + " " + user.getId());
    }
}
