package com.example.customersupport.services;

import com.example.customersupport.dtos.ChatRequestMessage;
import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.repositories.InMemoryChatLogRepository;
import com.example.customersupport.entities.ChatMessage;
import com.example.customersupport.services.rules.ChatRule;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class RuleBasedChatbotService {

    private final List<ChatRule> rules;
    private final InMemoryChatLogRepository chatLog;

    public RuleBasedChatbotService(List<ChatRule> rules, InMemoryChatLogRepository chatLog) {
        // sort once by priority desc
        this.rules = rules.stream()
                .sorted(Comparator.comparingInt(ChatRule::priority).reversed())
                .toList();
        this.chatLog = chatLog;
    }

    public ChatResponseMessage handle(ChatRequestMessage req) {
        Instant ts = req.getTimestamp() != null ? req.getTimestamp() : Instant.now();
        ChatContext ctx = new ChatContext(req.getUserId(), req.getText(), ts);

        chatLog.append(new ChatMessage(req.getUserId(), req.getText(), ts, false));

        for (ChatRule rule : rules) {
            if (rule.matches(ctx)) {
                ChatResponseMessage resp = rule.apply(ctx);
                if (resp.getTimestamp() == null) resp.setTimestamp(Instant.now());
                if (resp.getUserId() == null) resp.setUserId(req.getUserId());
                chatLog.append(new ChatMessage(req.getUserId(), resp.getReply(), resp.getTimestamp(), true));
                return resp;
            }
        }

        // should never happen if FallbackRule exists
        return new ChatResponseMessage(req.getUserId(), "Sorry, I couldn't process your request.", Instant.now());
    }

    public List<String> getRuleNamesInOrder() {
        return rules.stream().map(ChatRule::name).toList();
    }
}
