package com.example.user.sync;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSyncMessage {

    private UserSyncEventType eventType;
    private UUID userId;

    // dacă vrei să le folosești mai târziu
    private String username;
    private String email;
}
