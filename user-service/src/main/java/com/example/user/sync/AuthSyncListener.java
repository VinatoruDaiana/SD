package com.example.user.sync;

import com.example.user.entities.User;
import com.example.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthSyncListener {

    private static final Logger log = LoggerFactory.getLogger(AuthSyncListener.class);

    private final UserRepository userRepository;

    public AuthSyncListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "${sync.auth.queue}")
    @Transactional
    public void handleAuthSync(AuthSyncMessage message) {
        log.info("[USER-SYNC] Received {} for user {}",
                message.getEventType(), message.getUsername());

        switch (message.getEventType()) {
            case AUTH_USER_CREATED -> handleUserCreated(message);
            case AUTH_USER_UPDATED -> handleUserUpdated(message);
            case AUTH_USER_DELETED -> handleUserDeleted(message);
        }
    }

    private void handleUserCreated(AuthSyncMessage message) {
        if (userRepository.findByUsername(message.getUsername()).isPresent()) {
            log.info("[USER-SYNC] User {} already exists, skipping", message.getUsername());
            return;
        }

        User user = new User();
        user.setUsername(message.getUsername());
        user.setEmail(message.getEmail());
        user.setPasswordHash(message.getPasswordHash()); // deja hash
        user.setRole(message.getRole());

        userRepository.save(user);
        log.info("[USER-SYNC] Created user {}", message.getUsername());
    }

    private void handleUserUpdated(AuthSyncMessage message) {
        User user = userRepository.findByUsername(message.getUsername())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(message.getUsername());
                    return newUser;
                });

        if (message.getEmail() != null) {
            user.setEmail(message.getEmail());
        }
        if (message.getPasswordHash() != null && !message.getPasswordHash().isBlank()) {
            user.setPasswordHash(message.getPasswordHash());
        }
        if (message.getRole() != null) {
            user.setRole(message.getRole());
        }

        userRepository.save(user);
        log.info("[USER-SYNC] Updated user {}", message.getUsername());
    }

    private void handleUserDeleted(AuthSyncMessage message) {
        userRepository.findByUsername(message.getUsername())
                .ifPresentOrElse(
                        user -> {
                            userRepository.delete(user);
                            log.info("[USER-SYNC] Deleted user {}", message.getUsername());
                        },
                        () -> log.info("[USER-SYNC] User {} not found for deletion", message.getUsername())
                );
    }
}