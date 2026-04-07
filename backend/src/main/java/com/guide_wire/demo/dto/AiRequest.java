package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRequest {

    private Long userId;

    private double inactivityHours;

    private double latitude;
    private double longitude;

    private int pastClaims;

    // 🔥 NEW FIELDS
    private int weather;
    private double distance;
    private int isStationary;
}