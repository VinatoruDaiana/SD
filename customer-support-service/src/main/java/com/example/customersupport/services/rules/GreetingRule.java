package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GreetingRule implements ChatRule {

    @Override
    public int priority() { return 100; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny( "hello", "hey", "salut", "buna", "bună");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        return new ChatResponseMessage(ctx.getUserId(),
                "Hi! I'm the Customer Support bot. I can help with devices, login, and overconsumption alerts. Type 'help' to see what I can do.",
                Instant.now())
                .addSuggestion("help")
                .addSuggestion("overconsumption")
                .addSuggestion("assign device");
    }

    @Override
    public String name() { return "GreetingRule"; }
}
