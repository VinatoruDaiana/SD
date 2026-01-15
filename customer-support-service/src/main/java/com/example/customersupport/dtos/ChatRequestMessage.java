package com.example.customersupport.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class ChatRequestMessage {

    @NotNull
    private UUID userId;

    @NotBlank
    private String text;

    private Instant timestamp;

    public ChatRequestMessage() {
    }

    public ChatRequestMessage(UUID userId, String text, Instant timestamp) {
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
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
}
