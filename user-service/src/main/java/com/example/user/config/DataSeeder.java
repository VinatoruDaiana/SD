package com.example.user.config;

import com.example.user.entities.User;
import com.example.user.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner initUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                repo.save(new User("admin", encoder.encode("admin123"), "ADMIN"));
            }
            if (repo.findByUsername("client1").isEmpty()) {
                repo.save(new User("client1", encoder.encode("client123"), "CLIENT"));
            }
        };
    }
}