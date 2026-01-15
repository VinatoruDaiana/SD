package com.example.customersupport.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * Simple domain object (no DB) for keeping chat messages in memory if needed.
 */
public class ChatMessage {

    private String userId;
    private String text;
    private Instant timestamp;
    private boolean fromBot;

    public ChatMessage() {}

    public ChatMessage(String userId, String text, Instant timestamp, boolean fromBot) {
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
        this.fromBot = fromBot;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isFromBot() {
        return fromBot;
    }

    public void setFromBot(boolean fromBot) {
        this.fromBot = fromBot;
    }


}
