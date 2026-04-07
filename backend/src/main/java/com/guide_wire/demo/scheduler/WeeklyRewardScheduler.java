package com.guide_wire.demo.scheduler;

import com.guide_wire.demo.entity.WeeklyUsage;
import com.guide_wire.demo.repository.WeeklyUsageRepository;
import com.guide_wire.demo.service.RewardService;
import com.guide_wire.demo.service.NotificationService;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.enums.NotificationPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WeeklyRewardScheduler {

    private final WeeklyUsageRepository weeklyUsageRepository;
    private final RewardService rewardService;
    private final NotificationService notificationService;

    /**
     * 🕛 Runs every Sunday midnight
     */
//
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void processWeeklyRewards() {

        List<WeeklyUsage> usages = weeklyUsageRepository.findAll();

        for (WeeklyUsage usage : usages) {

            User user = usage.getUser();

            int remainingHours = usage.getRemainingHours();

            if (remainingHours > 0) {

                int rewardPoints = remainingHours * 10;

                // 🎁 Give reward
                rewardService.earnPoints(
                        user,
                        rewardPoints,
                        "Weekly unused hours reward"
                );

                // 🔔 Notify user
                notificationService.createNotification(
                        user,
                        "Weekly Reward Earned",
                        "You earned " + rewardPoints + " points from unused hours!",
                        NotificationType.REWARD,
                        NotificationPriority.MEDIUM
                );
            }

            // 🔄 Reset weekly usage
            usage.setUsedHours(0);
            usage.setRemainingHours(usage.getPlanHours());
            usage.setRewardPoints(0);

            weeklyUsageRepository.save(usage);
        }
    }
}