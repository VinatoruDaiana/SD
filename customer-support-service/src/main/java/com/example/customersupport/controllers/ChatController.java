package com.example.customersupport.controllers;

import com.example.customersupport.dtos.ChatRequestMessage;
import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.RuleBasedChatbotService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer-support")
public class ChatController {

    private final RuleBasedChatbotService chatbot;

    public ChatController(RuleBasedChatbotService chatbot) {
        this.chatbot = chatbot;
    }

    /**
     * Simple REST endpoint to test the rule-based chatbot (no websocket needed).
     */
    @PostMapping("/chat")
    public ChatResponseMessage chat(@Valid @RequestBody ChatRequestMessage req) {
        return chatbot.handle(req);
    }
}
