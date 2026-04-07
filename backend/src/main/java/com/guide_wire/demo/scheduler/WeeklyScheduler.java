package com.guide_wire.demo.scheduler;

import com.guide_wire.demo.service.WeeklyRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyScheduler {

    private final WeeklyRewardService weeklyRewardService;

    /**
     * ⏰ Runs every Sunday at 11:59 PM
     */
    @Scheduled(cron = "0 59 23 ? * SUN")
    public void runWeeklyRewardJob() {

        System.out.println("Running Weekly Reward Job...");

        weeklyRewardService.processWeeklyRewards();
    }
}