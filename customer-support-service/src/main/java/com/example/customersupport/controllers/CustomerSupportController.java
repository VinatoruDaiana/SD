package com.example.customersupport.controllers;

import com.example.customersupport.services.RuleBasedChatbotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customer-support")
public class CustomerSupportController {

    private final RuleBasedChatbotService chatbot;
    private final String requestQueue;
    private final String responseQueue;

    public CustomerSupportController(RuleBasedChatbotService chatbot,
                                     @Value("${chat.request.queue}") String requestQueue,
                                     @Value("${chat.response.queue}") String responseQueue) {
        this.chatbot = chatbot;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> m = new HashMap<>();
        m.put("status", "UP");
        m.put("service", "customer-support-service");
        return m;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> m = new HashMap<>();
        m.put("rules", chatbot.getRuleNamesInOrder());
        m.put("queues", Map.of(
                "chat.request.queue", requestQueue,
                "chat.response.queue", responseQueue
        ));
        return m;
    }
}
