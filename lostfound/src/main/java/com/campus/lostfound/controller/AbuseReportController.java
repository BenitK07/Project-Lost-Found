package com.campus.lostfound.controller;

import com.campus.lostfound.entity.AbuseReport;
import com.campus.lostfound.entity.MatchRecord;
import com.campus.lostfound.repository.AbuseReportRepository;
import com.campus.lostfound.repository.MatchRecordRepository;
import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class AbuseReportController {

    private final AbuseReportRepository reportRepo;
    private final MatchRecordRepository matchRepo;

    public AbuseReportController(
            AbuseReportRepository reportRepo,
            MatchRecordRepository matchRepo
    ) {
        this.reportRepo = reportRepo;
        this.matchRepo = matchRepo;
    }

    // 🚩 REPORT ABUSE
    @PostMapping("/{matchId}")
    public String report(
            @PathVariable Long matchId,
            @RequestBody String reason,
            HttpServletRequest request
    ) {
        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Students only");
        }

        String reporter = (String) request.getAttribute("userEmail");

        MatchRecord match = matchRepo.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        boolean isParticipant =
                reporter.equals(match.getLostItem().getOwnerEmail()) ||
                        reporter.equals(match.getFoundItem().getFinderEmail());

        if (!isParticipant) {
            throw new RuntimeException("Not part of this match");
        }

        String reported =
                reporter.equals(match.getLostItem().getOwnerEmail())
                        ? match.getFoundItem().getFinderEmail()
                        : match.getLostItem().getOwnerEmail();

        AbuseReport report = new AbuseReport();
        report.setMatchRecord(match);
        report.setReporterEmail(reporter);
        report.setReportedEmail(reported);
        report.setReason(reason);
        report.setStatus("OPEN");
        report.setCreatedAt(LocalDateTime.now());

        reportRepo.save(report);

        return "Report submitted";
    }

    // 👑 ADMIN: VIEW ALL REPORTS
    @GetMapping("/all")
    public List<AbuseReport> allReports(HttpServletRequest request) {

        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }

        return reportRepo.findAll();
    }
}
