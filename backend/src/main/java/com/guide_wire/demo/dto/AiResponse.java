package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponse {

    /**
     * 🔥 NEW AI RESPONSE (DIRECT FROM ML)
     */
    private double riskScore;          // 0–100
    private boolean shouldCreateClaim; // true/false

    /**
     * 🎯 Derived: Risk Level
     */
    public String getRiskLevel() {

        if (riskScore < 30) {
            return "LOW";
        } else if (riskScore < 70) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    /**
     * 🎯 Derived: Reason
     */
    public String getReason() {

        return shouldCreateClaim
                ? "High risk detected by ML model"
                : "User activity within safe limits";
    }
}