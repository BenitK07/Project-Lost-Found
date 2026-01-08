package com.campus.lostfound.repository;

import com.campus.lostfound.entity.AbuseReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbuseReportRepository extends JpaRepository<AbuseReport, Long> {

    List<AbuseReport> findByReportedEmail(String email);

    List<AbuseReport> findByStatus(String status); // ✅ ADD THIS
}
