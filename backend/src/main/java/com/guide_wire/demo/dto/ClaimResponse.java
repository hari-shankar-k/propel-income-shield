package com.guide_wire.demo.dto;

import com.guide_wire.demo.entity.ClaimStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClaimResponse {

    private Long id;
    private Double claimAmount;
    private String reason;
    private ClaimStatus status;
    private LocalDateTime createdAt;
}