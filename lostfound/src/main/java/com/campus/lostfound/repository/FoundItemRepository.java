package com.campus.lostfound.repository;

import com.campus.lostfound.entity.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    int countByFinderEmail(String email);


    List<FoundItem> findByFinderEmail(String finderEmail);

    List<FoundItem> findByStatus(String status);

}
