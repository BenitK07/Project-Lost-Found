package com.campus.lostfound.controller;

import com.campus.lostfound.entity.*;
import com.campus.lostfound.repository.*;
import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final LostItemRepository lostRepo;
    private final FoundItemRepository foundRepo;
    private final AbuseReportRepository reportRepo;
    private final UserRepository userRepo;
    private final MatchRecordRepository matchRepo;

    public AdminController(
            LostItemRepository lostRepo,
            FoundItemRepository foundRepo,
            AbuseReportRepository reportRepo,
            UserRepository userRepo,
            MatchRecordRepository matchRepo
    ) {
        this.lostRepo = lostRepo;
        this.foundRepo = foundRepo;
        this.reportRepo = reportRepo;
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
    }

    // 🔐 Admin guard
    private void adminOnly(HttpServletRequest request) {
        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }
    }

    // =========================
    // LOST ITEM MODERATION
    // =========================

    @GetMapping("/lost-items/pending")
    public List<LostItem> pendingLostItems(HttpServletRequest request) {
        adminOnly(request);
        return lostRepo.findByStatus("PENDING");
    }

    @PostMapping("/lost-items/{id}/approve")
    public String approveLostItem(@PathVariable Long id, HttpServletRequest request) {
        adminOnly(request);

        LostItem item = lostRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));

        item.setStatus("APPROVED");
        lostRepo.save(item);

        return "Lost item approved";
    }

    @PostMapping("/lost-items/{id}/reject")
    public String rejectLostItem(@PathVariable Long id, HttpServletRequest request) {
        adminOnly(request);

        LostItem item = lostRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lost item not found"));

        item.setStatus("REJECTED");
        lostRepo.save(item);

        return "Lost item rejected";
    }

    // =========================
    // FOUND ITEM MODERATION
    // =========================

    @GetMapping("/found-items/pending")
    public List<FoundItem> pendingFoundItems(HttpServletRequest request) {
        adminOnly(request);
        return foundRepo.findByStatus("PENDING");
    }

    @PostMapping("/found-items/{id}/approve")
    public String approveFoundItem(@PathVariable Long id, HttpServletRequest request) {
        adminOnly(request);

        FoundItem item = foundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Found item not found"));

        item.setStatus("APPROVED");
        foundRepo.save(item);

        return "Found item approved";
    }

    @PostMapping("/found-items/{id}/reject")
    public String rejectFoundItem(@PathVariable Long id, HttpServletRequest request) {
        adminOnly(request);

        FoundItem item = foundRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Found item not found"));

        item.setStatus("REJECTED");
        foundRepo.save(item);

        return "Found item rejected";
    }

    // =========================
    // ABUSE REPORT MODERATION
    // =========================

    @GetMapping("/reports")
    public List<AbuseReport> allReports(HttpServletRequest request) {
        adminOnly(request);
        return reportRepo.findAll();
    }

    @PostMapping("/reports/{id}/reviewed")
    public String markReviewed(@PathVariable Long id, HttpServletRequest request) {
        adminOnly(request);

        AbuseReport report = reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("REVIEWED");
        reportRepo.save(report);

        return "Report marked as reviewed";
    }

    @PostMapping("/reports/{id}/close")
    public String closeReport(@PathVariable Long id, HttpServletRequest request) {
        adminOnly(request);

        AbuseReport report = reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("CLOSED");
        reportRepo.save(report);

        return "Report closed";
    }

    // =========================
    // DASHBOARD STATS
    // =========================

    @GetMapping("/stats")
    public Map<String, Long> stats(HttpServletRequest request) {
        adminOnly(request);

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", userRepo.count());
        stats.put("lostItems", lostRepo.count());
        stats.put("foundItems", foundRepo.count());
        stats.put("pendingItems",
                (long) lostRepo.findByStatus("PENDING").size()
                        + foundRepo.findByStatus("PENDING").size()
        );
        stats.put("confirmedMatches", matchRepo.countByStatus("CONFIRMED"));
        stats.put("openReports", (long) reportRepo.findByStatus("OPEN").size());

        return stats;
    }
}
