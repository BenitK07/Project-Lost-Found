package com.campus.lostfound.controller;

import com.campus.lostfound.entity.Notification;
import com.campus.lostfound.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    // 🧑‍🎓 User views their notifications
    @GetMapping
    public List<Notification> myNotifications(HttpServletRequest request) {

        String email = (String) request.getAttribute("userEmail");
        return repo.findByUserEmailOrderByCreatedAtDesc(email);
    }
}
