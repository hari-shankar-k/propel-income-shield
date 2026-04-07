package com.guide_wire.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "weekly_usage",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "week_start_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 📅 Week Identifier (Monday-based week recommended)
    @Column(name = "week_start_date", nullable = false)
    private LocalDate weekStartDate;

    // 📊 Plan hours (7 / 14 / 21)
    @Column(nullable = false)
    private int planHours;

    // ⏱️ Used hours (calculated from inactivity)
    @Column(nullable = false)
    private int usedHours;

    // 🎯 Remaining hours
    @Column(nullable = false)
    private int remainingHours;

    // 🎁 Reward points earned at week end
    @Column(nullable = false)
    private int rewardPoints;

    // 🕒 Last updated timestamp
    private LocalDateTime lastUpdated;
}