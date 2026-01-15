package com.example.customersupport.services.rules;

import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.ChatContext;

public interface ChatRule {

    /**
     * Higher number = higher priority.
     */
    int priority();

    boolean matches(ChatContext ctx);

    ChatResponseMessage apply(ChatContext ctx);

    String name();
}
