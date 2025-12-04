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

        //1. incarca config-ul
        SimulatorConfig config = SimulatorConfig.load();
        log.info("Configuratie incarcata: {}", config);


        //2. creeaza generatorul de masuratori
        MeasurementGenerator generator = new MeasurementGenerator(config.getDeviceId());


        //3. deschide conexiunea catre RabbitMQ
        try (RabbitMQSender sender = new RabbitMQSender(
                config.getRabbitHost(),
                config.getRabbitPort(),
                config.getRabbitUsername(),
                config.getRabbitPassword(),
                config.getQueueName()
        )) {
            long interval = config.getIntervalMillis();
            log.info("Incep simularea. Interval masurare = {} ms", interval);

            //loop infinit
            //geenreaza mesaje
            // le trimite
            //asteapta
            //tot asa

            while (true) {
                Instant now = Instant.now();

                //geenreaza masuratoarea
                DeviceMeasurementMessage msg = generator.generateMeasurement(now);

                //trimite in RabbitMQ ca JSON
                sender.sendMeasurement(msg);

                // asteapta un anumit interval de timp (10 sec)
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
