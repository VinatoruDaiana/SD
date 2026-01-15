package com.example.websocket.messaging;

import com.example.websocket.dtos.ChatMessage;
import com.example.websocket.dtos.ChatResponseMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatListener {

    private final SimpMessagingTemplate simp;

    public ChatListener(SimpMessagingTemplate simp) {
        this.simp = simp;
    }

    @RabbitListener(queues = "${websocket.queues.chatUserOut}")
    public void onChatReply(ChatResponseMessage resp) {
        ChatMessage msg = new ChatMessage();
        msg.setUserIdentifier(resp.getUserIdentifier());
        msg.setFrom("BOT");
        msg.setText(resp.getReply());
        msg.setTimestamp(resp.getTimestamp() != null ? resp.getTimestamp().toEpochMilli() : System.currentTimeMillis());

        simp.convertAndSend("/topic/chat/user/" + msg.getUserIdentifier(), msg);
    }
}
