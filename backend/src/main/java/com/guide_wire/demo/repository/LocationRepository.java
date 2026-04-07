package com.guide_wire.demo.repository;

import com.guide_wire.demo.entity.Location;
import com.guide_wire.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByUser(User user);

    // 🔥 MOST IMPORTANT (FIX YOUR ERROR)
    Optional<Location> findTopByUserOrderByTimestampDesc(User user);

    // Existing ones (keep them)

    List<Location> findTop10ByUserOrderByTimestampDesc(User user);
    List<Location> findTop5ByUserOrderByTimestampDesc(User user);
    List<Location> findTop2ByUserOrderByTimestampDesc(User user);

}