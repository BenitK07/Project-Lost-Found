package com.campus.lostfound.controller;

import com.campus.lostfound.dto.LostItemRequest;
import com.campus.lostfound.dto.MatchResult;
import com.campus.lostfound.entity.LostItem;
import com.campus.lostfound.repository.LostItemRepository;
import com.campus.lostfound.security.RoleUtil;
import com.campus.lostfound.service.MatchingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemRepository repository;
    private final MatchingService matchingService;

    public LostItemController(
            LostItemRepository repository,
            MatchingService matchingService
    ) {
        this.repository = repository;
        this.matchingService = matchingService;
    }

    @PostMapping
    public List<MatchResult> createLostItem(
            @RequestBody LostItemRequest body,
            HttpServletRequest request
    ) {
        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Only students can create lost items");
        }

        String email = (String) request.getAttribute("userEmail");

        LostItem item = new LostItem();
        item.setTitle(body.getTitle());
        item.setDescription(body.getDescription());
        item.setCategory(body.getCategory());
        item.setLocation(body.getLocation());
        item.setOwnerEmail(email);
        item.setStatus("PENDING");
        item.setCreatedAt(LocalDateTime.now());

        repository.save(item);

        // matching only happens after approval
        return List.of();
    }

    @GetMapping("/my")
    public List<LostItem> myLostItems(HttpServletRequest request) {
        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Students only");
        }
        String email = (String) request.getAttribute("userEmail");
        return repository.findByOwnerEmail(email);
    }

    @GetMapping("/all")
    public List<LostItem> allLostItems(HttpServletRequest request) {
        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }
        return repository.findAll();
    }
}
