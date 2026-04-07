package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.Reward;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.enums.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    // 📊 Get all rewards of a user
    List<Reward> findByUser(User user);

    // 📊 Get rewards by type
    List<Reward> findByUserAndType(User user, RewardType type);

    // 🔢 Calculate total earned points
    List<Reward> findByUserAndTypeOrderByCreatedAtDesc(User user, RewardType type);
}