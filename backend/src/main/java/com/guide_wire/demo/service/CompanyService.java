package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.CompanyRequest;
import com.guide_wire.demo.entity.Company;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.entity.UserCompany;
import com.guide_wire.demo.enums.NotificationPriority;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.repository.CompanyRepository;
import com.guide_wire.demo.repository.UserCompanyRepository;
import com.guide_wire.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final NotificationService notificationService;

    public String joinCompany(CompanyRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findByCompanyCode(request.getCompanyCode())
                .orElseThrow(() -> new RuntimeException("Invalid company code"));

        userCompanyRepository.findByUser(user).ifPresent(u -> {
            throw new RuntimeException("User already linked to a company");
        });

        UserCompany userCompany = UserCompany.builder()
                .user(user)
                .company(company)
                .employeeId(request.getEmployeeId())
                .verificationStatus("PENDING")
                .build();

        userCompanyRepository.save(userCompany);

        // 🔔 Notification
        notificationService.createNotification(
                user,
                "Company Request Submitted",
                "Your company verification request is under review.",
                NotificationType.SYSTEM,
                NotificationPriority.MEDIUM
        );

        return "Company request submitted. Awaiting verification.";
    }

    // 🔥 ADMIN VERIFY
    public String verifyCompany(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserCompany uc = userCompanyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        uc.setVerificationStatus("VERIFIED");
        userCompanyRepository.save(uc);

        // 🔔 Notification
        notificationService.createNotification(
                user,
                "Company Verified",
                "Your company details have been verified.",
                NotificationType.SYSTEM,
                NotificationPriority.MEDIUM
        );

        return "Company verified successfully";
    }
}