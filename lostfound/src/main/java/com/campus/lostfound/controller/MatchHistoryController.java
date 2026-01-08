package com.campus.lostfound.controller;

import com.campus.lostfound.dto.MatchResponseDTO;
import com.campus.lostfound.repository.MatchRecordRepository;
import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchHistoryController {

    private final MatchRecordRepository repo;

    public MatchHistoryController(MatchRecordRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/my")
    public List<MatchResponseDTO> myMatches(HttpServletRequest request) {

        String email = (String) request.getAttribute("userEmail");

        return repo.findMyMatches(email).stream()
                .map(r -> new MatchResponseDTO(
                        r.getId(),
                        r.getScore(),
                        r.getStatus(),
                        r.getLostItem().getTitle(),
                        r.getFoundItem().getTitle()
                ))
                .toList();
    }

    @GetMapping("/all")
    public List<MatchResponseDTO> allMatches(HttpServletRequest request) {

        if (!RoleUtil.isAdmin(request)) {
            throw new RuntimeException("Admins only");
        }

        return repo.findAllWithItems().stream()
                .map(r -> new MatchResponseDTO(
                        r.getId(),
                        r.getScore(),
                        r.getStatus(),
                        r.getLostItem().getTitle(),
                        r.getFoundItem().getTitle()
                ))
                .toList();
    }
}
