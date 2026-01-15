package com.example.customersupport.repositories;

import com.example.customersupport.entities.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Simple in-memory chat log. Not required for the assignment, but useful for debugging.
 */
@Repository
public class InMemoryChatLogRepository {

    private static final int MAX_MESSAGES_PER_USER = 50;
    private final Map<UUID, Deque<ChatMessage>> store = new ConcurrentHashMap<>();

    public void append(ChatMessage message) {
        Deque<ChatMessage> deque = store.computeIfAbsent(message.getUserId(), k -> new ConcurrentLinkedDeque<>());
        deque.addLast(message);
        while (deque.size() > MAX_MESSAGES_PER_USER) {
            deque.pollFirst();
        }
    }

    public List<ChatMessage> getRecent(UUID userId) {
        Deque<ChatMessage> deque = store.get(userId);
        if (deque == null) return List.of();
        return new ArrayList<>(deque);
    }
}
