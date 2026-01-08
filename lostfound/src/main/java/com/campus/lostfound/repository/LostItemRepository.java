package com.campus.lostfound.repository;

import com.campus.lostfound.entity.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    int countByOwnerEmail(String email);

    List<LostItem> findByOwnerEmail(String ownerEmail);

    List<LostItem> findByStatus(String status);


}
