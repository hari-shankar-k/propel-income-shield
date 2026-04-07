package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.DashboardResponse;
import com.guide_wire.demo.entity.*;
import com.guide_wire.demo.enums.PolicyStatus;
import com.guide_wire.demo.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final RewardRepository rewardRepository;
    private final AiDecisionLogRepository aiDecisionLogRepository;

    public DashboardResponse getDashboard(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Active Policy
        Policy activePolicy = policyRepository
                .findByUserAndStatus(user, PolicyStatus.ACTIVE)
                .orElse(null);

        // ✅ Total Claims
        int totalClaims = claimRepository.countByUser(user);

        // ✅ Reward Points
        int rewardPoints = rewardRepository
                .findByUser(user)
                .stream()
                .mapToInt(Reward::getPoints)
                .sum();

        // ✅ Latest AI Risk
        AiDecisionLog latestLog = aiDecisionLogRepository
                .findTopByUserOrderByCreatedAtDesc(user)
                .orElse(null);

        String riskLevel = (latestLog != null)
                ? latestLog.getRiskLevel().name()
                : "UNKNOWN";

        return DashboardResponse.builder()
                .activePolicy(activePolicy)
                .totalClaims(totalClaims)
                .rewardPoints(rewardPoints)
                .riskLevel(riskLevel)
                .build();
    }
}