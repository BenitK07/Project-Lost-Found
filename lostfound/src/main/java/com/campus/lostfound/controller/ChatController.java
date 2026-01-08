package com.campus.lostfound.controller;

import com.campus.lostfound.entity.ChatMessage;
import com.campus.lostfound.entity.MatchRecord;
import com.campus.lostfound.entity.MessageRequest;
import com.campus.lostfound.repository.ChatMessageRepository;
import com.campus.lostfound.repository.MatchRecordRepository;
import com.campus.lostfound.repository.MessageRequestRepository;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatRepo;
    private final MatchRecordRepository matchRepo;
    private final MessageRequestRepository requestRepo;

    public ChatController(
            SimpMessagingTemplate messagingTemplate,
            ChatMessageRepository chatRepo,
            MatchRecordRepository matchRepo,
            MessageRequestRepository requestRepo
    ) {
        this.messagingTemplate = messagingTemplate;
        this.chatRepo = chatRepo;
        this.matchRepo = matchRepo;
        this.requestRepo = requestRepo;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload Map<String, String> payload,
            @Header("simpSessionAttributes") Map<String, Object> session
    ) {

        Long matchId = Long.valueOf(payload.get("matchId"));
        String text = payload.get("message");
        String sender = (String) session.get("userEmail");

        MatchRecord match = matchRepo.findById(matchId)
                .orElseThrow();

        MessageRequest mr =
                requestRepo.findByMatchRecord_Id(matchId)
                        .orElseThrow();

        if (!"ACCEPTED".equals(mr.getStatus())) {
            throw new RuntimeException("Chat not allowed");
        }

        if (!sender.equals(match.getLostItem().getOwnerEmail()) &&
                !sender.equals(match.getFoundItem().getFinderEmail())) {
            throw new RuntimeException("Unauthorized");
        }

        ChatMessage msg = new ChatMessage();
        msg.setMatchId(matchId);
        msg.setSenderEmail(sender);
        msg.setMessage(text);
        msg.setCreatedAt(LocalDateTime.now());

        chatRepo.save(msg);

        messagingTemplate.convertAndSend(
                "/topic/match." + matchId,
                msg
        );
    }
}
