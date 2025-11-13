package com.example.auth.requests;

import com.example.auth.entities.UserRole;

public class SyncUserFromUsersService {
    public String username;
    public String email;         // poate fi null
    public String passwordHash;  // deja HASH, nu plaintext
    public String role;        // ADMIN / USER



}
