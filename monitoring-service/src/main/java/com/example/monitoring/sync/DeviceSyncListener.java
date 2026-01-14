package com.example.monitoring.sync;

import com.example.monitoring.services.DeviceShadowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeviceSyncListener {

    private static final Logger log = LoggerFactory.getLogger(DeviceSyncListener.class);

    private final DeviceShadowService deviceShadowService;

    public DeviceSyncListener(DeviceShadowService deviceShadowService) {
        this.deviceShadowService = deviceShadowService;
    }

    @RabbitListener(queues = "${sync.device.queue}")
    public void handleDeviceSync(DeviceSyncMessage message) {
        log.info("[MONITORING-SYNC] Received event {} for device {}",
                message.getEventType(), message.getDeviceId());

        switch (message.getEventType()) {
            case DEVICE_CREATED -> deviceShadowService.handleDeviceCreated(message);
            case DEVICE_UPDATED -> deviceShadowService.handleDeviceUpdated(message);
            case DEVICE_DELETED -> deviceShadowService.handleDeviceDeleted(message.getDeviceId());
        }
    }
}
