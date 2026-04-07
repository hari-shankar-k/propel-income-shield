package com.guide_wire.demo.service;

import com.guide_wire.demo.entity.Reward;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.NotificationPriority;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.enums.RewardType;
import com.guide_wire.demo.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final NotificationService notificationService;

    /**
     * ✅ Earn reward points
     */
    public void earnPoints(User user, int points, String reason) {

        Reward reward = Reward.builder()
                .user(user)
                .points(points)
                .type(RewardType.EARNED)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();

        rewardRepository.save(reward);

        // 🔔 Notify user
        notificationService.createNotification(
                user,
                "🎉 Reward Earned!",
                "You earned " + points + " points. Reason: " + reason,
                NotificationType.REWARD,
                NotificationPriority.MEDIUM
        );
    }

    /**
     * ❌ Redeem points
     */
    public void redeemPoints(User user, int points, String reason) {

        int totalPoints = getTotalPoints(user);

        if (totalPoints < points) {
            throw new RuntimeException("Not enough reward points");
        }

        Reward reward = Reward.builder()
                .user(user)
                .points(points)
                .type(RewardType.REDEEMED)
                .reason(reason)
                .createdAt(LocalDateTime.now())
                .build();

        rewardRepository.save(reward);

        // 🔔 Notify user
        notificationService.createNotification(
                user,
                "💸 Points Redeemed",
                "You redeemed " + points + " points. Reason: " + reason,
                NotificationType.REWARD,
                NotificationPriority.MEDIUM
        );
    }

    /**
     * 📊 Get total available points
     */
    public int getTotalPoints(User user) {

        List<Reward> earned = rewardRepository.findByUserAndType(user, RewardType.EARNED);
        List<Reward> redeemed = rewardRepository.findByUserAndType(user, RewardType.REDEEMED);

        int totalEarned = earned.stream()
                .mapToInt(Reward::getPoints)
                .sum();

        int totalRedeemed = redeemed.stream()
                .mapToInt(Reward::getPoints)
                .sum();

        return totalEarned - totalRedeemed;
    }

    /**
     * 📜 Get reward history
     */
    public List<Reward> getUserRewards(User user) {
        return rewardRepository.findByUser(user);
    }
}