package com.campus.lostfound.controller;

import com.campus.lostfound.dto.UserProfileDTO;
import com.campus.lostfound.entity.User;
import com.campus.lostfound.repository.*;
import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepo;
    private final LostItemRepository lostRepo;
    private final FoundItemRepository foundRepo;
    private final MatchRecordRepository matchRepo;

    public ProfileController(
            UserRepository userRepo,
            LostItemRepository lostRepo,
            FoundItemRepository foundRepo,
            MatchRecordRepository matchRepo
    ) {
        this.userRepo = userRepo;
        this.lostRepo = lostRepo;
        this.foundRepo = foundRepo;
        this.matchRepo = matchRepo;
    }

    // 🔒 PRIVATE PROFILE (self)
    @GetMapping("/me")
    public UserProfileDTO myProfile(HttpServletRequest request) {

        String email = (String) request.getAttribute("userEmail");

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int lost = lostRepo.countByOwnerEmail(email);
        int found = foundRepo.countByFinderEmail(email);
        int matches =
                matchRepo.countByStatusAndLostItem_OwnerEmail("CONFIRMED", email) +
                        matchRepo.countByStatusAndFoundItem_FinderEmail("CONFIRMED", email);

        return new UserProfileDTO(
                user.getName(),
                user.getEmail(), // visible
                lost,
                found,
                matches
        );
    }

    // 🌍 PUBLIC PROFILE
    @GetMapping("/{email}")
    public UserProfileDTO publicProfile(@PathVariable String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int lost = lostRepo.countByOwnerEmail(email);
        int found = foundRepo.countByFinderEmail(email);
        int matches =
                matchRepo.countByStatusAndLostItem_OwnerEmail("CONFIRMED", email) +
                        matchRepo.countByStatusAndFoundItem_FinderEmail("CONFIRMED", email);

        return new UserProfileDTO(
                user.getName(),
                null, // 🔒 email hidden
                lost,
                found,
                matches
        );
    }
}
