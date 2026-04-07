package com.guide_wire.demo.entity;

import com.guide_wire.demo.enums.PolicyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🏷️ Plan name (Plan A / B / C)
    @Column(nullable = false)
    private String planName;

    // 💰 Coverage amount
    @Column(nullable = false)
    private Double coverageAmount;

    // 💸 Premium
    @Column(nullable = false)
    private Double premium;

    // ⏱️ Plan hours (7 / 14 / 21)
    @Column(nullable = false)
    private int planHours;

    // 🟢 Policy status
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    // 📅 Start date
    @Column(nullable = false)
    private LocalDate startDate;

    // 📅 End date
    private LocalDate endDate;

    // 🔗 Relation with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}