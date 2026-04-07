package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.*;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(AiIntegrationService.class);

    private final RestTemplate restTemplate;
    private final WeatherService weatherService;

    private static final String PREDICT_URL = "http://localhost:5000/predict";
    private static final String PREMIUM_URL = "http://localhost:5001/ai/premium";

    /**
     * 🔥 STEP 1: CALL OLD AI (RISK)
     */
    @Retry(name = "aiService", fallbackMethod = "fallbackEvaluateRisk")
    public AiResponse evaluateRisk(AiRequest request, double distance) {

        // 🌦️ Get real weather data
        WeatherService.WeatherData weather =
                weatherService.getWeatherData(
                        request.getLatitude(),
                        request.getLongitude()
                );

        // 🔥 Build ML request
        Map<String, Object> mlRequest = new HashMap<>();

        mlRequest.put("distance", distance);
        mlRequest.put("time_stationary", request.getInactivityHours());

        mlRequest.put("temperature", weather.temperature());
        mlRequest.put("humidity", weather.humidity());
        mlRequest.put("weather", weather.weatherCode());

        mlRequest.put("hour", java.time.LocalDateTime.now().getHour());
        mlRequest.put("day", java.time.LocalDate.now().getDayOfWeek().getValue());

        mlRequest.put("is_stationary", request.getInactivityHours() > 2 ? 1 : 0);
        mlRequest.put("is_bad_weather", weather.weatherCode() >= 2 ? 1 : 0);
        mlRequest.put("is_night", java.time.LocalTime.now().getHour() >= 20 ? 1 : 0);

        log.info("=== STEP 1: Calling Risk AI ===");
        log.info("ML Request → {}", mlRequest);

        AiResponse response = restTemplate.postForObject(
                PREDICT_URL,
                mlRequest,
                AiResponse.class
        );

        if (response == null) {
            throw new RuntimeException("Risk AI returned null response");
        }

        log.info("Response ← {}", response);

        return response;
    }

    /**
     * ⚠️ FALLBACK
     */
    public AiResponse fallbackEvaluateRisk(AiRequest request, Exception ex) {
        log.warn("⚠️ Risk AI FAILED → fallback", ex);

        return AiResponse.builder()
                .riskScore(50.0)
                .shouldCreateClaim(false)
                .build();
    }

    /**
     * 🔥 STEP 2: CALL PREMIUM AI
     */
    public PremiumResponse getDynamicPremium(PremiumRequest request) {

        try {
            log.info("=== STEP 2: Calling Premium AI ===");
            log.info("Request → {}", request);

            Map<String, Object> responseMap = restTemplate.postForObject(
                    PREMIUM_URL,
                    request,
                    Map.class
            );

            log.info("🔥 RAW Premium Response → {}", responseMap);

            if (responseMap == null) {
                throw new RuntimeException("Premium AI returned null");
            }

// 🔥 MANUAL MAPPING
            PremiumResponse response = PremiumResponse.builder()
                    .suggestedPremium(((Number) responseMap.get("suggestedPremium")).doubleValue())
                    .minPremium(((Number) responseMap.get("minPremium")).doubleValue())
                    .maxPremium(((Number) responseMap.get("maxPremium")).doubleValue())
                    .riskLevel((String) responseMap.get("riskLevel"))
                    .recommendedPlan((String) responseMap.get("recommendedPlan"))
                    .reason((String) responseMap.get("reason"))
                    .build();

            log.info("Response ← {}", response);

            return response;

        } catch (Exception e) {

            log.warn("⚠️ Premium AI FAILED → fallback", e);

            return PremiumResponse.builder()
                    .suggestedPremium(500.0)
                    .minPremium(400.0)
                    .maxPremium(800.0)
                    .riskLevel("MEDIUM")
                    .recommendedPlan("PLAN_B")
                    .reason("Fallback due to AI failure")
                    .build();
        }
    }
}