package com.example.customersupport.services;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public class ChatContext {

    private final UUID userId;
    private final String rawText;
    private final String text;
    private final Instant timestamp;

    public ChatContext(UUID userId, String rawText, Instant timestamp) {
        this.userId = userId;
        this.rawText = rawText == null ? "" : rawText;
        this.text = this.rawText.trim().toLowerCase(Locale.ROOT);
        this.timestamp = timestamp;
    }

    public UUID getUserId() { return userId; }
    public String getRawText() { return rawText; }
    public String getText() { return text; }
    public Instant getTimestamp() { return timestamp; }

    public boolean containsAny(String... keywords) {
        for (String k : keywords) {
            if (k == null) continue;
            if (text.contains(k.toLowerCase(Locale.ROOT))) return true;
        }
        return false;
    }
}
