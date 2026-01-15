package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class FallbackRule implements ChatRule {

    @Override
    public int priority() { return 0; }

    @Override
    public boolean matches(ChatContext ctx) { return true; }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        return new ChatResponseMessage(ctx.getUserId(),
                "I'm not sure I understood. Type 'help' for available topics.",
                Instant.now())
                .addSuggestion("help")
                .addSuggestion("overconsumption")
                .addSuggestion("login");
    }

    @Override
    public String name() { return "FallbackRule"; }
}
