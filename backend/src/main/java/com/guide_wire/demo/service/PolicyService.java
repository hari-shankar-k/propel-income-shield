package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.PolicyResponse;
import com.guide_wire.demo.dto.PremiumResponse;
import com.guide_wire.demo.entity.Policy;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.PolicyStatus;
import com.guide_wire.demo.repository.PolicyRepository;
import com.guide_wire.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PolicyService {


    private final PremiumService premiumService;


    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    //  CREATE POLICY
    public Policy createPolicyByEmail(String email, Policy policy) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  VALIDATION
        if (policy.getCoverageAmount() == null || policy.getCoverageAmount() <= 0) {
            throw new RuntimeException("Coverage must be greater than 0");
        }

        if (policy.getStartDate() == null || policy.getEndDate() == null) {
            throw new RuntimeException("Dates cannot be null");
        }

        if (policy.getStartDate().isAfter(policy.getEndDate())) {
            throw new RuntimeException("Invalid dates");
        }

        if (policy.getStartDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Start date cannot be in the past");
        }

        //  BUSINESS LOGIC (Premium Calculation)
        if (policy.getCoverageAmount() <= 100000) {
            policy.setPremium(500.0);
        } else {
            policy.setPremium(1000.0);
        }

        //  STATUS
        policy.setStatus(PolicyStatus.ACTIVE);

        //  LINK USER
        policy.setUser(user);

        return policyRepository.save(policy);
    }

    //  GET USER POLICIES
    public List<PolicyResponse> getPoliciesByUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return policyRepository.findByUser(user)
                .stream()
                .map(p -> {

                    //  NULL SAFE + AUTO EXPIRE
                    if (p.getEndDate() != null && p.getEndDate().isBefore(LocalDate.now())) {
                        p.setStatus(PolicyStatus.EXPIRED);
                        // Optional: persist change
                        policyRepository.save(p);
                    }

                    return new PolicyResponse(
                            p.getId(),
                            p.getPlanName(),
                            p.getCoverageAmount(),
                            p.getPremium(),
                            p.getStatus() != null ? p.getStatus().name() : null,
                            p.getStartDate(),
                            p.getEndDate()
                    );
                })
                .toList();
    }
    public void purchasePolicy(String email, String planName) {

        // ✅ STEP 1: GET USER FIRST
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ STEP 2: CHECK ACTIVE POLICY
        Policy existingPolicy = policyRepository
                .findByUserAndStatus(user, PolicyStatus.ACTIVE)
                .orElse(null);

        if (existingPolicy != null) {
            existingPolicy.setStatus(PolicyStatus.EXPIRED);
            existingPolicy.setEndDate(LocalDate.now());
            policyRepository.save(existingPolicy);
        }

        // 🔥 STEP 3: GET PREMIUM
        PremiumResponse premium = premiumService.calculatePremium(
                user,
                user.getWorkType(),
                5
        );

        double finalPremium;

        switch (planName) {
            case "PLAN_A":
                finalPremium = premium.getMinPremium();
                break;
            case "PLAN_B":
                finalPremium = premium.getSuggestedPremium();
                break;
            case "PLAN_C":
                finalPremium = premium.getMaxPremium();
                break;
            default:
                throw new RuntimeException("Invalid plan");
        }

        // 🔥 STEP 4: SAVE POLICY
        Policy policy = Policy.builder()
                .planName(planName)
                .premium(finalPremium)
                .coverageAmount(getCoverage(planName))
                .planHours(getPlanHours(planName))
                .status(PolicyStatus.ACTIVE)
                .startDate(LocalDate.now())
                .user(user)
                .build();

        policyRepository.save(policy);
    }
    private double getCoverage(String planName) {
        return switch (planName) {
            case "PLAN_A" -> 50000;
            case "PLAN_B" -> 100000;
            case "PLAN_C" -> 200000;
            default -> 0;
        };
    }

    private int getPlanHours(String planName) {
        return switch (planName) {
            case "PLAN_A" -> 7;
            case "PLAN_B" -> 14;
            case "PLAN_C" -> 21;
            default -> 0;
        };
    }public PolicyResponse getActivePolicy(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Policy policy = policyRepository
                .findByUserAndStatus(user, PolicyStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active policy found"));

        return new PolicyResponse(
                policy.getId(),
                policy.getPlanName(),
                policy.getCoverageAmount(),
                policy.getPremium(),
                policy.getStatus().name(),
                policy.getStartDate(),
                policy.getEndDate()
        );
    }
}