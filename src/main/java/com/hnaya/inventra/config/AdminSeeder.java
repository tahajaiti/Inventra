package com.hnaya.inventra.config;

import com.hnaya.inventra.entity.User;
import com.hnaya.inventra.entity.enums.Role;
import com.hnaya.inventra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private static final String ADMIN_EMAIL = "admin@admin.com";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.init-login:admin}")
    private String adminUsername;

    @Value("${app.admin.init-password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {

        if (!StringUtils.hasText(adminPassword)) {
            throw new IllegalStateException(
                    "Admin password must be provided via 'app.admin.init-password'"
            );
        }

        if (userRepository.existsByUsername(adminUsername)) {
            log.info("Admin user already exists, skipping seeding");
            return;
        }

        User admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .name("Admin")
                .lastName("System")
                .email(ADMIN_EMAIL)
                .role(Role.ADMIN)
                .active(true)
                .build();

        userRepository.save(admin);

        log.info("Admin user created successfully with username: {}", adminUsername);
    }
}
