package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.*;
import com.guide_wire.demo.entity.AiDecisionLog;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.repository.AiDecisionLogRepository;
import com.guide_wire.demo.repository.ClaimRepository;
import com.guide_wire.demo.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PremiumService {

    private final AiIntegrationService aiIntegrationService;
    private final ClaimRepository claimRepository;
    private final LocationRepository locationRepository;
    private final AiDecisionLogRepository logRepository;
    private final AccessControlService accessControlService; // 🔥 ADD THIS

    public PremiumResponse calculatePremium(User user,
                                            String workType,
                                            double avgInactivityHours) {

        // 🔒 ACCESS CONTROL (ADD THIS BLOCK)
        accessControlService.checkKycVerified(user);
        accessControlService.checkCompanyVerified(user);



        int pastClaims = claimRepository.countByUser(user);

        var locations = locationRepository
                .findTop2ByUserOrderByTimestampDesc(user);

        if (locations.size() < 2) {
            throw new RuntimeException("Not enough location data");
        }

        var latest = locations.get(0);
        var previous = locations.get(1);

        // 🔥 DISTANCE
        double distance = com.guide_wire.demo.util.DistanceUtil.calculateDistance(
                previous.getLatitude(),
                previous.getLongitude(),
                latest.getLatitude(),
                latest.getLongitude()
        );

        // 🔥 STEP 1: CALL OLD AI
        AiRequest aiRequest = AiRequest.builder()
                .latitude(latest.getLatitude())
                .longitude(latest.getLongitude())
                .pastClaims(pastClaims)
                .inactivityHours(avgInactivityHours)
                .build();

        AiResponse aiResponse = aiIntegrationService.evaluateRisk(aiRequest, distance);

        // 🔥 STEP 2: CREATE PREMIUM REQUEST
        PremiumRequest request = PremiumRequest.builder()
                .userId(user.getId())
                .workType(workType)
                .latitude(latest.getLatitude())
                .longitude(latest.getLongitude())
                .pastClaims(pastClaims)
                .avgInactivityHours(avgInactivityHours)
                .riskScore(aiResponse.getRiskScore())
                .isNewUser(pastClaims == 0)
                .build();

        // 🔥 CALL PREMIUM AI
        PremiumResponse response = aiIntegrationService.getDynamicPremium(request);

        // 🔥 SAVE LOG
        AiDecisionLog log = AiDecisionLog.builder()
                .user(user)
                .inactivityHours(avgInactivityHours)
                .pastClaims(pastClaims)
                .latitude(latest.getLatitude())
                .longitude(latest.getLongitude())
                .riskScore(aiResponse.getRiskScore())
                .decision(aiResponse.isShouldCreateClaim())
                .reason(response.getReason())
                .riskLevel(
                        response.getRiskLevel() != null
                                ? com.guide_wire.demo.enums.RiskLevel.valueOf(response.getRiskLevel())
                                : com.guide_wire.demo.enums.RiskLevel.MEDIUM
                )
                .createdAt(java.time.LocalDateTime.now())
                .build();

        logRepository.save(log);

        return response;
    }
}