package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LoginRule implements ChatRule {

    @Override
    public int priority() { return 60; }

    @Override
    public boolean matches(ChatContext ctx) {
        return ctx.containsAny("login", "log in", "sign in", "autent", "token", "jwt");
    }

    @Override
    public ChatResponseMessage apply(ChatContext ctx) {
        String reply = "If you can't log in:\n" +
                "1) Make sure your username/password are correct.\n" +
                "2) Check that auth-service is running and Traefik routes /api/auth.\n" +
                "3) After login, your browser should store the JWT cookie/token used by other services.";

        return new ChatResponseMessage(ctx.getUserId(), reply, Instant.now())
                .addSuggestion("register")
                .addSuggestion("contact human");
    }

    @Override
    public String name() { return "LoginRule"; }
}
