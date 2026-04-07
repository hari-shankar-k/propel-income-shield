package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.KycRequest;
import com.guide_wire.demo.entity.KYC;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.NotificationPriority;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.repository.KYCRepository;
import com.guide_wire.demo.repository.UserRepository;
import com.guide_wire.demo.security.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KYCService {

    private final KYCRepository kycRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public String uploadKyc(KycRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Prevent duplicate
        kycRepository.findByUser(user).ifPresent(k -> {
            throw new RuntimeException("KYC already submitted");
        });

        String encryptedAadhaar = EncryptionUtil.encrypt(request.getAadhaarNumber());
        String encryptedPan = EncryptionUtil.encrypt(request.getPanNumber());

        KYC kyc = KYC.builder()
                .user(user)
                .aadhaarNumber(encryptedAadhaar)
                .panNumber(encryptedPan)
                .aadhaarImage(request.getAadhaarImage())
                .panImage(request.getPanImage())
                .kycStatus("PENDING")
                .build();

        kycRepository.save(kyc);

        // 🔔 Notification
        notificationService.createNotification(
                user,
                "KYC Submitted",
                "Your KYC has been submitted and is under review.",
                NotificationType.SYSTEM,
                NotificationPriority.MEDIUM
        );

        return "KYC submitted successfully. Awaiting verification.";
    }

    // 🔥 ADMIN VERIFY METHOD (IMPORTANT)
    public String verifyKyc(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        KYC kyc = kycRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("KYC not found"));

        kyc.setKycStatus("VERIFIED");
        kycRepository.save(kyc);

        // 🔔 Notification
        notificationService.createNotification(
                user,
                "KYC Verified",
                "Your identity has been successfully verified.",
                NotificationType.SYSTEM,
                NotificationPriority.MEDIUM
        );

        return "KYC verified successfully";
    }
}