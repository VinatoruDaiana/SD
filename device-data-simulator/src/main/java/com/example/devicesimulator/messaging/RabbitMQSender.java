package com.example.devicesimulator.messaging;

import com.example.devicesimulator.model.DeviceMeasurementMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

//deschide conexiunea la RabbitMQ si trimite mesajele in coada
public class RabbitMQSender implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQSender.class);

    private final String queueName;
    private final Connection connection;
    private final Channel channel;
    private final ObjectMapper objectMapper;

    //se trimit mesajele
    public RabbitMQSender(String host,
                          int port,
                          String username,
                          String password,
                          String queueName) throws IOException, TimeoutException {
        this.queueName = queueName;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        this.connection = factory.newConnection();
        this.channel = connection.createChannel();

        // Coada declarata ca durable (sa existe daca nu exista)
        channel.queueDeclare(queueName, true, false, false, null);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    //converteste obiectul la JSON

    public void sendMeasurement(DeviceMeasurementMessage message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        byte[] body = json.getBytes(StandardCharsets.UTF_8);

        //pune mesajul in RabbitMQ
        channel.basicPublish(
                "",
                queueName,
                null,
                body
        );

        log.info("Trimis mesaj in RabbitMQ: {}", json);
    }

    @Override
    public void close() throws IOException {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (TimeoutException e) {
            log.warn("Timeout la inchiderea canalului RabbitMQ", e);
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
