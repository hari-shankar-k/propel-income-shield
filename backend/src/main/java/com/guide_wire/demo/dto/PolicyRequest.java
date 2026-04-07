package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyRequest {

    private String planName;   // PLAN_A / PLAN_B / PLAN_C
}