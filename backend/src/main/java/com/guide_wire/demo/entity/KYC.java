package com.guide_wire.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KYC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private String aadhaarNumber;
    private String panNumber;

    private String aadhaarImage;
    private String panImage;

    private String kycStatus;
}