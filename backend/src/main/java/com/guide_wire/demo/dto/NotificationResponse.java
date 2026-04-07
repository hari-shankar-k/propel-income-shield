package com.guide_wire.demo.dto;

import com.guide_wire.demo.enums.NotificationPriority;
import com.guide_wire.demo.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder  // 🔥 IMPORTANT
public class NotificationResponse {

    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationPriority priority; // 🔥 add this (used in service)
    private boolean isRead;
    private LocalDateTime createdAt;
}