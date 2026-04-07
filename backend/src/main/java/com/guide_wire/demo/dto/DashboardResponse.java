package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private Object activePolicy;
    private int totalClaims;
    private int rewardPoints;
    private String riskLevel;
}