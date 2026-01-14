package com.example.websocket.services;

import com.example.websocket.dtos.OverconsumptionAlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OverconsumptionNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public OverconsumptionNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToUser(OverconsumptionAlertMessage alert) {
        String destination = "/topic/overconsumption/" + alert.getUserId();
        messagingTemplate.convertAndSend(destination, alert);
    }
}
