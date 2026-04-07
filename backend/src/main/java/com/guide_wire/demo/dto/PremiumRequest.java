package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumRequest {

    private Long userId;
    private String workType;


    private double latitude;
    private double longitude;

    private int pastClaims;
    private double avgInactivityHours;

    // 🔥 FROM AI
    private double riskScore;

    // 🔥 BUSINESS LOGIC
    private boolean isNewUser;
}