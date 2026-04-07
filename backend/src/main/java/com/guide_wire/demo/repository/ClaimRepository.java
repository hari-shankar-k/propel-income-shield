package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.Claim;
import com.guide_wire.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByUser(User user);

    int countByUser(User user); // 🔥 ADD THIS
}