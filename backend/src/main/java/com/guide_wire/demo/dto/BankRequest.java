package com.guide_wire.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankRequest {

    private Long userId;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String accountHolderName;
}