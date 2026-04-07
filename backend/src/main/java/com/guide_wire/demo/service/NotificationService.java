package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.NotificationResponse;
import com.guide_wire.demo.entity.Notification;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.NotificationPriority;
import com.guide_wire.demo.enums.NotificationType;
import com.guide_wire.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // ✅ CREATE (DEFAULT)
    public void createNotification(User user,
                                   String title,
                                   String message,
                                   NotificationType type) {

        createNotification(user, title, message, type, NotificationPriority.MEDIUM);
    }

    // ✅ CREATE (FULL)
    public void createNotification(User user,
                                   String title,
                                   String message,
                                   NotificationType type,
                                   NotificationPriority priority) {

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .priority(priority)
                .isRead(false)
                .isDeleted(false)
                .build();

        notificationRepository.save(notification);
    }

    // ✅ GET (FILTER + PAGINATION)
    public Page<NotificationResponse> getUserNotifications(
            User user,
            Boolean isRead,
            NotificationType type,
            Pageable pageable) {

        Page<Notification> page;

        if (isRead != null && type != null) {
            page = notificationRepository
                    .findByUserAndIsReadAndTypeAndIsDeletedFalse(user, isRead, type, pageable);

        } else if (isRead != null) {
            page = notificationRepository
                    .findByUserAndIsReadAndIsDeletedFalse(user, isRead, pageable);

        } else if (type != null) {
            page = notificationRepository
                    .findByUserAndTypeAndIsDeletedFalse(user, type, pageable);

        } else {
            page = notificationRepository
                    .findByUserAndIsDeletedFalse(user, pageable);
        }

        return page.map(this::mapToDTO);
    }

    // ✅ MARK ONE AS READ
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // ✅ MARK ALL AS READ
    public void markAllAsRead(User user) {
        Page<Notification> page =
                notificationRepository.findByUserAndIsDeletedFalse(user, Pageable.unpaged());

        page.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(page.getContent());
    }

    // ✅ SOFT DELETE
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setDeleted(true);
        notificationRepository.save(notification);
    }

    // ✅ UNREAD COUNT
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndIsReadFalseAndIsDeletedFalse(user);
    }

    // ✅ ADMIN / DEBUG
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // ✅ DTO MAPPER
    private NotificationResponse mapToDTO(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .priority(n.getPriority())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}