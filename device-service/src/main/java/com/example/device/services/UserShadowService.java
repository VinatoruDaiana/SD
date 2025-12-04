package com.example.device.services;

import com.example.device.entities.UserShadow;
import com.example.device.repositories.UserShadowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserShadowService {

    private static final Logger log = LoggerFactory.getLogger(UserShadowService.class);

    private final UserShadowRepository repo;

    public UserShadowService(UserShadowRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void handleUserCreated(UUID userId) {
        if (repo.existsById(userId)) {
            log.info("[DEVICE-SHADOW] User {} already exists, skipping create", userId);
            return;
        }
        UserShadow shadow = new UserShadow();
        shadow.setId(userId);
        repo.save(shadow);
        log.info("[DEVICE-SHADOW] Created shadow for user {}", userId);
    }

    @Transactional
    public void handleUserUpdated(UUID userId) {
        // pentru moment doar ne asigurăm că există în tabelă
        if (!repo.existsById(userId)) {
            UserShadow shadow = new UserShadow();
            shadow.setId(userId);
            repo.save(shadow);
            log.info("[DEVICE-SHADOW] Created shadow on update for user {}", userId);
        } else {
            log.info("[DEVICE-SHADOW] User {} already present on update", userId);
        }
    }

    @Transactional
    public void handleUserDeleted(UUID userId) {
        if (repo.existsById(userId)) {
            repo.deleteById(userId);
            log.info("[DEVICE-SHADOW] Deleted shadow for user {}", userId);
        } else {
            log.info("[DEVICE-SHADOW] User {} not found on delete", userId);
        }
    }
}
