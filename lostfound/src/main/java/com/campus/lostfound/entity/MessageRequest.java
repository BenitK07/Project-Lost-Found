package com.campus.lostfound.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message_requests")
public class MessageRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MatchRecord matchRecord;

    private String fromUserEmail;
    private String toUserEmail;

    @Column(length = 500)
    private String message;

    // PENDING / ACCEPTED / REJECTED / BLOCKED
    private String status;

    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public MatchRecord getMatchRecord() { return matchRecord; }
    public void setMatchRecord(MatchRecord matchRecord) { this.matchRecord = matchRecord; }

    public String getFromUserEmail() { return fromUserEmail; }
    public void setFromUserEmail(String fromUserEmail) { this.fromUserEmail = fromUserEmail; }

    public String getToUserEmail() { return toUserEmail; }
    public void setToUserEmail(String toUserEmail) { this.toUserEmail = toUserEmail; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
