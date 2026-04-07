package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.AiDecisionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.guide_wire.demo.entity.User;

public interface AiDecisionLogRepository extends JpaRepository<AiDecisionLog, Long> {

    Optional<AiDecisionLog> findTopByUserOrderByCreatedAtDesc(User user);
}