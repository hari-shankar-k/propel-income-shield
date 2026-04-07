package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.NotificationResponse;
import com.guide_wire.demo.entity.Notification;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.service.NotificationService;
import com.guide_wire.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    // ✅ GET WITH FILTER
    @GetMapping
    public Page<NotificationResponse> getNotifications(
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) NotificationType type,
            Pageable pageable) {

        User user = userService.getCurrentUser();
        return notificationService.getUserNotifications(user, isRead, type, pageable);
    }

    // ✅ MARK ONE
    @PutMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "Notification marked as read";
    }

    // ✅ MARK ALL
    @PutMapping("/read-all")
    public String markAllRead() {
        User user = userService.getCurrentUser();
        notificationService.markAllAsRead(user);
        return "All notifications marked as read";
    }

    // ✅ UNREAD COUNT
    @GetMapping("/unread-count")
    public long getUnreadCount() {
        User user = userService.getCurrentUser();
        return notificationService.getUnreadCount(user);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "Notification deleted";
    }

    // ✅ ADMIN
    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
}