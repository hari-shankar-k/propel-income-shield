package com.guide_wire.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AutomationService {

    private final DailyEvaluationService dailyEvaluationService;
    private static final Logger log = LoggerFactory.getLogger(DailyEvaluationService.class);

    // ⏰ RUN EVERY DAY AT MIDNIGHT
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyEvaluation() {

        log.info(" Running Daily Evaluation...");

        try {
            // ✅ Single call (handles everything internally)
            dailyEvaluationService.evaluateDailyActivity();

        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage());
        }

        log.warn("AI FAILED → fallback logic triggered");
    }
}