package com.campus.lostfound.controller;

import com.campus.lostfound.entity.FoundItem;
import com.campus.lostfound.entity.LostItem;
import com.campus.lostfound.repository.FoundItemRepository;
import com.campus.lostfound.repository.LostItemRepository;
import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/moderation")
public class AdminModerationController {

    private final LostItemRepository lostRepo;
    private final FoundItemRepository foundRepo;

    public AdminModerationController(
            LostItemRepository lostRepo,
            FoundItemRepository foundRepo
    ) {
        this.lostRepo = lostRepo;
        this.foundRepo = foundRepo;
    }

    @PostMapping("/lost/{id}/approve")
    public String approveLost(@PathVariable Long id, HttpServletRequest request) {
        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }
        LostItem item = lostRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));
        item.setStatus("APPROVED");
        lostRepo.save(item);
        return "Lost item approved";
    }

    @PostMapping("/lost/{id}/reject")
    public String rejectLost(@PathVariable Long id, HttpServletRequest request) {
        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }
        LostItem item = lostRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));
        item.setStatus("REJECTED");
        lostRepo.save(item);
        return "Lost item rejected";
    }

    @PostMapping("/found/{id}/approve")
    public String approveFound(@PathVariable Long id, HttpServletRequest request) {
        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }
        FoundItem item = foundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Found item not found"));
        item.setStatus("APPROVED");
        foundRepo.save(item);
        return "Found item approved";
    }

    @PostMapping("/found/{id}/reject")
    public String rejectFound(@PathVariable Long id, HttpServletRequest request) {
        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }
        FoundItem item = foundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Found item not found"));
        item.setStatus("REJECTED");
        foundRepo.save(item);
        return "Found item rejected";
    }
}
