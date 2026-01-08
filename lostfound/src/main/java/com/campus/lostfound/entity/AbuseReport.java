package com.campus.lostfound.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "abuse_reports")
public class AbuseReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MatchRecord matchRecord;

    private String reporterEmail;
    private String reportedEmail;

    @Column(length = 1000)
    private String reason;

    // OPEN / REVIEWED / CLOSED
    private String status;

    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public MatchRecord getMatchRecord() { return matchRecord; }
    public void setMatchRecord(MatchRecord matchRecord) { this.matchRecord = matchRecord; }

    public String getReporterEmail() { return reporterEmail; }
    public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }

    public String getReportedEmail() { return reportedEmail; }
    public void setReportedEmail(String reportedEmail) { this.reportedEmail = reportedEmail; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
