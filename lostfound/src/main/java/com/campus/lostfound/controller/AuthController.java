package com.campus.lostfound.controller;

import com.campus.lostfound.dto.LoginRequest;
import com.campus.lostfound.dto.RegisterRequest;
import com.campus.lostfound.entity.User;
import com.campus.lostfound.enums.Role;
import com.campus.lostfound.repository.UserRepository;
import com.campus.lostfound.security.JwtUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        if (repo.existsByEmail(req.getEmail())) return "Email exists";

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.STUDENT);
        user.setVerified(false);

        repo.save(user);
        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        User user = repo.findByEmail(req.getEmail()).orElseThrow();
        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid");

        return JwtUtil.generateToken(user.getEmail());
    }
}
