package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycRequest {

    private Long userId;
    private String aadhaarNumber;
    private String panNumber;
    private String aadhaarImage;
    private String panImage;
}