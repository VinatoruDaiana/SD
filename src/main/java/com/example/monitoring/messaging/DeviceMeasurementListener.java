package com.example.monitoring.messaging;

import com.example.monitoring.dtos.DeviceMeasurementMessage;
import com.example.monitoring.services.MeasurementProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Componenta de "message consumer" cerută în temă.
 * Ascultă coada device.data.queue și procesează fiecare mesaj.
 */
@Component
public class DeviceMeasurementListener {

    private static final Logger log = LoggerFactory.getLogger(DeviceMeasurementListener.class);

    private final MeasurementProcessingService measurementProcessingService;

    public DeviceMeasurementListener(MeasurementProcessingService measurementProcessingService) {
        this.measurementProcessingService = measurementProcessingService;
    }

    @RabbitListener(queues = "${monitoring.queues.device-data}")
    public void handleMessage(DeviceMeasurementMessage message) {
        log.debug("Received measurement message: {}", message);
        measurementProcessingService.processMeasurement(message);
    }
}
