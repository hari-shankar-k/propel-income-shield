package com.guide_wire.demo.entity;

import com.guide_wire.demo.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_decision_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiDecisionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Link to user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 📊 Input data
    private double inactivityHours;
    private int pastClaims;
    private double latitude;
    private double longitude;

    // 🤖 AI output
    private double riskScore;
    private boolean decision; // true = claim, false = reward
    private String reason;
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    // ⏰ Timestamp
    private LocalDateTime createdAt;
}