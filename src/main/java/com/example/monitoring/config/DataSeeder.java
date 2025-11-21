package com.example.monitoring.config;

import com.example.monitoring.entities.User;
import com.example.monitoring.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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