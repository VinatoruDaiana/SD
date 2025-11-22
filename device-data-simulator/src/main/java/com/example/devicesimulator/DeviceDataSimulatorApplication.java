package com.example.devicesimulator;

import com.example.devicesimulator.config.SimulatorConfig;
import com.example.devicesimulator.messaging.RabbitMQSender;
import com.example.devicesimulator.model.DeviceMeasurementMessage;
import com.example.devicesimulator.service.MeasurementGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class DeviceDataSimulatorApplication {

    private static final Logger log = LoggerFactory.getLogger(DeviceDataSimulatorApplication.class);

    public static void main(String[] args) {
        log.info("Pornire Device Data Simulator...");

        SimulatorConfig config = SimulatorConfig.load();
        log.info("Configuratie incarcata: {}", config);

        MeasurementGenerator generator = new MeasurementGenerator(config.getDeviceId());

        try (RabbitMQSender sender = new RabbitMQSender(
                config.getRabbitHost(),
                config.getRabbitPort(),
                config.getRabbitUsername(),
                config.getRabbitPassword(),
                config.getQueueName()
        )) {
            long interval = config.getIntervalMillis();
            log.info("Incep simularea. Interval masurare = {} ms", interval);

            while (true) {
                Instant now = Instant.now();
                DeviceMeasurementMessage msg = generator.generateMeasurement(now);

                sender.sendMeasurement(msg);

                // Sleep pana la urmatoarea masuratoare
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    log.warn("Simulator intrerupt, ies din bucla...", e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        } catch (Exception e) {
            log.error("Eroare in simulator", e);
        }

        log.info("Device Data Simulator s-a oprit.");
    }
}
