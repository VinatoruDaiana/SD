package com.example.device.sync;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSyncMessage {

    private DeviceSyncEventType eventType;
    private UUID deviceId;

    // câmpuri suplimentare, dacă îți trebuie mai târziu
    private UUID userId;
    private String name;
}
