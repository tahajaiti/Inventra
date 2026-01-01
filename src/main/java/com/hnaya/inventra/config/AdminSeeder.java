package com.hnaya.inventra.config;

import com.hnaya.inventra.entity.User;
import com.hnaya.inventra.entity.enums.Role;
import com.hnaya.inventra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_EMAIL = "admin@admin.com";

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByUsername(ADMIN_USERNAME)) {
            User admin = User.builder()
                    .username(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .name("Admin")
                    .lastName("System")
                    .email(ADMIN_EMAIL)
                    .role(Role.ADMIN)
                    .active(true)
                    .assignedWarehouse(null)
                    .build();

            userRepository.save(admin);
            log.info("Admin user created successfully - Username: {}, Password: {}", ADMIN_USERNAME, ADMIN_PASSWORD);
        } else {
            log.info("Admin user already exists, skipping seeding");
        }
    }
}
