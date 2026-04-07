package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.ClaimResponse;
import com.guide_wire.demo.entity.*;
import com.guide_wire.demo.enums.*;
import com.guide_wire.demo.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final NotificationService notificationService;
    private final AccessControlService accessControlService;

    // 🧾 MANUAL CLAIM
    public ClaimResponse createClaim(String email, Long policyId, Double amount, String reason) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔒 ACCESS CONTROL (ADD THIS)
        accessControlService.checkKycVerified(user);
        accessControlService.checkCompanyVerified(user);
        accessControlService.checkBankAdded(user);



        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (!policy.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Policy does not belong to user");
        }

        if (policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new RuntimeException("Policy is not active");
        }

        if (amount <= 0) {
            throw new RuntimeException("Invalid claim amount");
        }

        Claim claim = Claim.builder()
                .user(user)
                .policy(policy)
                .claimAmount(amount)
                .reason(reason)
                .status(ClaimStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        claimRepository.save(claim);

        notificationService.createNotification(
                user,
                "Claim Created",
                "Your claim has been successfully created.",
                NotificationType.CLAIM_CREATED,
                NotificationPriority.HIGH
        );

        return mapToDTO(claim);
    }

    // 📄 GET CLAIMS
    public List<ClaimResponse> getUserClaims(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return claimRepository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 🤖 AUTO CLAIM (AI FLOW)
    public void createClaimForUser(User user, double inactivityHours) {

        // 🔍 Find ACTIVE policy
        Policy policy = policyRepository
                .findByUserAndStatus(user, PolicyStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active policy"));

        Claim claim = Claim.builder()
                .user(user)
                .policy(policy)
                .claimAmount(calculateClaimAmount(inactivityHours)) // ✅ FIXED
                .reason("AI triggered claim due to inactivity")
                .status(ClaimStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        claimRepository.save(claim);

        notificationService.createNotification(
                user,
                "Auto Claim Generated",
                "AI detected income disruption. Claim created.",
                NotificationType.SYSTEM,
                NotificationPriority.HIGH
        );
    }

    // 📊 CLAIM COUNT (FOR AI INPUT)
    public int getUserClaimCount(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return claimRepository.countByUser(user);
    }

    // 🔥 CLAIM AMOUNT LOGIC
    private double calculateClaimAmount(double inactivityHours) {
        return inactivityHours * 100; // simple logic (can improve later)
    }

    // 📦 DTO MAPPER
    private ClaimResponse mapToDTO(Claim claim) {
        return ClaimResponse.builder()
                .id(claim.getId())
                .claimAmount(claim.getClaimAmount())
                .reason(claim.getReason())
                .status(claim.getStatus())
                .createdAt(claim.getCreatedAt())
                .build();
    }

    // 📄 GET ALL CLAIMS
    public List<ClaimResponse> getAllClaims() {
        return claimRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}