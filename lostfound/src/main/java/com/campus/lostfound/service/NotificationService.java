package com.campus.lostfound.service;

import com.campus.lostfound.entity.Notification;
import com.campus.lostfound.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public void notify(String userEmail, String message) {

        Notification n = new Notification();
        n.setUserEmail(userEmail);
        n.setMessage(message);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());

        repo.save(n);
    }
}
