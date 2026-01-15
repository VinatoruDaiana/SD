package com.example.websocket.dtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChatResponseMessage {
    private String userIdentifier;
    private String reply;
    private Instant timestamp;
    private List<String> suggestions = new ArrayList<>();

    public ChatResponseMessage() {}


    public String getUserIdentifier() { return userIdentifier; }
    public void setUserIdentifier(String userIdentifier) { this.userIdentifier = userIdentifier; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
}
