package com.example.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;
import java.util.UUID;


public class UserDetailsDTO {

    private UUID id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @Pattern(regexp = "ADMIN|USER|CLIENT", message = "Role must be ADMIN, USER or CLIENT")
    private String role;


    private String email;



    public UserDetailsDTO() {}

    public UserDetailsDTO(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserDetailsDTO(UUID id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }



    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsDTO that = (UserDetailsDTO) o;
        return Objects.equals(username, that.username)
                && Objects.equals(password, that.password)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, role);
    }


    @Override
    public String toString() {
        return "UserDetailsDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }


    public String getEmail() {

        return email;
    }
}
