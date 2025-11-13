package com.example.auth.entities;

public enum UserRole {
    ADMIN, USER;

    public static UserRole fromString(String role) {
        if (role == null) return USER;
        switch (role.trim().toUpperCase()) {
            case "ADMIN": return ADMIN;
            case "USER":
            case "CLIENT":
            case "CUSTOMER": return USER;
            default: return USER;
        }
    }


}
