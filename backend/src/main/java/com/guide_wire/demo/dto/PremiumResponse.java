package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumResponse {

    private double suggestedPremium;
    private double minPremium;
    private double maxPremium;
    private String riskLevel;
    private String recommendedPlan;
    private String reason;
}