package com.guide_wire.demo.dto;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String workType;
    private LocalDateTime createdAt;

    public UserResponse(Long id, String name, String email, String phone, String workType, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.workType = workType;
        this.createdAt = createdAt;
    }

    // Getters

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getWorkType() { return workType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}