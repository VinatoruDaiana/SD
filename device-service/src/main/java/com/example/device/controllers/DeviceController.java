package com.example.device.controllers;



import com.example.device.dtos.DeviceDTO;
import com.example.device.dtos.DeviceDetailsDTO;
import com.example.device.services.DeviceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
import java.util.UUID;

@Tag(name = "Devices")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/devices")

public class DeviceController {

    private final DeviceService service;

    public DeviceController(DeviceService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<DeviceDTO> create(@Valid @RequestBody DeviceDetailsDTO in) {
        return ResponseEntity.ok(service.create(in));
    }

    @GetMapping
    public List<DeviceDTO> all() {
        return service.getAll();
    }


    @GetMapping("/{id}")
    public DeviceDTO byId(@PathVariable UUID id) {
        return service.getById(id);
    }


    @PutMapping("/{id}")
    public DeviceDTO update(@PathVariable UUID id, @Valid @RequestBody DeviceDetailsDTO in) {
        return service.update(id, in);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/assign/{userId}")
    public DeviceDTO assign(@PathVariable UUID id, @PathVariable UUID userId) {
        return service.assign(id, userId);
    }

    @PatchMapping("/{id}/unassign")
    public DeviceDTO unassign(@PathVariable UUID id) {
        return service.unassign(id);
    }

    @GetMapping("/health")
    public String health() { return "ok"; }

    @Operation(
            summary = "List devices by user",
            description = "Returnează device-urile asignate unui utilizator"
    )
    @ApiResponse(responseCode = "200", description = "Listă returnată cu succes")

    @GetMapping(params = "userId")
    public List<DeviceDTO> byUser(@RequestParam UUID userId) {


        return service.getByUser(userId);
    }

}
