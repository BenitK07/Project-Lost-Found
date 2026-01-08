package com.campus.lostfound.startup;

import com.campus.lostfound.entity.User;
import com.campus.lostfound.enums.Role;
import com.campus.lostfound.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminInitializer(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (!repo.existsByRole(Role.ADMIN)) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@campus.edu");
            admin.setPassword(encoder.encode("admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setVerified(true);
            repo.save(admin);
        }
    }
}
