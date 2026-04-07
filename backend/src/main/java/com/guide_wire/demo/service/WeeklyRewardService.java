package com.guide_wire.demo.service;

import com.guide_wire.demo.entity.WeeklyUsage;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.repository.WeeklyUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyRewardService {

    private final WeeklyUsageRepository weeklyUsageRepository;
    private final RewardService rewardService;

    // 🎯 Conversion rate (can be dynamic later)
    private static final int POINTS_PER_HOUR = 10;

    /**
     * 🔥 MAIN METHOD → Called by Scheduler
     */
    public void processWeeklyRewards() {

        LocalDate weekStart = getCurrentWeekStart();

        // 📊 Get all users' weekly usage
        List<WeeklyUsage> usages = weeklyUsageRepository.findByWeekStartDate(weekStart);

        for (WeeklyUsage usage : usages) {

            int remainingHours = usage.getRemainingHours();

            if (remainingHours > 0) {

                int rewardPoints = remainingHours * POINTS_PER_HOUR;

                // 🎁 Save in usage
                usage.setRewardPoints(rewardPoints);

                // 💰 Add to reward system
                User user = usage.getUser();
                rewardService.earnPoints(user, rewardPoints, "Weekly unused hours reward");

                weeklyUsageRepository.save(usage);
            }
        }
    }

    private LocalDate getCurrentWeekStart() {
        return LocalDate.now().with(java.time.DayOfWeek.MONDAY);
    }
}