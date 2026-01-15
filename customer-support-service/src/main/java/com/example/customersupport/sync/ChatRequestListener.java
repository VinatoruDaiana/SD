package com.example.customersupport.sync;

import com.example.customersupport.dtos.ChatRequestMessage;
import com.example.customersupport.dtos.ChatResponseMessage;
import com.example.customersupport.services.RuleBasedChatbotService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatRequestListener {

    private final RuleBasedChatbotService chatbot;
    private final RabbitTemplate chatRabbitTemplate;
    private final String responseQueue;

    public ChatRequestListener(RuleBasedChatbotService chatbot,
                              RabbitTemplate chatRabbitTemplate,
                              @Value("${chat.response.queue}") String responseQueue) {
        this.chatbot = chatbot;
        this.chatRabbitTemplate = chatRabbitTemplate;
        this.responseQueue = responseQueue;
    }

    @RabbitListener(queues = "${chat.request.queue}")
    public void onChatRequest(ChatRequestMessage request) {
        ChatResponseMessage response = chatbot.handle(request);
        // publish the bot reply back to the response queue
        chatRabbitTemplate.convertAndSend(responseQueue, response);
    }
}
