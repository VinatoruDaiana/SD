package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GoodbyeRule implements ChatRule {

    @Override
    public int priority() { return 30; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("bye", "goodbye", "see you", "pa", "la revedere");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        return new ChatResponseMessage(ctx.getUserId(), "Bye! If you need me again, type 'help'.", Instant.now());
    }

    @Override
    public String name() { return "GoodbyeRule"; }
}
