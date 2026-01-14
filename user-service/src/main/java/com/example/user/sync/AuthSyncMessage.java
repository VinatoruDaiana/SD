package com.example.user.sync;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthSyncMessage {
    private AuthSyncEventType eventType;
    private String username;
    private String email;
    private String passwordHash;
    private String role;
}