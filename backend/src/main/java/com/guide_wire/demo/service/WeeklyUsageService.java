package com.guide_wire.demo.service;

import com.guide_wire.demo.entity.Policy;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.entity.WeeklyUsage;
import com.guide_wire.demo.enums.PolicyStatus;
import com.guide_wire.demo.repository.PolicyRepository;
import com.guide_wire.demo.repository.WeeklyUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WeeklyUsageService {

    private final WeeklyUsageRepository weeklyUsageRepository;
    private final PolicyRepository policyRepository;

    /**
     * 🧠 MAIN METHOD → Called from DailyEvaluationService
     */
    public WeeklyUsage updateWeeklyUsage(User user, int inactivityHours) {

        LocalDate weekStart = getStartOfWeek();

        // 🔍 Get or create weekly record
        WeeklyUsage usage = weeklyUsageRepository
                .findByUserAndWeekStartDate(user, weekStart)
                .orElseGet(() -> createNewWeeklyUsage(user, weekStart));

        // ➕ Update used hours
        int updatedUsedHours = usage.getUsedHours() + inactivityHours;

        // ⚠️ Prevent exceeding plan
        if (updatedUsedHours > usage.getPlanHours()) {
            updatedUsedHours = usage.getPlanHours();
        }

        usage.setUsedHours(updatedUsedHours);

        // 🎯 Calculate remaining
        int remaining = usage.getPlanHours() - updatedUsedHours;
        usage.setRemainingHours(Math.max(remaining, 0));

        usage.setLastUpdated(LocalDateTime.now());

        return weeklyUsageRepository.save(usage);
    }

    /**
     * 🆕 Create weekly record
     */
    private WeeklyUsage createNewWeeklyUsage(User user, LocalDate weekStart) {

        // 📄 Fetch active policy
        Policy policy = policyRepository
                .findByUserAndStatus(user, PolicyStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Active policy not found"));

        int planHours = policy.getPlanHours(); // 👈 IMPORTANT

        return WeeklyUsage.builder()
                .user(user)
                .weekStartDate(weekStart)
                .planHours(planHours)
                .usedHours(0)
                .remainingHours(planHours)
                .rewardPoints(0)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    /**
     * 📅 Get Monday as start of week
     */
    private LocalDate getStartOfWeek() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }
}