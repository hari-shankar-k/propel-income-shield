package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.InactivityLog;
import com.guide_wire.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InactivityLogRepository extends JpaRepository<InactivityLog, Long> {

    List<InactivityLog> findByUser(User user);
}