package com.campus.lostfound.controller;

import com.campus.lostfound.dto.FoundItemRequest;
import com.campus.lostfound.dto.MatchResult;
import com.campus.lostfound.entity.FoundItem;
import com.campus.lostfound.repository.FoundItemRepository;
import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {

    private final FoundItemRepository repository;

    public FoundItemController(FoundItemRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public List<MatchResult> createFoundItem(
            @RequestBody FoundItemRequest body,
            HttpServletRequest request
    ) {
        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Only students can report found items");
        }

        String email = (String) request.getAttribute("userEmail");

        FoundItem item = new FoundItem();
        item.setTitle(body.getTitle());
        item.setDescription(body.getDescription());
        item.setCategory(body.getCategory());
        item.setLocation(body.getLocation());
        item.setFinderEmail(email);
        item.setStatus("PENDING");
        item.setCreatedAt(LocalDateTime.now());

        repository.save(item);

        return List.of();
    }

    @GetMapping("/my")
    public List<FoundItem> myFoundItems(HttpServletRequest request) {
        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Students only");
        }
        String email = (String) request.getAttribute("userEmail");
        return repository.findByFinderEmail(email);
    }

    @GetMapping("/all")
    public List<FoundItem> allFoundItems(HttpServletRequest request) {
        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }
        return repository.findAll();
    }
}
