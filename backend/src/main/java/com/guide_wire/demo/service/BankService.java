package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.BankRequest;
import com.guide_wire.demo.entity.*;
import com.guide_wire.demo.enums.NotificationPriority;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.repository.*;
import com.guide_wire.demo.security.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankDetailsRepository bankRepository;
    private final UserRepository userRepository;
    private final KYCRepository kycRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final NotificationService notificationService;

    public String addBankDetails(BankRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        KYC kyc = kycRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("KYC not completed"));

        if (!"VERIFIED".equals(kyc.getKycStatus())) {
            throw new RuntimeException("KYC not verified");
        }

        UserCompany uc = userCompanyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Company not linked"));

        if (!"VERIFIED".equals(uc.getVerificationStatus())) {
            throw new RuntimeException("Company not verified");
        }

        bankRepository.findByUser(user).ifPresent(b -> {
            throw new RuntimeException("Bank already added");
        });

        String encryptedAccount = EncryptionUtil.encrypt(request.getAccountNumber());

        BankDetails bank = BankDetails.builder()
                .user(user)
                .accountNumber(encryptedAccount)
                .ifscCode(request.getIfscCode())
                .bankName(request.getBankName())
                .accountHolderName(request.getAccountHolderName())
                .verified(false)
                .build();

        bankRepository.save(bank);

        // 🔔 Notification
        notificationService.createNotification(
                user,
                "Bank Added",
                "Your bank details have been added and are under verification.",
                NotificationType.SYSTEM,
                NotificationPriority.HIGH
        );

        return "Bank details added successfully. Awaiting verification.";
    }

    // 🔥 ADMIN VERIFY
    public String verifyBank(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BankDetails bank = bankRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        bank.setVerified(true);
        bankRepository.save(bank);

        // 🔔 Notification
        notificationService.createNotification(
                user,
                "Bank Verified",
                "Your bank account is verified. Payouts enabled.",
                NotificationType.SYSTEM,
                NotificationPriority.HIGH
        );

        return "Bank verified successfully";
    }
}