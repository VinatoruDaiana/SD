package com.example.device.sync;

import com.example.device.entities.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeviceSyncProducer {

    private static final Logger log = LoggerFactory.getLogger(DeviceSyncProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public DeviceSyncProducer(RabbitTemplate rabbitTemplate,
                              @Value("${sync.device.queue}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    private void sendEvent(DeviceSyncEventType type, Device device) {
        DeviceSyncMessage msg = new DeviceSyncMessage(
                type,
                device.getId(),
                device.getUserId(),
                device.getName(),
                device.getMaxConsumption()
        );
        log.info("[DEVICE-SYNC] Sending {} for device {}", type, device.getId());
        rabbitTemplate.convertAndSend(queueName, msg);
    }

    public void sendDeviceCreated(Device device) {
        sendEvent(DeviceSyncEventType.DEVICE_CREATED, device);
    }

    public void sendDeviceUpdated(Device device) {
        sendEvent(DeviceSyncEventType.DEVICE_UPDATED, device);
    }

    public void sendDeviceDeleted(Device device) {
        sendEvent(DeviceSyncEventType.DEVICE_DELETED, device);
    }
    //  NEW: Trimite eveniment când device-ul e asignat la un user
    public void sendDeviceAssigned(Device device) {
        sendEvent(DeviceSyncEventType.DEVICE_UPDATED, device);
        log.info("[DEVICE-SYNC] Device {} assigned to user {}", device.getId(), device.getUserId());
    }

    //  NEW: Trimite eveniment când device-ul e dezasignat
    public void sendDeviceUnassigned(Device device) {
        sendEvent(DeviceSyncEventType.DEVICE_UPDATED, device);
        log.info("[DEVICE-SYNC] Device {} unassigned from user", device.getId());
    }
}

