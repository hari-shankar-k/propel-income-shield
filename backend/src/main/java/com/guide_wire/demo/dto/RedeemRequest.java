package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedeemRequest {

    private int points;
    private String reason;
}