package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.DashboardResponse;
import com.guide_wire.demo.security.SecurityUtils;
import com.guide_wire.demo.service.DashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard() {

        String email = SecurityUtils.getCurrentUserEmail();

        return dashboardService.getDashboard(email);
    }
}