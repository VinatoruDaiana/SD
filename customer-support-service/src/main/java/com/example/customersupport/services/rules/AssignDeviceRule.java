package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AssignDeviceRule implements ChatRule {

    @Override
    public int priority() { return 70; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("assign", "asign", "asigneaza", "assign device", "link device");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "To assign a device to a user, use the Device Service (ADMIN):\n" +
                "1) Find the userId (from user-service).\n" +
                "2) Call PATCH/PUT on /api/devices/{deviceId} (or the dedicated assign endpoint) to set user_id.\n" +
                "After assignment, the monitoring shadow table should also receive the user_id via sync.";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("unassign device")
                .addSuggestion("device list")
                .addSuggestion("overconsumption");
    }

    @Override
    public String name() { return "AssignDeviceRule"; }
}
