package com.example.devicesimulator.service;

import com.example.devicesimulator.model.DeviceMeasurementMessage;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;



//geenreaza o val de consum in functie de ora si zi
public class MeasurementGenerator {

    private final UUID deviceId;
    private final Random random;

    public MeasurementGenerator(UUID deviceId) {
        this.deviceId = deviceId;

        this.random = new Random();
    }

    /**
     * Genereaza o masuratoare pentru intervalul de 10 minute in jurul lui "now".
     * Logica simpla:
     *  - noaptea (0-5): consum mic
     *  - dimineata (6-9): creste
     *  - zi (10-16): moderat
     *  - seara (17-21): maxim
     *  - tarziu (22-23): scade iar
     * Valoarea rezultata este "consum pe 10 minute" in kWh.
     */
    public DeviceMeasurementMessage generateMeasurement(Instant now) {
        ZonedDateTime zdt = now.atZone(ZoneId.systemDefault());
        int hour = zdt.getHour();

        double hourlyBase; // kWh per ora
        if (hour >= 0 && hour <= 5) {
            hourlyBase = 0.3; // foarte mic
        } else if (hour >= 6 && hour <= 9) {
            hourlyBase = 0.6; // dimineata
        } else if (hour >= 10 && hour <= 16) {
            hourlyBase = 0.8; // zi lucratoare
        } else if (hour >= 17 && hour <= 21) {
            hourlyBase = 1.2; // seara, varf
        } else {
            hourlyBase = 0.5; // tarziu
        }

        // Consum pe 10 minute = 1/6 din consumul pe ora
        double basePerTenMinutes = hourlyBase / 6.0;

        // Fluctuatie
        double factor = 0.9 + random.nextDouble() * 0.2;
        double value = basePerTenMinutes * factor;

        //  sa nu fie negativ sau 0
        if (value < 0.01) {
            value = 0.01;
        }

        return new DeviceMeasurementMessage(deviceId, now, value);
    }
}
