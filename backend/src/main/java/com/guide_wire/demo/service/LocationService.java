package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.LocationRequest;
import com.guide_wire.demo.entity.Location;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.repository.LocationRepository;
import com.guide_wire.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final SmartRiskService smartRiskService;

    // 📍 Update location
    public void updateLocation(String email, LocationRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔒 Validation
        if (request.getLatitude() == null || request.getLongitude() == null) {
            throw new RuntimeException("Invalid location data");
        }

        Location location = Location.builder()
                .user(user)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .timestamp(LocalDateTime.now())
                .build();

        locationRepository.save(location);
    }
}