package com.campus.lostfound.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "match_records")
public class MatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private LostItem lostItem;


    @ManyToOne
    @JsonIgnore
    private FoundItem foundItem;

    private int score;

    // PENDING / CONFIRMED / REJECTED
    private String status;

    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public LostItem getLostItem() { return lostItem; }
    public void setLostItem(LostItem lostItem) { this.lostItem = lostItem; }

    public FoundItem getFoundItem() { return foundItem; }
    public void setFoundItem(FoundItem foundItem) { this.foundItem = foundItem; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
