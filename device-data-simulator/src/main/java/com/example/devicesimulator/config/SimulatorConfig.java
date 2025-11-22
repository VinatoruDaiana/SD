package com.example.devicesimulator.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

public class SimulatorConfig {

    private final String rabbitHost;
    private final int rabbitPort;
    private final String rabbitUsername;
    private final String rabbitPassword;
    private final String queueName;
    private final UUID deviceId;
    private final long intervalMillis;

    public SimulatorConfig(String rabbitHost,
                           int rabbitPort,
                           String rabbitUsername,
                           String rabbitPassword,
                           String queueName,
                           UUID deviceId,
                           long intervalMillis) {
        this.rabbitHost = rabbitHost;
        this.rabbitPort = rabbitPort;
        this.rabbitUsername = rabbitUsername;
        this.rabbitPassword = rabbitPassword;
        this.queueName = queueName;
        this.deviceId = deviceId;
        this.intervalMillis = intervalMillis;
    }

    public static SimulatorConfig load() {
        Properties props = new Properties();
        String resourceName = "simulator-config.properties";

        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourceName)) {

            if (is == null) {
                throw new IllegalStateException("Nu am gasit fisierul " + resourceName +
                        " pe classpath. Asigura-te ca exista in src/main/resources.");
            }

            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Nu pot citi simulator-config.properties", e);
        }

        String host = props.getProperty("rabbitmq.host", "localhost");
        int port = Integer.parseInt(props.getProperty("rabbitmq.port", "5672"));
        String username = props.getProperty("rabbitmq.username", "guest");
        String password = props.getProperty("rabbitmq.password", "guest");
        String queue = props.getProperty("rabbitmq.queue", "device.data.queue");

        String deviceIdStr = props.getProperty("simulator.device-id");
        if (deviceIdStr == null || deviceIdStr.isBlank()) {
            throw new IllegalStateException("simulator.device-id lipseste din simulator-config.properties");
        }
        UUID deviceId = UUID.fromString(deviceIdStr.trim());

        long intervalMillis = Long.parseLong(props.getProperty("simulator.interval.millis", "600000"));

        return new SimulatorConfig(host, port, username, password, queue, deviceId, intervalMillis);
    }

    public String getRabbitHost() {
        return rabbitHost;
    }

    public int getRabbitPort() {
        return rabbitPort;
    }

    public String getRabbitUsername() {
        return rabbitUsername;
    }

    public String getRabbitPassword() {
        return rabbitPassword;
    }

    public String getQueueName() {
        return queueName;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return "SimulatorConfig{" +
                "rabbitHost='" + rabbitHost + '\'' +
                ", rabbitPort=" + rabbitPort +
                ", rabbitUsername='" + rabbitUsername + '\'' +
                ", queueName='" + queueName + '\'' +
                ", deviceId=" + deviceId +
                ", intervalMillis=" + intervalMillis +
                '}';
    }
}
