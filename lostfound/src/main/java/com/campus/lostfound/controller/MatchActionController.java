package com.campus.lostfound.controller;

import com.campus.lostfound.entity.MatchRecord;
import com.campus.lostfound.repository.MatchRecordRepository;
import com.campus.lostfound.security.RoleUtil;
import com.campus.lostfound.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchActionController {

    private final MatchRecordRepository matchRepo;
    private final NotificationService notificationService;

    public MatchActionController(
            MatchRecordRepository matchRepo,
            NotificationService notificationService
    ) {
        this.matchRepo = matchRepo;
        this.notificationService = notificationService;
    }

    // ✅ CONFIRM MATCH
    @PostMapping("/{matchId}/confirm")
    public String confirmMatch(
            @PathVariable Long matchId,
            HttpServletRequest request
    ) {
        MatchRecord record = matchRepo.findByIdWithItems(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        validateOwnership(record, request);

        record.setStatus("CONFIRMED");
        matchRepo.save(record);

        notificationService.notify(
                record.getFoundItem().getFinderEmail(),
                "Your match was confirmed!"
        );

        return "Match confirmed";
    }

    @PostMapping("/{matchId}/reject")
    public String rejectMatch(
            @PathVariable Long matchId,
            HttpServletRequest request
    ) {
        MatchRecord record = matchRepo.findByIdWithItems(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        validateOwnership(record, request);

        record.setStatus("REJECTED");
        matchRepo.save(record);

        notificationService.notify(
                record.getFoundItem().getFinderEmail(),
                "A match was rejected."
        );

        return "Match rejected";
    }


    // 🔐 ownership validation
    private void validateOwnership(MatchRecord record, HttpServletRequest request) {

        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Only students can act on matches");
        }

        String email = (String) request.getAttribute("userEmail");

        boolean ownsLost =
                record.getLostItem() != null &&
                        email.equals(record.getLostItem().getOwnerEmail());

        boolean ownsFound =
                record.getFoundItem() != null &&
                        email.equals(record.getFoundItem().getFinderEmail());

        if (!ownsLost && !ownsFound) {
            throw new RuntimeException("This match does not belong to you");
        }
    }
}
