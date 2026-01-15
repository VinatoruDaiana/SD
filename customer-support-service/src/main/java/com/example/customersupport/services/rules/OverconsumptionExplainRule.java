package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OverconsumptionExplainRule implements ChatRule {

    @Override
    public int priority() { return 80; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("overconsumption", "alert", "alarma", "alerta", "consum prea", "consumption too", "threshold");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "Overconsumption means the measured energy for a time window exceeded the device's maxConsumption threshold.\n" +
                "Typical checks:\n" +
                "1) The device is assigned to the correct user.\n" +
                "2) maxConsumption is in the same unit as measurements (usually kWh per hour).\n" +
                "3) If you get spam, enable 1-alert-per-hour deduplication in monitoring.";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("Why many alerts?")
                .addSuggestion("max consumption unit")
                .addSuggestion("assign device");
    }

    @Override
    public String name() { return "OverconsumptionExplainRule"; }
}
