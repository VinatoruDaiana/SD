package com.example.monitoring.services;

import com.example.monitoring.dtos.DeviceMeasurementMessage;
import com.example.monitoring.entities.HourlyConsumption;
import com.example.monitoring.repositories.HourlyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.UUID;

@Service
public class MeasurementProcessingService {

    private static final Logger log = LoggerFactory.getLogger(MeasurementProcessingService.class);

    private final HourlyConsumptionRepository hourlyConsumptionRepository;

    public MeasurementProcessingService(HourlyConsumptionRepository hourlyConsumptionRepository) {
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
    }

    /**
     * Procesează o măsurătoare venită din RabbitMQ:
     * - extrage ziua și ora
     * - face aggregate (sum) în tabela HourlyConsumption.
     */
    @Transactional
    public void processMeasurement(DeviceMeasurementMessage message) {
        UUID deviceId = message.getDeviceId();
        if (deviceId == null) {
            log.warn("Received measurement without deviceId, ignoring: {}", message);
            return;
        }

        if (message.getTimestamp() == null) {
            log.warn("Received measurement without timestamp, ignoring: {}", message);
            return;
        }

        if (message.getMeasurementValue() < 0) {
            log.warn("Received negative measurement value, ignoring: {}", message);
            return;
        }

        // Convertim Instant -> LocalDateTime în zona serverului (poți folosi și UTC).
        LocalDateTime dateTime = LocalDateTime.ofInstant(message.getTimestamp(), ZoneId.systemDefault());
        LocalDate date = dateTime.toLocalDate();
        int hour = dateTime.getHour();

        hourlyConsumptionRepository
                .findByDeviceIdAndDateAndHour(deviceId, date, hour)
                .ifPresentOrElse(existing -> {
                    existing.addEnergy(message.getMeasurementValue());
                    log.debug("Updated hourly consumption for device {} date {} hour {}: +{} kWh",
                            deviceId, date, hour, message.getMeasurementValue());
                }, () -> {
                    HourlyConsumption hc = new HourlyConsumption(
                            deviceId,
                            date,
                            hour,
                            message.getMeasurementValue()
                    );
                    hourlyConsumptionRepository.save(hc);
                    log.debug("Created hourly consumption for device {} date {} hour {}: {} kWh",
                            deviceId, date, hour, message.getMeasurementValue());
                });
    }
}
