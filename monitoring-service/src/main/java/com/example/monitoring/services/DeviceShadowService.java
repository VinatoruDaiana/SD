package com.example.monitoring.services;

import com.example.monitoring.entities.DeviceShadow;
import com.example.monitoring.repositories.DeviceShadowRepository;
import com.example.monitoring.sync.DeviceSyncMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeviceShadowService {

    private static final Logger log = LoggerFactory.getLogger(DeviceShadowService.class);

    private final DeviceShadowRepository repo;

    public DeviceShadowService(DeviceShadowRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void handleDeviceCreated(DeviceSyncMessage message) {
        UUID deviceId = message.getDeviceId();

        DeviceShadow shadow = repo.findById(deviceId)
                .orElse(new DeviceShadow(deviceId));

        shadow.setMaxConsumption(message.getMaxConsumption());
        shadow.setUserId(message.getUserId());
        repo.save(shadow);

        log.info("[MONITORING-SYNC] Saved shadow for device {} with maxConsumption={}",
                deviceId, message.getMaxConsumption());
    }

    @Transactional
    public void handleDeviceUpdated(DeviceSyncMessage message) {
        UUID deviceId = message.getDeviceId();

        DeviceShadow shadow = repo.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found in shadow: " + deviceId));

        shadow.setMaxConsumption(message.getMaxConsumption());
        shadow.setUserId(message.getUserId());
        if (message.getUserId() != null) {
            shadow.setUserId(message.getUserId());
        }

        repo.save(shadow);

        log.info("[MONITORING-SYNC] Updated shadow for device {} with maxConsumption={}",
                deviceId, message.getMaxConsumption());
    }

    @Transactional
    public void handleDeviceDeleted(UUID deviceId) {
        if (repo.existsById(deviceId)) {
            repo.deleteById(deviceId);
            log.info("[MONITORING-SHADOW] Deleted shadow for device {}", deviceId);
        } else {
            log.info("[MONITORING-SHADOW] Device {} not found on delete", deviceId);
        }
    }
}
