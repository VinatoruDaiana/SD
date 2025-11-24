package com.example.user.dtos;

import java.util.UUID;

public class UserSyncEvent {

    private UUID userId;
    private String username;
    private String eventType;  // "USER_CREATED", "USER_UPDATED", "USER_DELETED"

    public UserSyncEvent() {
    }

    public UserSyncEvent(UUID userId, String username, String eventType) {
        this.userId = userId;
        this.username = username;
        this.eventType = eventType;
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
