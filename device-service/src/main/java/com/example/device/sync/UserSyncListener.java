package com.example.device.sync;

import com.example.device.services.UserShadowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserSyncListener {

    private static final Logger log = LoggerFactory.getLogger(UserSyncListener.class);

    private final UserShadowService userShadowService;

    public UserSyncListener(UserShadowService userShadowService) {
        this.userShadowService = userShadowService;
    }

    @RabbitListener(queues = "${sync.user.queue}")
    public void handleUserSync(UserSyncMessage message) {
        log.info("[DEVICE-SYNC] Received event {} for user {}",
                message.getEventType(), message.getUserId());

        switch (message.getEventType()) {
            case USER_CREATED -> userShadowService.handleUserCreated(message.getUserId());
            case USER_UPDATED -> userShadowService.handleUserUpdated(message.getUserId());
            case USER_DELETED -> userShadowService.handleUserDeleted(message.getUserId());
        }
    }
}
