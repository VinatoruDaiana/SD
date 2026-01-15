package com.example.monitoring.services;

import com.example.monitoring.dtos.DeviceMeasurementMessage;
import com.example.monitoring.dtos.OverconsumptionAlertMessage;
import com.example.monitoring.entities.DeviceShadow;
import com.example.monitoring.entities.HourlyConsumption;
import com.example.monitoring.repositories.DeviceShadowRepository;
import com.example.monitoring.repositories.HourlyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.time.*;
import java.util.UUID;

@Service
public class MeasurementProcessingService {

    private static final Logger log = LoggerFactory.getLogger(MeasurementProcessingService.class);

    private final HourlyConsumptionRepository hourlyConsumptionRepository;

    private final DeviceShadowRepository deviceShadowRepository;
    private final RabbitTemplate rabbitTemplate;
    private final String overconsumptionQueueName;

    // cheie: deviceId|userId  -> ultima oră (epochHour) pentru care am trimis alertă
    private final ConcurrentMap<String, Long> lastAlertHourByDeviceUser = new ConcurrentHashMap<>();


    public MeasurementProcessingService(HourlyConsumptionRepository hourlyConsumptionRepository,
                                        DeviceShadowRepository deviceShadowRepository,
                                        RabbitTemplate rabbitTemplate,
                                        @Value("${monitoring.queues.overconsumption}") String overconsumptionQueueName) {
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
        this.deviceShadowRepository = deviceShadowRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.overconsumptionQueueName = overconsumptionQueueName;
    }


    /**
     * Procesează o măsurătoare venită din RabbitMQ:
     * - extrage ziua și ora
     * - face aggregate (sum) în tabela HourlyConsumption.
     */

    //aici procesez mesajul din coada RabbitMq
    //aici calculez HourlyConsumtion

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

        LocalDateTime dateTime = LocalDateTime.ofInstant(message.getTimestamp(), ZoneId.systemDefault());
        LocalDate date = dateTime.toLocalDate();
        int hour = dateTime.getHour();

        // 1) Update / create HourlyConsumption (si obtin totalul actual pe ora curenta)
        HourlyConsumption hc = hourlyConsumptionRepository
                .findByDeviceIdAndDateAndHour(deviceId, date, hour)
                .orElseGet(() -> new HourlyConsumption(deviceId, date, hour, 0.0));

        hc.addEnergy(message.getMeasurementValue());
        hourlyConsumptionRepository.save(hc); // sigur persistă și pentru cazul create

        double currentHourlyTotalKwh = hc.getEnergyKwh();

        log.debug("Hourly consumption for device {} date {} hour {} is now {} kWh (added {} kWh)",
                deviceId, date, hour, currentHourlyTotalKwh, message.getMeasurementValue());

        // 2) Citesc pragul maxConsumption din DeviceShadow (setat prin sync)
        DeviceShadow shadow = deviceShadowRepository.findById(deviceId).orElse(null);
        if (shadow == null || shadow.getMaxConsumption() == null) {
            log.warn("No maxConsumption available in DeviceShadow for device {}. Skipping overconsumption check.", deviceId);
            return;
        }

        double maxAllowedKwh = shadow.getMaxConsumption() / 1000.0;


        UUID userId = shadow.getUserId();
        if (userId == null) {
            log.warn("No userId in DeviceShadow for device {}. Skipping per-user overconsumption alert.", deviceId);
            return;
        }

        // oră curentă ca număr unic (ca să comparăm ușor orele între ele)
        ZonedDateTime zdt = message.getTimestamp().atZone(ZoneId.systemDefault());
        long epochHour = zdt.toEpochSecond() / 3600;

        // cheia e per device + per user (dacă device se reasignează, alt user poate primi alertă în aceeași oră)
        String alertKey = deviceId + "|" + userId;


        // 3) Check overconsumption + emit alert (catre websocket-service via RabbitMQ)
        if (currentHourlyTotalKwh > maxAllowedKwh) {

            Long lastSentHour = lastAlertHourByDeviceUser.get(alertKey);

            // trimitem doar dacă:
            // - n-am mai trimis niciodată
            // - sau a trecut într-o oră nouă
            if (lastSentHour == null || lastSentHour < epochHour) {

                OverconsumptionAlertMessage alert = new OverconsumptionAlertMessage(
                        deviceId,
                        date,
                        hour,
                        currentHourlyTotalKwh,
                        maxAllowedKwh
                );
                alert.setUserId(userId);

                rabbitTemplate.convertAndSend(overconsumptionQueueName, alert);

                // marcăm că am trimis alertă pentru ora curentă
                lastAlertHourByDeviceUser.put(alertKey, epochHour);

                log.info("OVERCONSUMPTION detected for device {} ({} kWh > {} kWh) at {} hour {}. Alert sent (throttled: 1/hour).",
                        deviceId, currentHourlyTotalKwh, maxAllowedKwh, date, hour);

            } else {
                log.debug("Overconsumption still true for device {} user {} at {} hour {}, but alert already sent this hour.",
                        deviceId, userId, date, hour);
            }
        }

    }

}
