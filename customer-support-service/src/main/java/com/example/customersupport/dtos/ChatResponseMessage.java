package com.example.customersupport.dtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatResponseMessage {

    private String userIdentifier;
    private String reply;
    private Instant timestamp;
    private List<String> suggestions = new ArrayList<>();

    public ChatResponseMessage() {
    }

    public ChatResponseMessage(String userIdentifier, String reply, Instant timestamp) {
        this.userIdentifier = userIdentifier;
        this.reply = reply;
        this.timestamp = timestamp;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public ChatResponseMessage addSuggestion(String s) {
        this.suggestions.add(s);
        return this;
    }
}
