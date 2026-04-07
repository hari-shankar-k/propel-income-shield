package com.guide_wire.demo.controller;

import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.repository.UserRepository;
import com.guide_wire.demo.security.SecurityUtils;
import com.guide_wire.demo.service.PremiumService;
import com.guide_wire.demo.dto.PremiumResponse;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/premium")
@RequiredArgsConstructor
public class PremiumController {

    private final PremiumService premiumService;
    private final UserRepository userRepository;

    @RateLimiter(name = "premiumLimiter")
    @PostMapping("/calculate")
    public PremiumResponse calculatePremium(
            @RequestParam String workType,
            @RequestParam double avgInactivityHours // ✅ FIXED
    ) {

        User user = getLoggedInUser();

        return premiumService.calculatePremium(user, workType, avgInactivityHours);
    }

    private User getLoggedInUser() {
        String email = SecurityUtils.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}