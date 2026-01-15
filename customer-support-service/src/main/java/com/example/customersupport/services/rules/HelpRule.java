package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class HelpRule implements ChatRule {

    @Override
    public int priority() { return 90; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("help", "ajutor", "commands", "comenzi", "what can you do", "ce poti face");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "I can answer common support questions. Try:\n" +
                "• overconsumption / alert\n" +
                "• max consumption / unit\n" +
                "• assign device / unassign device\n" +
                "• login / register\n" +
                "• contact human";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("overconsumption")
                .addSuggestion("max consumption")
                .addSuggestion("login");
    }

    @Override
    public String name() { return "HelpRule"; }
}
