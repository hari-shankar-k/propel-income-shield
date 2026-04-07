package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.Notification;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserAndIsDeletedFalse(User user, Pageable pageable);

    Page<Notification> findByUserAndIsReadAndIsDeletedFalse(User user, boolean isRead, Pageable pageable);

    Page<Notification> findByUserAndTypeAndIsDeletedFalse(User user, NotificationType type, Pageable pageable);

    Page<Notification> findByUserAndIsReadAndTypeAndIsDeletedFalse(User user, boolean isRead, NotificationType type, Pageable pageable);

    long countByUserAndIsReadFalseAndIsDeletedFalse(User user);
}