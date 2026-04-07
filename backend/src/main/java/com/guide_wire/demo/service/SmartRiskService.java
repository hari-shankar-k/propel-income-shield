package com.guide_wire.demo.service;

import com.guide_wire.demo.entity.InactivityLog;
import com.guide_wire.demo.entity.Location;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.NotificationPriority;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.repository.InactivityLogRepository;
import com.guide_wire.demo.repository.LocationRepository;
import com.guide_wire.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartRiskService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final InactivityLogRepository inactivityLogRepository;
    private final NotificationService notificationService;

    public void checkSmartRisk(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Location> locations =
                locationRepository.findTop5ByUserOrderByTimestampDesc(user);

        if (locations.size() < 2) return;

        boolean stationary = isUserStationary(locations);

        long minutes = Duration.between(
                locations.get(locations.size() - 1).getTimestamp(),
                LocalDateTime.now()
        ).toMinutes();

        if (stationary && minutes > 10) {

            if (isRiskyCondition()) {

                // 💾 SAVE LOG
                saveInactivityLog(user, locations);

                // 🔔 FIXED (WITH PRIORITY)
                notificationService.createNotification(
                        user,
                        "Risk Detected",
                        "We detected unusual inactivity in your work pattern.",
                        NotificationType.RISK_ALERT,
                        NotificationPriority.MEDIUM
                );
            }
        }
    }

    private boolean isUserStationary(List<Location> locations) {

        Location first = locations.get(0);
        Location last = locations.get(locations.size() - 1);

        double latDiff = Math.abs(first.getLatitude() - last.getLatitude());
        double lonDiff = Math.abs(first.getLongitude() - last.getLongitude());

        return latDiff < 0.0005 && lonDiff < 0.0005;
    }

    private boolean isRiskyCondition() {
        return true;
    }

    private void saveInactivityLog(User user, List<Location> locations) {

        Location last = locations.get(0);

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = locations.get(locations.size() - 1).getTimestamp();

        long duration = Duration.between(start, end).toMinutes();

        InactivityLog log = InactivityLog.builder()
                .user(user)
                .startTime(start)
                .endTime(end)
                .durationMinutes(duration)
                .latitude(last.getLatitude())
                .longitude(last.getLongitude())
                .reason("UNKNOWN")
                .build();

        inactivityLogRepository.save(log);
    }
}