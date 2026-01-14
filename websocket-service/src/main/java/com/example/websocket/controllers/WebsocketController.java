package com.example.websocket.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/websocket")
public class WebsocketController {

    @Value("${websocket.endpoint:/ws}")
    private String endpoint;

    @Value("${websocket.topic:/topic/overconsumption}")
    private String topic;

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of(
                "wsEndpoint", endpoint,
                "topic", topic
        );
    }
}
