package com.example.websocket.controllers;

import com.example.websocket.dtos.ChatMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWsController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${websocket.queues.chatUserIn}")
    private String chatUserIn;

    public ChatWsController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Frontend -> /app/chat/send
    @MessageMapping("/chat/send")
    public void send(ChatMessage msg) {
        // opțional: validează câmpuri
        if (msg.getTimestamp() == null) msg.setTimestamp(System.currentTimeMillis());
        rabbitTemplate.convertAndSend(chatUserIn, msg);
    }
}

