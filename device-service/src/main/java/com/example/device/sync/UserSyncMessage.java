package com.example.device.sync;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSyncMessage {

    private UserSyncEventType eventType;
    private UUID userId;

    private String username;
    private String email;
}
