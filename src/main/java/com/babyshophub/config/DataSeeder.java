package com.babyshophub.config;

import com.babyshophub.entity.User;
import com.babyshophub.enums.Role;
import com.babyshophub.repository.UserRepository;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@babyshophub.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("AdminSecurePassword123!"));
            admin.setPhoneNumber("08000000000");
            admin.setEnabled(true);
            admin.setDob(LocalDate.of(1990, 1, 1));

            admin.getRoles().add(Role.ROLE_ADMIN);

            userRepository.save(admin);
            System.out.println(">>> Default Admin account created successfully: " + adminEmail);
        }
    }
}