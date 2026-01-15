package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ContactHumanRule implements ChatRule {

    @Override
    public int priority() { return 50; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("human", "agent", "support", "admin", "help desk", "operator", "vorbesc cu", "contact");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "If you need a human agent, please describe your issue and include:\n" +
                "• your username\n" +
                "• deviceId (if relevant)\n" +
                "• time of the issue\n" +
                "A human can then review logs in monitoring-service and rabbitmq.";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("overconsumption")
                .addSuggestion("login")
                .addSuggestion("help");
    }

    @Override
    public String name() { return "ContactHumanRule"; }
}
