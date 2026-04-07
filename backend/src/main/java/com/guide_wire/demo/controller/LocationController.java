package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.LocationRequest;
import com.guide_wire.demo.security.JwtUtil;
import com.guide_wire.demo.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/update")
    public String updateLocation(
            @RequestHeader("Authorization") String token,
            @RequestBody LocationRequest request
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));
        locationService.updateLocation(email, request);

        return "Location updated successfully";
    }
}