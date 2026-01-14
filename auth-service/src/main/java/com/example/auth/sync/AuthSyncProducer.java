package com.example.auth.sync;

import com.example.auth.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthSyncProducer {

    private static final Logger log = LoggerFactory.getLogger(AuthSyncProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public AuthSyncProducer(RabbitTemplate rabbitTemplate,
                            @Value("${sync.auth.queue}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    private void sendEvent(AuthSyncEventType type, User user) {
        AuthSyncMessage message = new AuthSyncMessage(
                type,
                user.getUsername(),
                user.getEmail(),
                user.getPassword(), // deja hash
                user.getRole().name()
        );
        log.info("[AUTH-SYNC] Sending {} for user {}", type, user.getUsername());
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public void sendAuthUserCreated(User user) {
        sendEvent(AuthSyncEventType.AUTH_USER_CREATED, user);
    }

    public void sendAuthUserUpdated(User user) {
        sendEvent(AuthSyncEventType.AUTH_USER_UPDATED, user);
    }

    public void sendAuthUserDeleted(User user) {
        sendEvent(AuthSyncEventType.AUTH_USER_DELETED, user);
    }
}