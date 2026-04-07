package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.Policy;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    // 📄 Get ALL policies of a user
    List<Policy> findByUser(User user);

    // 🔥 BEST PRACTICE → Get ACTIVE policy (used in WeeklyUsageService)
    Optional<Policy> findByUserAndStatus(User user, PolicyStatus status);
}