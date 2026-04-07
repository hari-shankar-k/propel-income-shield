package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.AiRequest;
import com.guide_wire.demo.dto.AiResponse;
import com.guide_wire.demo.entity.*;
import com.guide_wire.demo.enums.RiskLevel;
import com.guide_wire.demo.repository.*;
import com.guide_wire.demo.util.DistanceUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyEvaluationService {

    private static final Logger log = LoggerFactory.getLogger(DailyEvaluationService.class);

    private final InactivityLogRepository inactivityLogRepository;
    private final UserRepository userRepository;
    private final ClaimService claimService;
    private final RewardService rewardService;
    private final AiIntegrationService aiIntegrationService;
    private final AiDecisionLogRepository aiDecisionLogRepository;
    private final WeatherService weatherService;
    private final LocationRepository locationRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void evaluateDailyActivity() {

        List<User> users = userRepository.findAll();

        for (User user : users) {
            processUserEvaluation(user);
        }
    }

    public void evaluateSingleUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        processUserEvaluation(user);
    }

    private void processUserEvaluation(User user) {

        // 📊 Inactivity Calculation
        List<InactivityLog> logs = inactivityLogRepository.findByUser(user);

        double totalInactivityHours = logs.stream()
                .filter(log -> log.getStartTime() != null && log.getEndTime() != null)
                .mapToDouble(log ->
                        Duration.between(log.getStartTime(), log.getEndTime()).toMinutes() / 60.0
                )
                .sum();

        int pastClaims = claimService.getUserClaimCount(user.getId());

        double latitude = logs.isEmpty() ? 0 : logs.get(0).getLatitude();
        double longitude = logs.isEmpty() ? 0 : logs.get(0).getLongitude();

        // 🌦️ WEATHER (safe fallback)

        WeatherService.WeatherData weatherData;

        try {
            weatherData = weatherService.getWeatherCode(latitude, longitude);
        } catch (Exception e) {
            log.warn("Weather API failed for user {}", user.getId());
            weatherData = new WeatherService.WeatherData(30, 70, 2);
        }

        int weather = weatherData.weatherCode();
        double temperature = weatherData.temperature();
        double humidity = weatherData.humidity();
        // 📍 LOCATION TRACKING
        List<Location> recentLocations =
                locationRepository.findTop2ByUserOrderByTimestampDesc(user);

        double distance = 0;
        int isStationary = 1;

        if (recentLocations != null && recentLocations.size() >= 2) {

            Location loc1 = recentLocations.get(0);
            Location loc2 = recentLocations.get(1);

            if (loc1 != null && loc2 != null) {
                distance = DistanceUtil.calculateDistance(
                        loc1.getLatitude(), loc1.getLongitude(),
                        loc2.getLatitude(), loc2.getLongitude()
                );

                isStationary = (distance < 0.1) ? 1 : 0;
            }
        }

        // 🧠 AI REQUEST
        AiRequest aiRequest = AiRequest.builder()
                .userId(user.getId())
                .inactivityHours(totalInactivityHours)
                .latitude(latitude)
                .longitude(longitude)
                .pastClaims(pastClaims)
                .weather(weather)
                .distance(distance)
                .isStationary(isStationary)
                .build();

        try {

            AiResponse aiResponse = aiIntegrationService.evaluateRisk(aiRequest,distance);

            if (aiResponse == null) {
                throw new RuntimeException("AI returned null response");
            }

            // 🔥 SAFE RiskLevel conversion
            RiskLevel riskLevel;
            try {
                riskLevel = RiskLevel.valueOf(aiResponse.getRiskLevel());
            } catch (Exception ex) {
                riskLevel = RiskLevel.MEDIUM; // fallback
            }

            // 🪵 SAVE LOG
            AiDecisionLog logEntry = AiDecisionLog.builder()
                    .user(user)
                    .inactivityHours(totalInactivityHours)
                    .pastClaims(pastClaims)
                    .latitude(latitude)
                    .longitude(longitude)
                    .riskScore(aiResponse.getRiskScore())
                    .decision(aiResponse.isShouldCreateClaim())
                    .reason(aiResponse.getReason())
                    .riskLevel(riskLevel)
                    .createdAt(LocalDateTime.now())
                    .build();

            aiDecisionLogRepository.save(logEntry);

            // 🔥 CLEAN LOGGING
            log.info("User {} | RiskScore={} | Level={} | Decision={}",
                    user.getId(),
                    aiResponse.getRiskScore(),
                    riskLevel,
                    aiResponse.isShouldCreateClaim()
            );

            // 🎯 DECISION
            if (aiResponse.isShouldCreateClaim()) {

                claimService.createClaimForUser(user, totalInactivityHours);

            } else {

                rewardService.earnPoints(
                        user,
                        10,
                        "AI evaluated as active worker"
                );
            }

        } catch (Exception e) {

            log.warn("AI FAILED for user {}", user.getId());
            log.error("Error: {}", e.getMessage());

            if (totalInactivityHours > 5) {

                claimService.createClaimForUser(user, totalInactivityHours);

            } else {

                rewardService.earnPoints(
                        user,
                        5,
                        "Fallback: low inactivity"
                );
            }
        }
    }
}