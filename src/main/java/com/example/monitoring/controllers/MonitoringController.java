package com.example.monitoring.controllers;

import com.example.monitoring.dtos.HourlyConsumptionDTO;
import com.example.monitoring.services.MonitoringService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    /**
     * GET /api/monitoring/consumption?deviceId=...&date=YYYY-MM-DD
     */
    @GetMapping("/consumption")
    public ResponseEntity<List<HourlyConsumptionDTO>> getDailyConsumption(
            @RequestParam UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<HourlyConsumptionDTO> result = monitoringService.getDailyConsumption(deviceId, date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
