package com.campus.lostfound.repository;

import com.campus.lostfound.entity.MatchRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {

    int countByStatusAndLostItem_OwnerEmail(String status, String email);
    int countByStatusAndFoundItem_FinderEmail(String status, String email);

    long countByStatus(String status); // ✅ ADD THIS

    @Query("""
        SELECT m FROM MatchRecord m
        JOIN FETCH m.lostItem
        JOIN FETCH m.foundItem
        WHERE m.id = :id
    """)
    Optional<MatchRecord> findByIdWithItems(@Param("id") Long id);

    @Query("""
        SELECT m FROM MatchRecord m
        JOIN FETCH m.lostItem
        JOIN FETCH m.foundItem
        WHERE m.lostItem.ownerEmail = :email
    """)
    List<MatchRecord> findMyMatches(@Param("email") String email);

    @Query("""
        SELECT m FROM MatchRecord m
        JOIN FETCH m.lostItem
        JOIN FETCH m.foundItem
    """)
    List<MatchRecord> findAllWithItems();
}
