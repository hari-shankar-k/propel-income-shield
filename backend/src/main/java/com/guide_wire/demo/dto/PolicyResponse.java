package com.guide_wire.demo.dto;

import java.time.LocalDate;

public class PolicyResponse {

    private Long id;
    private String planName;
    private Double coverageAmount;
    private Double premium;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

    public PolicyResponse(Long id, String planName, Double coverageAmount,
                          Double premium, String status,
                          LocalDate startDate, LocalDate endDate) {

        this.id = id;
        this.planName = planName;
        this.coverageAmount = coverageAmount;
        this.premium = premium;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public Long getId() { return id; }
    public String getPlanName() { return planName; }
    public Double getCoverageAmount() { return coverageAmount; }
    public Double getPremium() { return premium; }
    public String getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}