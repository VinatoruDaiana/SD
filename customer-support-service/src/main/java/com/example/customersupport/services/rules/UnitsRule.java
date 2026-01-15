package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UnitsRule implements ChatRule {

    @Override
    public int priority() { return 75; }

    @Override
    public boolean matches(ChatContext ctx) {
        String t = ctx.getText().toLowerCase();

        // match only whole words, not substrings like "wh" inside "what"
        return t.matches(".*\\b(kwh|kw|wh|watt|unit|units|unitate|unitati)\\b.*");
    }


    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "Quick unit guide:\n" +
                "• W / kW = power (instantaneous).\n" +
                "• Wh / kWh = energy (over time).\n" +
                "In this project, the simulator typically publishes energy per hour (kWh).\n" +
                "So maxConsumption should be set in kWh for the same time window (e.g., per hour).";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("overconsumption")
                .addSuggestion("maxConsumption")
                .addSuggestion("simulator unit");
    }

    @Override
    public String name() { return "UnitsRule"; }
}
