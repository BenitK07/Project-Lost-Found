package com.campus.lostfound.service;

import com.campus.lostfound.dto.MatchResult;
import com.campus.lostfound.entity.FoundItem;
import com.campus.lostfound.entity.LostItem;
import com.campus.lostfound.entity.MatchRecord;
import com.campus.lostfound.repository.FoundItemRepository;
import com.campus.lostfound.repository.LostItemRepository;
import com.campus.lostfound.repository.MatchRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingService {

    private final FoundItemRepository foundRepo;
    private final LostItemRepository lostRepo;
    private final MatchRecordRepository matchRepo;

    public MatchingService(
            FoundItemRepository foundRepo,
            LostItemRepository lostRepo,
            MatchRecordRepository matchRepo
    ) {
        this.foundRepo = foundRepo;
        this.lostRepo = lostRepo;
        this.matchRepo = matchRepo;
    }

    // 🔁 LOST ➜ FOUND
    public List<MatchResult<FoundItem>> matchLostItem(LostItem lostItem) {

        List<FoundItem> foundItems = foundRepo.findByStatus("APPROVED");
        List<MatchResult<FoundItem>> results = new ArrayList<>();

        for (FoundItem found : foundItems) {

            int score = calculateScore(
                    lostItem.getCategory(), found.getCategory(),
                    lostItem.getLocation(), found.getLocation(),
                    lostItem.getTitle(), found.getTitle(),
                    lostItem.getDescription(), found.getDescription()
            );

            if (score >= 50) {

                // ✅ CREATE MATCH RECORD
                MatchRecord record = new MatchRecord();
                record.setLostItem(lostItem);
                record.setFoundItem(found);
                record.setScore(score);
                record.setStatus("PENDING");
                record.setCreatedAt(LocalDateTime.now());

                matchRepo.save(record);

                results.add(
                        new MatchResult<>(
                                record.getId(),
                                found,
                                score,
                                record.getStatus()
                        )
                );
            }
        }
        return results;
    }

    // 🧠 SCORING LOGIC
    private int calculateScore(
            String cat1, String cat2,
            String loc1, String loc2,
            String title1, String title2,
            String desc1, String desc2
    ) {
        int score = 0;
        if (equals(cat1, cat2)) score += 40;
        if (equals(loc1, loc2)) score += 30;
        if (keywordMatch(title1, title2)) score += 20;
        if (keywordMatch(desc1, desc2)) score += 10;
        return score;
    }

    private boolean equals(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    private boolean keywordMatch(String a, String b) {
        if (a == null || b == null) return false;
        for (String word : a.toLowerCase().split("\\s+")) {
            if (word.length() > 3 && b.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }
}
