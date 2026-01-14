package com.example.websocket.messaging;

import com.example.websocket.dtos.OverconsumptionAlertMessage;
import com.example.websocket.services.OverconsumptionNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consuma overconsumption.queue din RabbitMQ si transmite alerta catre frontend prin WebSocket.
 */
@Component
public class OverconsumptionAlertListener {

    private static final Logger log = LoggerFactory.getLogger(OverconsumptionAlertListener.class);

    private final OverconsumptionNotificationService notificationService;

    public OverconsumptionAlertListener(OverconsumptionNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${websocket.queues.overconsumption}")
    public void handleMessage(OverconsumptionAlertMessage message) {
        log.debug("Received overconsumption alert message: {}", message);

        //primeste mesajul de overconsumption din RabbitMQ
        notificationService.sendToUser(message);
    }
}
