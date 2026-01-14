package com.example.monitoring.sync;

import java.util.UUID;

public class DeviceSyncMessage {

    private DeviceSyncEventType eventType;
    private UUID deviceId;
    private UUID userId;
    private String name;


    private Double maxConsumption;

    public DeviceSyncMessage() {
    }

    public DeviceSyncMessage(DeviceSyncEventType eventType, UUID deviceId,
                             UUID userId, String name) {
        this.eventType = eventType;
        this.deviceId = deviceId;
        this.userId = userId;
        this.name = name;
    }

    public DeviceSyncEventType getEventType() {
        return eventType;
    }

    public void setEventType(DeviceSyncEventType eventType) {
        this.eventType = eventType;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(Double maxConsumption) {
        this.maxConsumption = maxConsumption;
    }
}
