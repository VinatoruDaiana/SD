package com.example.auth.services;

import com.example.auth.dto.UserSyncEvent;
import com.example.auth.model.AuthUserShadow;
import com.example.auth.repositories.AuthUserShadowRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserSyncListener {

    private final AuthUserShadowRepository userShadowRepository;

    public UserSyncListener(AuthUserShadowRepository userShadowRepository) {
        this.userShadowRepository = userShadowRepository;
    }

    @RabbitListener(queues = "${sync.queue.user-events}")
    public void handleUserSync(UserSyncEvent event) {

        switch (event.getEventType()) {

            case "USER_CREATED":
                System.out.println("[AUTH] SYNC: creating shadow user " + event.getUserId());
                userShadowRepository.save(new AuthUserShadow(event.getUserId()));

                break;

            case "USER_DELETED":
                System.out.println("[AUTH] SYNC: deleting shadow user " + event.getUserId());
                userShadowRepository.deleteById(event.getUserId());

                break;

            default:
                System.out.println("[AUTH] UNKNOWN SYNC EVENT: " + event.getEventType());
        }
    }
}
