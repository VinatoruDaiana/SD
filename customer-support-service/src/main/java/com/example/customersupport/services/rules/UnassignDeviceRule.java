package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UnassignDeviceRule implements ChatRule {

    @Override
    public int priority() { return 69; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("unassign", "dez", "remove user", "unlink", "detach", "unassign device");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "To unassign a device, set its user_id to NULL using the Device Service (ADMIN).\n" +
                "After unassignment, the monitoring shadow should also update (sync).";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("assign device")
                .addSuggestion("overconsumption");
    }

    @Override
    public String name() { return "UnassignDeviceRule"; }
}
