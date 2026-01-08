package com.campus.lostfound.controller;

import com.campus.lostfound.entity.ChatMessage;
import com.campus.lostfound.entity.MatchRecord;
import com.campus.lostfound.entity.MessageRequest;
import com.campus.lostfound.repository.ChatMessageRepository;
import com.campus.lostfound.repository.MatchRecordRepository;
import com.campus.lostfound.repository.MessageRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    private final ChatMessageRepository chatRepo;
    private final MatchRecordRepository matchRepo;
    private final MessageRequestRepository requestRepo;

    public ChatHistoryController(
            ChatMessageRepository chatRepo,
            MatchRecordRepository matchRepo,
            MessageRequestRepository requestRepo
    ) {
        this.chatRepo = chatRepo;
        this.matchRepo = matchRepo;
        this.requestRepo = requestRepo;
    }

    // 📜 CHAT HISTORY
    @GetMapping("/{matchId}")
    public List<ChatMessage> getChatHistory(
            @PathVariable Long matchId,
            HttpServletRequest request
    ) {

        String email = (String) request.getAttribute("userEmail");

        MatchRecord match = matchRepo.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        MessageRequest mr = requestRepo.findByMatchRecord_Id(matchId)
                .orElseThrow(() -> new RuntimeException("Message request not found"));

        if (!"ACCEPTED".equals(mr.getStatus())) {
            throw new RuntimeException("Chat not allowed");
        }

        boolean isParticipant =
                email.equals(match.getLostItem().getOwnerEmail()) ||
                        email.equals(match.getFoundItem().getFinderEmail());

        if (!isParticipant) {
            throw new RuntimeException("Unauthorized");
        }

        return chatRepo.findByMatchIdOrderByCreatedAtAsc(matchId);
    }
}
