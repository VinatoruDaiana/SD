package com.example.customersupport.services;

import com.example.customersupport.dtos.ChatRequestMessage;
import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.repositories.InMemoryChatLogRepository;
import com.example.customersupport.entities.ChatMessage;
import com.example.customersupport.services.rules.ChatRule;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class RuleBasedChatbotService {

    private final List<ChatRule> rules;
    private final InMemoryChatLogRepository chatLog;
    private final com.example.customersupport.services.GeminiAiService geminiAiService;



    public RuleBasedChatbotService(List<ChatRule> rules, InMemoryChatLogRepository chatLog, com.example.customersupport.services.GeminiAiService geminiAiService) {
        // sort once by priority desc
        this.rules = rules.stream()
                .sorted(Comparator.comparingInt(ChatRule::priority).reversed())
                .toList();
        this.chatLog = chatLog;
        this.geminiAiService = geminiAiService;
    }

    public ChatResponseMessage handle(ChatRequestMessage req) {
        Instant ts = (req.getTimestamp() != null)
                ? Instant.ofEpochMilli(req.getTimestamp())
                : Instant.now();

        ChatContext ctx = new ChatContext(req.getUserIdentifier(), req.getText(), ts);


        chatLog.append(new com.example.customersupport.entities.ChatMessage(
                req.getUserIdentifier(), req.getText(), ts, false));

        for (ChatRule rule : rules) {
            if (rule.matches(ctx)) {
                ChatResponseMessage resp = rule.apply(ctx);
                if (resp.getTimestamp() == null) resp.setTimestamp(Instant.now());
                if (resp.getUserIdentifier() == null) resp.setUserIdentifier(req.getUserIdentifier());

                chatLog.append(new com.example.customersupport.entities.ChatMessage(
                        req.getUserIdentifier(), resp.getReply(), resp.getTimestamp(), true));
                return resp;
            }
        }

        // No rule matched => AI-driven customer support (Gemini)
        String aiReply = geminiAiService.generateReply(
                req.getUserIdentifier(),
                req.getText(),
                chatLog.getRecent(req.getUserIdentifier())
        );

        ChatResponseMessage aiResp = new ChatResponseMessage(
                req.getUserIdentifier(),
                aiReply,
                Instant.now()
        );

        chatLog.append(new ChatMessage(
                req.getUserIdentifier(), aiResp.getReply(), aiResp.getTimestamp(), true
        ));

        return aiResp;

    }

    public List<String> getRuleNamesInOrder() {
        return rules.stream().map(ChatRule::name).toList();
    }
}
