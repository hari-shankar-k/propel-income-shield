package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {

    private Long userId;
    private String companyCode;
    private String employeeId;
}