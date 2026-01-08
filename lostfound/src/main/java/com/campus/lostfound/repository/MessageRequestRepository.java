package com.campus.lostfound.repository;

import com.campus.lostfound.entity.MessageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MessageRequestRepository extends JpaRepository<MessageRequest, Long> {

    Optional<MessageRequest> findByMatchRecord_Id(Long matchId);

    List<MessageRequest> findByToUserEmail(String email);
}
