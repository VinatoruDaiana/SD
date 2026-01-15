package com.example.customersupport.services;

import java.time.Instant;
import java.util.*;
import java.util.Locale;

public class ChatContext {

    private final String userIdentifier;
    private final String rawText;
    private final String text;
    private final Instant timestamp;

    public ChatContext(String userId, String rawText, Instant timestamp) {
        this.userIdentifier = userId;
        this.rawText = rawText == null ? "" : rawText;
        this.text = this.rawText.trim().toLowerCase(Locale.ROOT);
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userIdentifier;
    }

    public String getUserIdentifier() { return userIdentifier; }
    public String getRawText() { return rawText; }
    public String getText() { return text; }
    public Instant getTimestamp() { return timestamp; }

    public boolean containsAny(String... keywords) {
        String t = text == null ? "" : text.toLowerCase(Locale.ROOT);


        return Arrays.stream(keywords)
                .filter(k -> k != null && !k.isBlank())
                .map(k -> k.toLowerCase(Locale.ROOT))
                .distinct()
                .anyMatch(t::contains);
    }
}
