package com.guide_wire.demo.service;

import com.guide_wire.demo.entity.*;
import com.guide_wire.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final KYCRepository kycRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final BankDetailsRepository bankRepository;

    public void checkKycVerified(User user) {
        KYC kyc = kycRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("KYC not found"));

        if (!"VERIFIED".equals(kyc.getKycStatus())) {
            throw new RuntimeException("KYC not verified");
        }
    }

    public void checkCompanyVerified(User user) {
        UserCompany uc = userCompanyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Company not linked"));

        if (!"VERIFIED".equals(uc.getVerificationStatus())) {
            throw new RuntimeException("Company not verified");
        }
    }

    public void checkBankAdded(User user) {
        bankRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Bank details not added"));
    }

    public void checkBankVerified(User user) {
        BankDetails bank = bankRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        if (!bank.isVerified()) {
            throw new RuntimeException("Bank not verified");
        }
    }
}