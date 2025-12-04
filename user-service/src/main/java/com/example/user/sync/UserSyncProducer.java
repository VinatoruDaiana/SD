package com.example.user.sync;

import com.example.user.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserSyncProducer {

    private static final Logger log = LoggerFactory.getLogger(UserSyncProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public UserSyncProducer(RabbitTemplate rabbitTemplate,
                            @Value("${sync.user.queue}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    private void sendEvent(UserSyncEventType type, User user) {
        UserSyncMessage message = new UserSyncMessage(
                type,
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
        log.info("[USER-SYNC] Sending event {} for user {}", type, user.getId());
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public void sendUserCreated(User user) {
        sendEvent(UserSyncEventType.USER_CREATED, user);
    }

    public void sendUserUpdated(User user) {
        sendEvent(UserSyncEventType.USER_UPDATED, user);
    }

    public void sendUserDeleted(User user) {
        sendEvent(UserSyncEventType.USER_DELETED, user);
    }
}
