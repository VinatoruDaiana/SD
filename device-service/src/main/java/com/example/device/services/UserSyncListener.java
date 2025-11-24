package com.example.device.services;

import com.example.device.dtos.UserSyncEvent;
import com.example.device.model.UserShadow;
import com.example.device.repositories.UserShadowRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserSyncListener {

    private final UserShadowRepository userShadowRepository;

    public UserSyncListener(UserShadowRepository userShadowRepository) {
        this.userShadowRepository = userShadowRepository;
    }

    @RabbitListener(queues = "${sync.queue.user-events}")
    public void handleUserSync(UserSyncEvent event) {

        switch (event.getEventType()) {

            case "USER_CREATED":
                System.out.println("SYNC: creating shadow user " + event.getUserId());
                userShadowRepository.save(new UserShadow(event.getUserId()));

                break;

            case "USER_DELETED":
                System.out.println("SYNC: deleting shadow user " + event.getUserId());
                userShadowRepository.deleteById(event.getUserId());
                break;

            default:
                System.out.println("UNKNOWN SYNC EVENT: " + event.getEventType());
        }
    }
}
