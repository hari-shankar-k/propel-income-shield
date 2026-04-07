package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.WeeklyUsage;
import com.guide_wire.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface WeeklyUsageRepository extends JpaRepository<WeeklyUsage, Long> {

    // 🔍 Get usage for specific user + week
    Optional<WeeklyUsage> findByUserAndWeekStartDate(User user, LocalDate weekStartDate);

    // 📊 Get all users for a specific week (for scheduler)
    List<WeeklyUsage> findByWeekStartDate(LocalDate weekStartDate);

}