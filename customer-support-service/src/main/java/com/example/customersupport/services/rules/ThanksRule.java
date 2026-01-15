package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ThanksRule implements ChatRule {

    @Override
    public int priority() { return 40; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("thanks", "thank you", "thx", "mersi", "multumesc", "mulțumesc");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        return new ChatResponseMessage(ctx.getUserId(), "You're welcome! Anything else I can help with?", Instant.now())
                .addSuggestion("help")
                .addSuggestion("overconsumption");
    }

    @Override
    public String name() { return "ThanksRule"; }
}
