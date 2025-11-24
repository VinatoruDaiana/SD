package com.example.device.dtos;

import java.util.UUID;

public class UserSyncEvent {
    private UUID userId;
    private String username;
    private String eventType; // "USER_CREATED", "USER_DELETED"

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
