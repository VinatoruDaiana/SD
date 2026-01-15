package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AlertSpamRule implements ChatRule {

    @Override
    public int priority() { return 65; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("spam", "too many", "many alerts", "prea multe", "multe alerte", "every second", "continuous");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "If you receive the same overconsumption alert repeatedly, enable de-duplication in monitoring-service:\n" +
                "• key = (deviceId, hour)\n" +
                "• send at most 1 alert per device per hour (or per window).\n" +
                "This keeps the dashboard usable.";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("overconsumption")
                .addSuggestion("units");
    }

    @Override
    public String name() { return "AlertSpamRule"; }
}
