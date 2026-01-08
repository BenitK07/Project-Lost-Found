package com.campus.lostfound.controller;

import com.campus.lostfound.dto.MatchResult;
import com.campus.lostfound.entity.FoundItem;
import com.campus.lostfound.entity.LostItem;
import com.campus.lostfound.repository.LostItemRepository;
import com.campus.lostfound.security.RoleUtil;
import com.campus.lostfound.service.MatchingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final LostItemRepository lostRepo;
    private final MatchingService matchingService;

    public MatchController(
            LostItemRepository lostRepo,
            MatchingService matchingService
    ) {
        this.lostRepo = lostRepo;
        this.matchingService = matchingService;
    }

    @GetMapping("/lost/{lostItemId}")
    public List<MatchResult<FoundItem>> matchLostItem(
            @PathVariable Long lostItemId,
            HttpServletRequest request
    ) {
        LostItem lostItem = lostRepo.findById(lostItemId)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));

        String email = (String) request.getAttribute("userEmail");

        if (RoleUtil.isStudent(request) &&
                !lostItem.getOwnerEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        return matchingService.matchLostItem(lostItem);
    }
}
