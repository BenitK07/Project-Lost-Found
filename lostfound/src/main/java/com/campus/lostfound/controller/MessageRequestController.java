package com.campus.lostfound.controller;

import com.campus.lostfound.entity.MatchRecord;
import com.campus.lostfound.entity.MessageRequest;
import com.campus.lostfound.repository.MatchRecordRepository;
import com.campus.lostfound.repository.MessageRequestRepository;
import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/message-requests")
public class MessageRequestController {

    private final MessageRequestRepository requestRepo;
    private final MatchRecordRepository matchRepo;

    public MessageRequestController(
            MessageRequestRepository requestRepo,
            MatchRecordRepository matchRepo
    ) {
        this.requestRepo = requestRepo;
        this.matchRepo = matchRepo;
    }

    // 📩 SEND MESSAGE REQUEST
    @PostMapping("/{matchId}")
    public Long sendRequest(
            @PathVariable Long matchId,
            @RequestBody String message,
            HttpServletRequest request
    ) {
        if (!RoleUtil.isStudent(request)) {
            throw new RuntimeException("Students only");
        }

        String fromEmail = (String) request.getAttribute("userEmail");

        MatchRecord match = matchRepo.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        if (!"CONFIRMED".equals(match.getStatus())) {
            throw new RuntimeException("Match not confirmed yet");
        }

        requestRepo.findByMatchRecord_Id(matchId)
                .ifPresent(r -> {
                    throw new RuntimeException("Message request already exists");
                });

        String toEmail =
                fromEmail.equals(match.getLostItem().getOwnerEmail())
                        ? match.getFoundItem().getFinderEmail()
                        : match.getLostItem().getOwnerEmail();

        MessageRequest mr = new MessageRequest();
        mr.setMatchRecord(match);
        mr.setFromUserEmail(fromEmail);
        mr.setToUserEmail(toEmail);
        mr.setMessage(message);
        mr.setStatus("PENDING");
        mr.setCreatedAt(LocalDateTime.now());

        requestRepo.save(mr);

        // ✅ return requestId
        return mr.getId();
    }

    // 📥 INBOX: view incoming requests
    @GetMapping("/inbox")
    public List<MessageRequest> inbox(HttpServletRequest request) {

        String email = (String) request.getAttribute("userEmail");

        return requestRepo.findByToUserEmail(email);
    }

    // ✅ ACCEPT REQUEST
    @PostMapping("/{id}/accept")
    public String accept(@PathVariable Long id, HttpServletRequest request) {

        MessageRequest mr = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        String email = (String) request.getAttribute("userEmail");

        if (!email.equals(mr.getToUserEmail())) {
            throw new RuntimeException("Not authorized");
        }

        mr.setStatus("ACCEPTED");
        requestRepo.save(mr);

        return "Message request accepted";
    }

    // ❌ REJECT REQUEST
    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, HttpServletRequest request) {

        MessageRequest mr = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        String email = (String) request.getAttribute("userEmail");

        if (!email.equals(mr.getToUserEmail())) {
            throw new RuntimeException("Not authorized");
        }

        mr.setStatus("REJECTED");
        requestRepo.save(mr);

        return "Message request rejected";
    }

    // 🚫 BLOCK REQUEST
    @PostMapping("/{id}/block")
    public String block(@PathVariable Long id, HttpServletRequest request) {

        MessageRequest mr = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        String email = (String) request.getAttribute("userEmail");

        if (!email.equals(mr.getToUserEmail())) {
            throw new RuntimeException("Not authorized");
        }

        mr.setStatus("BLOCKED");
        requestRepo.save(mr);

        return "User blocked for this match";
    }
}
