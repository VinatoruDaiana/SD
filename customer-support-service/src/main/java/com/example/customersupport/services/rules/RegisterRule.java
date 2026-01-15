package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RegisterRule implements ChatRule {

    @Override
    public int priority() { return 59; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("register", "sign up", "signup", "create account", "cont nou", "inregistr");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "To register, use the Register page (frontend) which calls auth-service /api/auth/register.\n" +
                "After registration, login to get a JWT for accessing protected endpoints.";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("login")
                .addSuggestion("help");
    }

    @Override
    public String name() { return "RegisterRule"; }
}
